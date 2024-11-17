import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {CustomerGenerationType} from '../../models/enums/customer-generation';

@Component({
  selector: 'app-start-page',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './start-page.component.html',
  styleUrls: ['./start-page.component.css']
})
export class StartPageComponent {
  stationConfiguration = {
    cashDeskCount: null as number | null,
    entranceCount: null as number | null,
    serviceTime: null as number | null,
    generationType: CustomerGenerationType.Simple
  };

  Submit(form: any): void {
    if (form.valid) {
      console.log('Start button clicked!');
      console.log('Number of Ticket Offices:', this.stationConfiguration.cashDeskCount);
      console.log('Number of Entrances:', this.stationConfiguration.entranceCount);
      console.log('Service Time:', this.stationConfiguration.serviceTime);
      console.log('Customer Acquisition Strategy:', this.stationConfiguration.generationType);
    } else {
      console.log('Form is invalid');
    }
  }
}
