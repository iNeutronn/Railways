import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CommonModule, NgClass, NgForOf} from '@angular/common';
import {animate, style, transition, trigger} from '@angular/animations';
import {CashDesk} from '../../models/entities/cash-desk';
import {Entrance} from '../../models/entities/entrance';
import {Client} from '../../models/entities/client';
import {PrivilegeEnum, PrivilegeEnumLabels} from '../../models/enums/privilege-enum';
import {StationConfigurationService} from '../../services/station-configuration/station-configuration.service';
import {Router} from '@angular/router';
import {StationConfiguration} from '../../models/station-configuration';
import {Position} from '../../models/position';
import {WebSocketService} from '../../services/socket/web-socket.service';
import {ClientCreatedDto} from '../../models/dtos/ClientCreatedDto';
import {SimulationService} from '../../services/simulation/simulation.service';
import {interval, Subscription, takeWhile} from 'rxjs';
import {MapPositionHelper} from '../../helpers/mapPositionHelper';
import {ClientServingStartedDto} from '../../models/dtos/ClientServingStartedDto';
import {ClientServedDto} from '../../models/dtos/ClientServedDto';
import { QueueUpdatedDto } from '../../models/dtos/QueueUpdatedDto';


@Component({
  selector: 'app-map-page',
  standalone: true,
  imports: [
    NgForOf,
    NgClass,
    CommonModule
  ],
  templateUrl: './map-page.component.html',
  styleUrl: './map-page.component.css',
  animations: [
    trigger('moveDown', [
      transition(':enter', [
        style({ top: '{{startTop}}px' }),
        animate('1000ms ease-out', style({ top: '{{endTop}}px' }))
      ]),
      transition(':leave', [
        animate('1000ms ease-out', style({ opacity: 0 }))
      ])
    ])
  ]
})
export class MapPageComponent implements OnInit {
  @ViewChild('stationContainer', {static: false}) stationContainer!: ElementRef;
  mapPositionHelper: MapPositionHelper = new MapPositionHelper();
  mapSize: {width: number, height: number} = {
    width: 0,
    height: 0
  }

  isSimulationActive: boolean = false;
  isSidebarOpen: boolean = false;
  cellSize: number = 50;

  entrances: Entrance[] = [];
  cashDesks: CashDesk[] = [];
  clients: Client[] = [];

  loggedClients: {
    clientID: number;
    firstName: string;
    lastName: string;
    ticketOfficeId: number;
    startTime: Date | null;
    endTime: Date | null;
  }[] = [];
  
  private servingClients: Map<number, Client> = new Map();

  currentTime: Date = new Date();
  private clockSubscription: Subscription;

  constructor(
    private configService: StationConfigurationService,
    private simulationService: SimulationService,
    private router: Router,
    private webSocketService: WebSocketService) {
    this.mapPositionHelper = new MapPositionHelper();
    
    // Update clock every second
    this.clockSubscription = interval(1000).subscribe(() => {
      this.currentTime = new Date();
    });
  }

