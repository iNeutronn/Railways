import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {CustomerGenerationType} from '../../models/enums/customer-generation';
import { StationConfigurationService } from '../../services/station-configuration.service';
import { StationConfiguration } from '../../models/station-configuration';

@Component({
  selector: 'app-start-page',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './start-page.component.html',
  styleUrls: ['./start-page.component.css']
})
export class StartPageComponent {
  stationConfiguration : StationConfiguration = {
    cashDeskCount: null,
    entranceCount: null,
    serviceTime: null,
    generationType: CustomerGenerationType.Simple
  };

  constructor(private stationConfigurationService: StationConfigurationService) { }

  Submit(form: any): void {
    if (form.valid) {
      console.log('Start button clicked!');
      console.log('Station Configuration:', this.stationConfiguration);

      this.stationConfigurationService.saveConfiguration(this.stationConfiguration).subscribe({
        next: (response) => {
          console.log('Configuration saved successfully:', response);
        },
        error: (error) => {
          console.error('Error saving configuration:', error);
        }
      });
    } else {
      console.log('Form is invalid');
    }
  
  }
}
