import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environmentDev } from '../../environments/environment.development';
import { StationConfiguration } from '../../models/station-configuration';
import {Observable} from 'rxjs';
import { forkJoin } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StationConfigurationService {

  constructor(private httpClient: HttpClient) { }

  stationConfigurationUrl: string = environmentDev.serverApi + '/config';

  saveConfiguration(configuration: StationConfiguration): Observable<any> {
    const entranceCount$ = this.setEntranceCount(configuration.entranceCount!);
    const cashpointCount$ = this.setCashpointCount(configuration.cashPointCount!);
    const minServeTime$ = this.setMinServeTime(configuration.minServiceTime);
    const maxServeTime$ = this.setMaxServeTime(configuration.maxServiceTime);
    const mapSize$ = this.setMapSize(configuration.mapSize.width, configuration.mapSize.height);

    return forkJoin([entranceCount$, cashpointCount$, minServeTime$, maxServeTime$, mapSize$]);
  }

    getConfiguration(){
      return this.httpClient.get<StationConfiguration>(this.stationConfigurationUrl);
    }

    private setMinServeTime(minServiceTime: number) {
      const url = this.stationConfigurationUrl + '/minServiceTime';
      return this.httpClient.post(url, null, {
        params: { minServiceTime: minServiceTime },
      });
    }

    private setMaxServeTime(maxServeTime: number) {
      const url = this.stationConfigurationUrl + '/maxServiceTime';
      return this.httpClient.post(url, null, {
        params: { maxServiceTime: maxServeTime },
      });
    }

    private setCashpointCount(cashPointCount: number) {
      const url = this.stationConfigurationUrl + '/cashpointsCount';
      return this.httpClient.post(url, null, {
        params: { cashpointsCount: cashPointCount },
      });
    }

  private setEntranceCount(entranceCount: number) {
    const url = this.stationConfigurationUrl + '/entranceCount';
    return this.httpClient.post(url, null, {
      params: { entranceCount: entranceCount },
    });
  }

  setMapSize(width: number, height: number){
    const url = this.stationConfigurationUrl + '/mapSize';
    return this.httpClient.post(url, null, {
      params: { width, height },
    })
  }
}
