
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
        style({ top: '{{startTop}}px' }), // Стартова позиція
        animate('1000ms ease-out', style({ top: '{{endTop}}px' })) // Плавна анімація
      ]),
      transition(':leave', [
        animate('1000ms ease-out', style({ opacity: 0 })) // Плавне зникнення
      ])
    ])
  ]
})
export class MapPageComponent implements OnInit {
  @ViewChild('stationContainer', {static: false}) stationContainer!: ElementRef;
  maxIndexX: number = 0;
  maxIndexY: number = 0;
  cellSize: number = 50;

  constructor(
    private configService: StationConfigurationService,
    private router: Router) {
  }

  ngOnInit() {

    this.calculateScaleFactor();
    this.configService.getConfiguration().subscribe(configuration => {

      console.log(configuration, 'Configuration');
      this.handleConfigurationResponse(configuration);
      console.log(this.entrances, 'Entrances');
      console.log(this.cashDesks, 'cashDesks');

      this.initializeClients();
      console.log(this.entrances)
    });
  }

  handleConfigurationResponse(configuration: StationConfiguration) {
    for (let i = 0; i < configuration.entranceConfigs.length; i++) {
      this.entrances.push({
        id: i,
        position: {
          x: configuration.entranceConfigs[i].x,
          y: configuration.entranceConfigs[i].y
        },
      })
    }

    for (let i =  0; i < configuration.cashpointConfigs.length; i++) {
      this.cashDesks.push({
        id: i,
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
      id: this.cashDesks.length + 1,
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

  validatePosition(position: Position, maxX: number, maxY: number): { x: number; y: number } {
    return {
      x: position.x > maxX ? maxX : position.x < 0 ? 0 : position.x,
      y: position.y > maxY ? maxY : position.y < 0 ? 0 : position.y,
    };
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
      { clientID: 1, position: {x: 0, y: 0}, privilege: PrivilegeEnum.DEFAULT, firstName:'', lastName:'', tickets:[], ticketsToBuy:0, },
      { clientID: 2, position: {x: 0, y: 1}, privilege: PrivilegeEnum.WITHCHILD, firstName:'', lastName:'', tickets:[], ticketsToBuy:0, },
      { clientID: 3, position: {x: 0, y: 2}, privilege: PrivilegeEnum.WARVETERAN, firstName:'', lastName:'', tickets:[], ticketsToBuy:0, },
      { clientID: 4, position: {x: 0, y: 3}, privilege: PrivilegeEnum.DISABLED, firstName:'', lastName:'', tickets:[], ticketsToBuy:0, },
      { clientID: 5, position: {x: 0, y: 4}, privilege: PrivilegeEnum.DEFAULT, firstName:'', lastName:'', tickets:[], ticketsToBuy:0, },
      { clientID: 6, position: {x: 0, y: 5}, privilege: PrivilegeEnum.WITHCHILD, firstName:'', lastName:'', tickets:[], ticketsToBuy:0, },
      { clientID: 7, position: {x: 0, y: 6}, privilege: PrivilegeEnum.WITHCHILD, firstName:'', lastName:'', tickets:[], ticketsToBuy:0, },
      { clientID: 8, position: {x: 0, y: 7}, privilege: PrivilegeEnum.DEFAULT, firstName:'', lastName:'', tickets:[], ticketsToBuy:0, },
      { clientID: 9, position: {x: 0, y: 8}, privilege: PrivilegeEnum.WARVETERAN, firstName:'', lastName:'', tickets:[], ticketsToBuy:0, },
      { clientID: 10, position: {x: 0, y: 9}, privilege: PrivilegeEnum.DISABLED, firstName:'', lastName:'', tickets:[], ticketsToBuy:0, },
    ]
  }

  getPrivilegeLabel(privilege: PrivilegeEnum): string {
    return PrivilegeEnumLabels[privilege];
  }

  entrances: Entrance[] = [];
  cashDesks: CashDesk[] = [];
  clients: Client[] = [];


  // clients moving as a queue
  moveClients() {
    const previousPositions = [...this.clients];

    for (let i = 1; i < this.clients.length; i++) {
      const previousClient = previousPositions[i - 1];
      const currentClient = this.clients[i];

      this.moveToPosition(currentClient.clientID, previousClient.position.x, previousClient.position.y);
    }

    const firstClient = this.clients[0];
    this.moveToPosition(firstClient.clientID, firstClient.position.x + 1, firstClient.position.y);
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
}