  toggleSidebar(): void {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  ngOnInit() {
    this.webSocketService.connect();
    this.webSocketService.getCreatedClientsMessages().subscribe((message) => {
      console.log('Received message:', message);
      this.handleClientCreatedEvent(message);
    });

    this.webSocketService.getClientServingStartedMessages().subscribe((message) => {
      console.log('Received message:', message);
      this.handleClientServingStartedEvent(message);
    });

    this.webSocketService.getClientServedMessages().subscribe({
      next: (message) => {
        console.log('Component received ClientServed message:', message);
        this.handleClientServedEvent(message);
      },
      error: (error) => {
        console.error('Error in ClientServed subscription:', error);
      }
    });

    this.webSocketService.getQueueUpdatedMessages().subscribe((message) => {
      console.log('Received message:', message);
      this.handleQueueUpdatedEvent(message);
     });

    this.configService.getConfiguration().subscribe(configuration => {

      this.handleConfigurationResponse(configuration);
      console.log(configuration, 'Configuration');
      console.log(this.entrances, 'Entrances');
      console.log(this.cashDesks, 'cashDesks');
    });
  }


  handleClientServingStartedEvent(clientServing: ClientServingStartedDto): void {
    const { clientId, ticketOfficeId, timeNeeded } = clientServing.data;
    const client = this.clients.find(c => c.clientID === clientId);
    const ticketOffice = this.cashDesks.find(c => c.id === ticketOfficeId);

    if (client && ticketOffice) {
      this.servingClients.set(clientId, client);
      console.log(`Client ${clientId} started being served at ticket office ${ticketOfficeId}`);

      setTimeout(() => {
        console.log(`Client ${clientId} finished serving at ticket office ${ticketOfficeId}`);
        this.clients = this.clients.filter(c => c.clientID !== clientId);
        ticketOffice.queue = ticketOffice.queue.filter(c => c.clientID !== clientId);
        this.updateQueuePositions(ticketOffice);
      }, timeNeeded * 1000);
    } else {
      console.warn(`Client or ticket office not found for event:`, clientServing);
    }
  }


  handleClientCreatedEvent(clientCreated: ClientCreatedDto){
    const entrancePosition = this.entrances
      .find(e => e.id == clientCreated.data.entranceId)?.position;

    if(entrancePosition) {
      const clientPosition: Position = {
        x: entrancePosition.x,
        y: entrancePosition.y
      }
      const privilegeString = clientCreated.data.client.privilege; // e.g., "default"
      const privilege = PrivilegeEnum[privilegeString.toString().toUpperCase() as keyof typeof PrivilegeEnum];

      const client: Client = {
        clientID: clientCreated.data.client.clientID,
        position: clientPosition,
        firstName: clientCreated.data.client.firstName,
        lastName: clientCreated.data.client.lastName,
        ticketsToBuy: clientCreated.data.client.ticketsToBuy,
        privilege: privilege
      };

      const cashPoint = this.cashDesks.find(c => c.id == clientCreated.data.ticketOfficeId);

      if(cashPoint){
        console.log('Created client with id ' + client.clientID + ', assigned to ' + clientCreated.data.ticketOfficeId);
        this.clients = [...this.clients, client];

        this.moveClientToPoint(client.clientID, cashPoint);
      }
    }
  }

  handleQueueUpdatedEvent(queueUpdated: QueueUpdatedDto) {  
    const cashDesk = this.cashDesks.find(c => c.id === queueUpdated.data.ticketOfficeID);
    if (cashDesk && cashDesk.queue.length > 0) {    
      const firstClient = cashDesk.queue[0];
  
      const remainingClients = cashDesk.queue.slice(1);
  
      const sortedQueue = queueUpdated.data.queue.map(id => remainingClients.find(client => client.clientID === id))
      .filter(client => client !== undefined); 
 
      cashDesk.queue = [firstClient, ...sortedQueue];
    }}
  

  handleConfigurationResponse(configuration: StationConfiguration) {
    console.log(configuration.mapSize.height)
    this.mapSize.height = configuration.mapSize.height;
    this.mapSize.width = configuration.mapSize.width;

    console.log('mapSize', this.mapSize)
    console.log(this.mapSize);
    for (let i = 0; i < configuration.entranceConfigs.length; i++) {
      const position = this.mapPositionHelper.findFreeCellNear(
        this.clients,
        configuration.entranceConfigs[i].x,
        configuration.entranceConfigs[i].y
      )

      if(!position)
        return;

      this.entrances.push({
        id: configuration.entranceConfigs[i].id,
        position: position,
      })
    }

    for (let i =  0; i < configuration.cashpointConfigs.length; i++) {
      this.cashDesks.push({
        id: configuration.cashpointConfigs[i].id,
        serviceTime: configuration.maxServiceTime,
        isActive: true,
        isReserve: false,
        queue: [],
        position: {
          x: configuration.cashpointConfigs[i].x,
          y: configuration.cashpointConfigs[i].y
        },
      })
    }

    this.cashDesks.push({
      id: configuration.reservCashPointConfig.id,
      serviceTime: configuration.maxServiceTime,
      isActive: false,
      isReserve: true,
      queue: [],
      position: {
        x: configuration.reservCashPointConfig.x,
        y: configuration.reservCashPointConfig.y
      },
    })
  }

  getPrivilegeLabel(privilege: PrivilegeEnum): string {
    return PrivilegeEnumLabels[privilege];
  }

  moveClientToPoint(clientId: number, cashDesk: CashDesk, stepSize: number = 1, intervalMs: number = 200): Subscription {
    const clientIndex = this.clients.findIndex(client => client.clientID === clientId);
    if (clientIndex < 0) {
      console.error(`Client with ID ${clientId} not found.`);
      return null!;
    }

    const client = this.clients[clientIndex];
    let target: Position = this.mapPositionHelper.findCashServingPoint(this.mapSize, client, cashDesk);

    return interval(intervalMs).pipe(
      takeWhile(() => this.calculateDistance(client.position, target) != 0),
    ).subscribe(() => {

      target = this.mapPositionHelper.findCashServingPoint(this.mapSize, client, cashDesk);
      const distance = this.calculateDistance(client.position, target);

      if (distance == 1 && !cashDesk.queue.map(c => c.clientID).includes(client.clientID)) {
        this.joinQueue(client, cashDesk);
      }

      if (distance != 0) {
        this.moveClient(client, target, stepSize);
      }

      /*// Якщо клієнт досяг цілі, оновіть позиції інших клієнтів в черзі
      if (distance === 0 && client.clientID === cashDesk.queue[0]?.clientID) {
        this.updateQueuePositions(cashDesk);
      }*/
    });
  }

  updateQueuePositions(cashDesk: CashDesk) {
    // Оновлення позицій всіх клієнтів після того, як один з них вийшов з черги
    for (let i = 0; i < cashDesk.queue.length; i++) {
      const client = cashDesk.queue[i];
      let target = this.mapPositionHelper.findCashServingPoint(this.mapSize, client, cashDesk);
      this.moveClient(client, target, 1);
    }
  }


  private calculateDistance(position: Position, target: Position): number {
    const deltaX = target.x - position.x;
    const deltaY = target.y - position.y;
    return Math.sqrt(deltaX ** 2 + deltaY ** 2);
  }

  private joinQueue(client: Client, cashDesk: CashDesk): void {
    cashDesk.queue.push(client);
    console.log(`!!Client ${client.clientID} joined ${cashDesk.id} cash point queue`);
    this.webSocketService.sendJoinedQueueEvent(client, cashDesk.id);
  }

  private moveClient(client: Client, target: Position, stepSize: number): void {
    const deltaX = target.x - client.position.x;
    const deltaY = target.y - client.position.y;

    const moveX = Math.abs(deltaX) <= stepSize ? deltaX : Math.sign(deltaX) * stepSize;
    const moveY = Math.abs(deltaY) <= stepSize ? deltaY : Math.sign(deltaY) * stepSize;

    // Ensure that the new position is not occupied
    if (!this.mapPositionHelper.isCellOccupied(
      { x: client.position.x + moveX, y: client.position.y + moveY }, this.clients)) {
      client.position.x += moveX;
      client.position.y += moveY;
    }
  }

  startSimulation(){
    this.simulationService.startSimulation().subscribe(() => {
      this.isSimulationActive = true;
      console.log('Simulation started');
    });
  }

  stopSimulation(){
    if(!this.isSimulationActive){
      this.simulationService.resumeSimulation()
        .subscribe();
    }
    else{
      this.simulationService.pauseSimulation()
        .subscribe();
    }

    this.isSimulationActive = !this.isSimulationActive;
  }

  goToSettings(): void {
    this.router.navigate(['/start']);
  }

  ngOnDestroy() {
    this.webSocketService.closeConnection();
    if (this.clockSubscription) {
      this.clockSubscription.unsubscribe();
    }
  }

  handleClientServedEvent(clientServed: ClientServedDto): void {
    const { clientID, startTime, endTime, ticketOfficeID } = clientServed.data;
    
    const client = this.clients.find(c => c.clientID === clientID) || 
                  this.servingClients.get(clientID);
    
    if (client) {
      const parseDate = (dateStr: string) => {
        return new Date(dateStr.split('[')[0]);
      };

      this.loggedClients.push({
        clientID: clientID,
        firstName: client.firstName,
        lastName: client.lastName,
        ticketOfficeId: ticketOfficeID,
        startTime: parseDate(startTime),
        endTime: parseDate(endTime)
      });
      
      console.log('Added logged client:', {
        startTime: parseDate(startTime),
        endTime: parseDate(endTime)
      });
      
      this.servingClients.delete(clientID);
    } else {
      console.warn(`Client ${clientID} not found in either active or serving clients`);
    }
  }
}
