import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Route } from '@angular/router';

@Component({
  selector: 'app-start-page',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './start-page.component.html',
  styleUrls: ['./start-page.component.css']
})
export class StartPageComponent {
  ticketOffices: number = 0;
  entrances: number = 0;
  serviceTime: number = 0;
  strategy: string = '';

  start(): void {
    console.log('Start button clicked!');
    console.log('Number of Ticket Offices:', this.ticketOffices);
    console.log('Number of Entrances:', this.entrances);
    console.log('Service Time:', this.serviceTime);
    console.log('Customer Acquisition Strategy:', this.strategy);
  }
}