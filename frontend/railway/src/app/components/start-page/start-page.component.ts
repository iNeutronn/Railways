import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {ClientGenerationType} from '../../models/enums/client-generation';
import { StationConfigurationService } from '../../services/station-configuration/station-configuration.service';
import { StationConfiguration } from '../../models/station-configuration';
import { Router } from '@angular/router';

@Component({
  selector: 'app-start-page',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './start-page.component.html',
  styleUrls: ['./start-page.component.css']
})
export class StartPageComponent {
  stationConfiguration: StationConfiguration = {
    cashPointCount: null,
    entranceCount: null,
    cashpointConfigs: [],
    entranceConfigs: [],
    reservCashPointConfig: {id: 0, x: -1, y: -1},
    minServiceTime: 1000,
    maxServiceTime: 5000,
    maxPeopleAllowed: 50,
    generationType: ClientGenerationType.Simple,
  };

  constructor(private stationConfigurationService: StationConfigurationService, private router: Router) { }

  Submit(form: any): void {
    if (form.valid) {
      console.log('Start button clicked!');
      console.log('Station Configuration:', this.stationConfiguration);

      this.stationConfigurationService.saveConfiguration(this.stationConfiguration).subscribe({
        next: (response) => {

          this.router.navigate(['/map']);
          console.log('Configuration saved successfully:', response);
        },
        error: (error) => {
          console.error('Error saving configuration:', error);
          this.router.navigate(['/map']);
        }
      });
    } else {
      console.log('Form is invalid');
    }

  }
}
