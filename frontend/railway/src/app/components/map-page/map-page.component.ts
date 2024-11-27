
import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {NgClass, NgForOf} from '@angular/common';
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

@Component({
  selector: 'app-map-page',
  standalone: true,
  imports: [
    NgForOf,
    NgClass
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
  maxIndexX: number = 0;
  maxIndexY: number = 0;
  cellSize: number = 50;

  entrances: Entrance[] = [];
  cashDesks: CashDesk[] = [];
  clients: Client[] = [];

  constructor(
    private configService: StationConfigurationService,
    private simulationService: SimulationService,
    private router: Router,
    private webSocketService: WebSocketService) {
    this.mapPositionHelper = new MapPositionHelper();
  }

  toggleSidebar(): void {
    this.isSidebarOpen = !this.isSidebarOpen;
  }
  
  ngOnInit() {
    this.webSocketService.connect();
    this.webSocketService.getMessages().subscribe((message) => {
      console.log('Received message:', message);
      this.handleClientCreatedEvent(message);
    });

    this.calculateScaleFactor();
    this.configService.getConfiguration().subscribe(configuration => {

      console.log(configuration, 'Configuration');
      this.handleConfigurationResponse(configuration);
      console.log(this.entrances, 'Entrances');
      console.log(this.cashDesks, 'cashDesks');
    });
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

  calculateScaleFactor() {
    if (this.stationContainer) {
      const containerWidth = this.stationContainer.nativeElement.getBoundingClientRect().width;
      const containerHeight = this.stationContainer.nativeElement.getBoundingClientRect().height;

      this.maxIndexX = Math.floor(containerHeight / this.cellSize) - 1;
      this.maxIndexY = Math.floor(containerWidth / this.cellSize) - 1;
    }
  }

  getPrivilegeLabel(privilege: PrivilegeEnum): string {
    return PrivilegeEnumLabels[privilege];
  }

  moveClientToPoint(clientId: number, cashDesk: CashDesk, stepSize: number = 1, intervalMs: number = 200): Subscription {
    const clientIndex = this.clients.findIndex(client => client.clientID === clientId);
    let target: Position = this.mapPositionHelper.findCashServingPoint(this.mapSize, cashDesk);

    console.log(cashDesk)
    console.log("TargetX: ", target.x);
    console.log("TargetY: ", target.y);

    if (clientIndex < 0) {
      console.error(`Client with ID ${clientId} not found.`);
      return null!;
    }

    const client = this.clients[clientIndex];

    return interval(intervalMs).pipe(
      takeWhile(() => {

        const deltaX = target.x - client.position.x;
        const deltaY = target.y - client.position.y;
        const distance = Math.sqrt(deltaX ** 2 + deltaY ** 2);

        return distance > 0;
      })
    ).subscribe(() => {
      const deltaX = target.x - client.position.x;
      const deltaY = target.y - client.position.y;

      const distance = Math.sqrt(deltaX ** 2 + deltaY ** 2);

      if (distance < 3 && !cashDesk.queue.includes(client)) {
        cashDesk.queue.push(client);
        console.log(`!!Client ${client.clientID} joined ` + cashDesk.id + ' cash point queue');
        this.webSocketService.sendJoinedQueueEvent(client, cashDesk.id);
      }

      if (distance > 0) {
        const moveX = Math.abs(deltaX) <= stepSize ? deltaX : Math.sign(deltaX) * stepSize;
        const moveY = Math.abs(deltaY) <= stepSize ? deltaY : Math.sign(deltaY) * stepSize;

        if(!this.mapPositionHelper.isCellOccupied(
          {
            x: client.position.x + moveX,
            y: client.position.y + moveY
          },this.clients))
        {
          client.position.x += moveX;
          client.position.y += moveY;
        }
      }
    });
  }

  // clients moving as a queue
  moveQueue(ticketOfficeId: number) {
    const clients = this.cashDesks.find(c => c.id == ticketOfficeId)
      ?.queue;

    if(!clients)
      return;

    const previousPositions = [...this.clients];

    for (let i = 1; i < clients.length; i++) {
      const previousClient = previousPositions[i - 1];
      const currentClient = this.clients[i];
      this.moveClientById(currentClient.clientID, 0, -1);
    }

    const firstClient = this.clients[0];
    this.moveClientById(firstClient.clientID, 0, -1);
  }

  moveToPosition(id: number, newX: number, newY: number) {
    const clientIndex = this.clients.findIndex(client => client.clientID === id);
    if (clientIndex !== -1) {
      const client = this.clients[clientIndex];

      this.clients = [
        ...this.clients.slice(0, clientIndex),
        { ...client, position: {x: newX, y: newY} },
        ...this.clients.slice(clientIndex + 1)
      ];
    }
  }

  // move to position depending on offsets
  moveClientById(id: number, offsetX: number, offsetY: number) {
    const clientIndex = this.clients.findIndex(client => client.clientID === id);
    if (clientIndex !== -1) {
      const client = this.clients[clientIndex];
      const newX = client.position.x + offsetX;
      const newY = client.position.y + offsetY;

      this.clients = [
        ...this.clients.slice(0, clientIndex),
        { ...client, position: {x: newX, y: newY} },
        ...this.clients.slice(clientIndex + 1)
      ];
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
  }
}
