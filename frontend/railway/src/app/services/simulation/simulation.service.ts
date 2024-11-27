import { Injectable } from '@angular/core';
import {environmentDev} from '../../environments/environment.development';
import {HttpClient} from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SimulationService {

  simulationUrl: string = environmentDev.serverApi + '/api/simulation';

  constructor(private httpClient: HttpClient) { }

  startSimulation() {
    const url = this.simulationUrl + '/start';
    return this.httpClient.get(url, { responseType: 'text' }).pipe(
      map(response => {
        console.log('Simulation started:', response); 
        return response; 
      }),
      catchError(error => {
        console.error('Error starting simulation:', error); 
        return throwError(() => new Error('Failed to start simulation'));
      })
    );
  }
  pauseSimulation() {
    const url = this.simulationUrl + '/stop';
    return this.httpClient.get(url, { responseType: 'text' }).pipe(
      map(response => {
        console.log('Simulation paused:', response); 
        return response; 
      }),
      catchError(error => {
        console.error('Error pausing simulation:', error); 
        return throwError(() => new Error('Failed to pause simulation'));
      })
    );
  }
  
  resumeSimulation() {
    const url = this.simulationUrl + '/resume';
    return this.httpClient.get(url, { responseType: 'text' }).pipe(
      map(response => {
        console.log('Simulation resumed:', response); 
        return response; 
      }),
      catchError(error => {
        console.error('Error resuming simulation:', error); 
        return throwError(() => new Error('Failed to resume simulation'));
      })
    );
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
