import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { env } from 'process';
import { environmentDev } from '../../environments/environment.development';
import { StationConfiguration } from '../../models/station-configuration';
import {of} from 'rxjs';
import {urlToHttpOptions} from 'node:url';

@Injectable({
  providedIn: 'root'
})
export class StationConfigurationService {

  constructor(private httpClient: HttpClient) { }

  stationConfigurationUrl: string = environmentDev.serverApi + '/config';

    saveConfiguration(configuration: StationConfiguration) {
      return this.httpClient.post(this.stationConfigurationUrl, configuration);
    }

    getConfiguration(){
      return this.httpClient.get<StationConfiguration>(this.stationConfigurationUrl);
    }
}
