
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
  maxIndexX: number = 0;
  maxIndexY: number = 0;
  cellSize: number = 50;

  entrances: Entrance[] = [];
  cashDesks: CashDesk[] = [];
  clients: Client[] = [];

  constructor(
    private configService: StationConfigurationService,
    private router: Router,
    private webSocketService: WebSocketService) {
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

      //this.initializeClients();
      console.log(this.entrances)
    });
  }

  handleClientCreatedEvent(clientCreated: ClientCreatedDto){
    const entrancePosition = this.entrances
      .find(e => e.id == clientCreated.data.entranceId)?.position;

    if(entrancePosition) {
      const clientPosition: Position = {
        x: entrancePosition.x - 1,
        y: entrancePosition.y - 1
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

      this.cashDesks.find(c => c.id == clientCreated.data.ticketOfficeId)
        ?.queue.push(client);

      console.log('Created client with id ' + client.clientID + ', assigned to ' + clientCreated.data.ticketOfficeId);
      this.clients = [...this.clients, client];
      this.moveClients(clientCreated.data.ticketOfficeId);
    }
  }

  handleConfigurationResponse(configuration: StationConfiguration) {
    for (let i = 0; i < configuration.entranceConfigs.length; i++) {
      this.entrances.push({
        id: configuration.entranceConfigs[i].id,
        position: {
          x: configuration.entranceConfigs[i].x,
          y: configuration.entranceConfigs[i].y
        },
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

      console.log('width', containerWidth);
      console.log('height', containerHeight);

      this.maxIndexX = Math.floor(containerHeight / this.cellSize) - 1;
      this.maxIndexY = Math.floor(containerWidth / this.cellSize) - 1;
      console.log('maxIndexX', this.maxIndexX);
      console.log('maxIndexY', this.maxIndexY);
    }
  }

  initializeClients(){
    this.clients = [
      { clientID: 1, position: {x: 0, y: 0}, privilege: PrivilegeEnum.DEFAULT, firstName:'', lastName:'', ticketsToBuy:0, },
      { clientID: 2, position: {x: 0, y: 1}, privilege: PrivilegeEnum.WITHCHILD, firstName:'', lastName:'', ticketsToBuy:0, },
      { clientID: 3, position: {x: 0, y: 2}, privilege: PrivilegeEnum.WARVETERAN, firstName:'', lastName:'', ticketsToBuy:0, },
      { clientID: 4, position: {x: 0, y: 3}, privilege: PrivilegeEnum.DISABLED, firstName:'', lastName:'', ticketsToBuy:0, },
      { clientID: 5, position: {x: 0, y: 4}, privilege: PrivilegeEnum.DEFAULT, firstName:'', lastName:'', ticketsToBuy:0, },
      { clientID: 6, position: {x: 0, y: 5}, privilege: PrivilegeEnum.WITHCHILD, firstName:'', lastName:'', ticketsToBuy:0, },
      { clientID: 7, position: {x: 0, y: 6}, privilege: PrivilegeEnum.WITHCHILD, firstName:'', lastName:'', ticketsToBuy:0, },
      { clientID: 8, position: {x: 0, y: 7}, privilege: PrivilegeEnum.DEFAULT, firstName:'', lastName:'', ticketsToBuy:0, },
      { clientID: 9, position: {x: 0, y: 8}, privilege: PrivilegeEnum.WARVETERAN, firstName:'', lastName:'', ticketsToBuy:0, },
      { clientID: 10, position: {x: 0, y: 9}, privilege: PrivilegeEnum.DISABLED, firstName:'', lastName:'', ticketsToBuy:0, },
    ]
  }

  getPrivilegeLabel(privilege: PrivilegeEnum): string {
    return PrivilegeEnumLabels[privilege];
  }

  // clients moving as a queue
  moveClients(ticketOfficeId: number) {
    const clients = this.cashDesks.find(c => c.id == ticketOfficeId)
      ?.queue;

    if(!clients)
      return;

    const previousPositions = [...this.clients];

    for (let i = 1; i < clients.length; i++) {
      const previousClient = previousPositions[i - 1];
      const currentClient = this.clients[i];

      this.moveClientById(currentClient.clientID, 0, -1);

      //this.moveToPosition(currentClient.clientID, previousClient.position.x, previousClient.position.y);
    }

    const firstClient = this.clients[0];
    this.moveClientById(firstClient.clientID, 0, -1);
    //this.moveToPosition(firstClient.clientID, firstClient.position.x + 1, firstClient.position.y);
  }

  // clients move to exact position
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
  goToSettings(): void {
    this.router.navigate(['/start']);
  }
  ngOnDestroy() {
    this.webSocketService.closeConnection();
  }
}
