import { Component } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NgClass, NgForOf} from '@angular/common';
import {animate, style, transition, trigger} from '@angular/animations';

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
export class MapPageComponent {
  // map has a size 11*30
  // mocked entrances
  entrances = [
    { x: 5, y: 29, label: 'Entrance 1' },
    { x: 11, y: 15, label: 'Entrance 2' },
    { x: 11, y: 21, label: 'Entrance 3' },
  ];
  // mocked desks
  cashDesks = [
    { x: 5, y: 0, label: 'Cash Desk 1' },
    { x: 7, y: 0, label: 'Cash Desk 2' },
    { x: 11, y: 2, label: 'Cash Desk 3' },
  ]
  // mocked clients
  clients = [
    { id: 1, x: 0, y: 0, label: 'disabled' },
    { id: 2, x: 0, y: 1, label: 'disabled' },
    { id: 3, x: 0, y: 2, label: 'mother' },
    { id: 4, x: 0, y: 3, label: 'person' },
    { id: 5, x: 0, y: 4, label: 'mother' },
    { id: 6, x: 0, y: 5, label: 'mother' },
    { id: 7, x: 0, y: 6, label: 'disabled' },
    { id: 8, x: 0, y: 7, label: 'person' },
    { id: 9, x: 0, y: 8, label: 'mother' },
    { id: 10, x: 0, y: 9, label: 'mother' },
    { id: 11, x: 0, y: 10, label: 'mother' },
    { id: 12, x: 0, y: 11, label: 'disabled' },
    { id: 13, x: 0, y: 12, label: 'person' },
    { id: 14, x: 0, y: 13, label: 'person' },
    { id: 15, x: 0, y: 14, label: 'disabled' },
    { id: 16, x: 0, y: 15, label: 'mother' },
    { id: 17, x: 0, y: 16, label: 'disabled' },
    { id: 18, x: 0, y: 17, label: 'mother' },
    { id: 19, x: 0, y: 18, label: 'disabled' },
    { id: 20, x: 0, y: 19, label: 'mother' },
    { id: 21, x: 0, y: 20, label: 'disabled' },
    { id: 22, x: 0, y: 21, label: 'mother' },
    { id: 23, x: 0, y: 22, label: 'disabled' },
    { id: 24, x: 0, y: 23, label: 'person' },
    { id: 25, x: 0, y: 24, label: 'mother' },
    { id: 26, x: 0, y: 25, label: 'person' },
    { id: 27, x: 0, y: 26, label: 'disabled' },
    { id: 28, x: 0, y: 27, label: 'mother' },
    { id: 29, x: 0, y: 28, label: 'mother' },
    { id: 30, x: 0, y: 29, label: 'disabled' },
  ];

  // scale (cell size)
  scaleFactor = 50;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    let calledNumber = 0;

    const moveClient = () => {
      if (calledNumber < 11) {
        this.moveClients();
        calledNumber++;
        setTimeout(moveClient, 100);
      }
    };

    //moveClient();
  }

  // clients moving as a queue
  moveClients() {
    const previousPositions = [...this.clients];

    for (let i = 1; i < this.clients.length; i++) {
      const previousClient = previousPositions[i - 1];
      const currentClient = this.clients[i];

      this.moveToPosition(currentClient.id, previousClient.x, previousClient.y);
    }

    const firstClient = this.clients[0];
    this.moveToPosition(firstClient.id, firstClient.x + 1, firstClient.y);
  }

  // clients move to exact position
  moveToPosition(id: number, newX: number, newY: number) {
    const clientIndex = this.clients.findIndex(client => client.id === id);
    if (clientIndex !== -1) {
      const client = this.clients[clientIndex];

      this.clients = [
        ...this.clients.slice(0, clientIndex),
        { ...client, x: newX, y: newY },
        ...this.clients.slice(clientIndex + 1)
      ];
    }
  }

  // move to position depending on offsets
  moveClientById(id: number, offsetX: number, offsetY: number) {
    const clientIndex = this.clients.findIndex(client => client.id === id);
    if (clientIndex !== -1) {
      const client = this.clients[clientIndex];
      const newX = client.x + offsetX;
      const newY = client.y + offsetY;

      this.clients = [
        ...this.clients.slice(0, clientIndex),
        { ...client, x: newX, y: newY },
        ...this.clients.slice(clientIndex + 1)
      ];
    }
  }
}
