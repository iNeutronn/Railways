
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  NgZone,
  OnInit,
  ViewChild
} from '@angular/core';
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
import {ClientServingStartedDto} from '../../models/dtos/ClientServingStartedDto';
import { QueueUpdatedDto } from '../../models/dtos/QueueUpdatedDto';

@Component({
  selector: 'app-map-page',
  standalone: true,
  imports: [
    NgForOf,
    NgClass
  ],
  templateUrl: './map-page.component.html',
  styleUrl: './map-page.component.css',
  changeDetection: ChangeDetectionStrategy.Default,
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

  closedCashDeskId: number = -1;
  isQueueUpdateInProgress: boolean = false;
  isSimulationActive: boolean = false;
  isSidebarOpen: boolean = false;
  cellSize: number = 50;

  entrances: Entrance[] = [];
  cashDesks: CashDesk[] = [];
  clients: Client[] = [];

  constructor(
    private configService: StationConfigurationService,
    private simulationService: SimulationService,
    private router: Router,
    private ngZone: NgZone,
    private cdr: ChangeDetectorRef,
    private webSocketService: WebSocketService) {
    this.mapPositionHelper = new MapPositionHelper();
  }

  toggleSidebar(): void {
    this.isSidebarOpen = !this.isSidebarOpen;
    this.ngZone.run(() => {
      // Оновлення позицій клієнтів
      this.cdr.detectChanges();
    });
  }

  ngOnInit() {
    this.webSocketService.connect();
    this.webSocketService.getCreatedClientsMessages().subscribe((message) => {
      //console.log('Received message:', message);
      this.handleClientCreatedEvent(message);
    });

    this.webSocketService.getClientServingStartedMessages().subscribe((message) => {
      //console.log('Received message:', message);
      this.handleClientServingStartedEvent(message);
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

  trackByClientId(index: number, client: Client): number {
    return client.clientID;
  }

  handleClientServingStartedEvent(clientServing: ClientServingStartedDto): void {
    const { clientId, ticketOfficeId, timeNeeded } = clientServing.data;

    // Знайти клієнта у списку клієнтів
    const client = this.clients.find(c => c.clientID === clientId);

    // Знайти касу, до якої прив'язаний клієнт
    const ticketOffice = this.cashDesks.find(c => c.id === ticketOfficeId);

    if (client && ticketOffice) {
      console.log(`Client ${clientId} started being served at ticket office ${ticketOfficeId}`);

      // Залишити клієнта на точці протягом заданого часу
      setTimeout(() => {
        console.log(`Client ${clientId} finished serving at ticket office ${ticketOfficeId}`);

        // Видалити клієнта зі списку
        this.clients = this.clients.filter(c => c.clientID !== clientId);
        ticketOffice.queue = ticketOffice.queue.filter(c => c.clientID !== clientId);
        console.log('new queue', ticketOffice.queue.map(q => q.clientID).join(', '));

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
        console.log('Created ' + client.privilege + ' client with id ' + client.clientID + ', assigned to ' + clientCreated.data.ticketOfficeId);
        this.clients = [...this.clients, client];

        this.moveClientToPoint(client.clientID, cashPoint);
      }
    }
  }

  handleQueueUpdatedEvent(queueUpdated: QueueUpdatedDto) {
    console.log(queueUpdated.data.Queue);
    const cashDesk = this.cashDesks.find(c => c.id === queueUpdated.data.TicketOfficeId);
    if (cashDesk) {
      cashDesk.isQueueUpdating = true;

      const firstClient = cashDesk.queue[0];
      const remainingClients = cashDesk.queue.slice(1);

      //console.log('queue before', cashDesk.queue.map(c => c.clientID).join(", "));
      //console.log('queue position before', cashDesk.queue.map(c => c.position.y).join(", "));

      // Зберігаємо попередні позиції
      const previousPositions = cashDesk.queue.map(c => ({ ...c.position }));

      const sortedQueue = queueUpdated.data.Queue
        .map(id => remainingClients.find(client => client.clientID == id))
        .filter(client => client !== undefined);

      // Оновлюємо позиції клієнтів
      sortedQueue.forEach((client, index) => {
        if (client) {
          const globalClient = this.clients.find(c => c.clientID === client.clientID);
          if (globalClient && index + 1 < previousPositions.length) {
            //console.log(globalClient.clientID + ' previous position', globalClient.position.x, globalClient.position.y);
            globalClient.position = { ...previousPositions[index + 1] };
            //console.log(globalClient.clientID + ' after position', globalClient.position.x, globalClient.position.y);
          }
        }
      });

      // Оновлюємо чергу, зберігаючи посилання
      cashDesk.queue = [firstClient, ...sortedQueue];
      //console.log('queue after', cashDesk.queue.map(c => c.clientID).join(", "));
      //console.log('queue position after', cashDesk.queue.map(c => c.position.y).join(", "));

      // Примусовий запуск перевірки змін
      this.cdr.detectChanges();
      setTimeout(() => {
        cashDesk.isQueueUpdating = false; // Розблокування руху
      }, 500); // Додатковий час для завершення змін
    }
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
        isQueueUpdating: false,
        queue: [],
        position: {
          x: configuration.cashpointConfigs[i].x,
          y: configuration.cashpointConfigs[i].y
        },
      })
    }

    this.closedCashDeskId = configuration.reservCashPointConfig.id;
    this.cashDesks.push({
      id: configuration.reservCashPointConfig.id,
      serviceTime: configuration.maxServiceTime,
      isActive: false,
      isReserve: true,
      isQueueUpdating: false,
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

  moveClientToPoint(clientId: number, cashDesk: CashDesk, stepSize: number = 1, intervalMs: number = 200): Subscription | null {

    const clientIndex = this.clients.findIndex(client => client.clientID === clientId);
    if (clientIndex < 0) {
      console.error(`Client with ID ${clientId} not found.`);
      return null;
    }

    const client = this.clients[clientIndex];
    let target: Position = this.mapPositionHelper.findCashServingPoint(this.mapSize, client, cashDesk);

    return interval(intervalMs).pipe(
      takeWhile(() => this.calculateDistance(client.position, target) != 0),
    ).subscribe(() => {
      target = this.mapPositionHelper.findCashServingPoint(this.mapSize, client, cashDesk);
      //console.log('target', target);
      const distance = this.calculateDistance(client.position, target);

      if (distance < 2 && !cashDesk.isQueueUpdating && !cashDesk.queue.map(c => c.clientID).includes(client.clientID)) {
        this.joinQueue(client, cashDesk);
      }

      if(!cashDesk.queue.map(c => c.clientID).includes(client.clientID) && cashDesk.queue.length > 0) {
        const distanceToQueue = this.calculateDistance(client.position, cashDesk.queue[cashDesk.queue.length - 1].position);
        //console.log('distanceToQueue', distanceToQueue);
        if(distanceToQueue < 2){
          this.joinQueue(client, cashDesk);
        }
      }

      if(!cashDesk.isQueueUpdating && cashDesk.queue.map(c => c.clientID).includes(client.clientID)){

      }
      else if (distance != 0) {
        this.moveClient(client, target, stepSize);
      }
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

  toggleCashDesk(id: number){
    if(this.closedCashDeskId == id){
    this.simulationService.cashPointOpenSimulation(id)
      .subscribe((id) => {
        this.closedCashDeskId = -1;
      });
    }
    else {
      this.simulationService.cashPointCloseSimulation(id)
        .subscribe((id) => {
          this.closedCashDeskId = id;
        });
    }
  }

  goToSettings(): void {
    this.router.navigate(['/start']);
  }

  ngOnDestroy() {
    this.webSocketService.closeConnection();
  }
}
