import { Injectable } from '@angular/core';
import {environmentDev} from '../../environments/environment.development';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SimulationService {

  simulationUrl: string = environmentDev.serverApi + '/simulation';

  constructor(private httpClient: HttpClient) { }

  startSimulation() {
    const url = this.simulationUrl + '/start';
    return this.httpClient.get(url);
  }

  pauseSimulation() {
    const url = this.simulationUrl + '/pause';
    return this.httpClient.get(url);
  }

  resumeSimulation() {
    const url: string = environmentDev.serverApi + '/resume';
    return this.httpClient.get(url);
  }

  cashPointCloseSimulation(id: number) {
    const url = this.simulationUrl + '/cashpoint/close';
    return this.httpClient.post<number>(url, null, {
      params: { id: id },
    });
  }

  cashPointOpenSimulation(id: number) {
    const url = this.simulationUrl + '/cashpoint/open';
    return this.httpClient.post<number>(url, null, {
      params: { id: id },
    });
  }
}
