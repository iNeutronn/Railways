import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ClientCreatedDto } from '../../models/dtos/ClientCreatedDto';
import {Client} from '../../models/entities/client';
import {EventTypes} from '../../models/enums/event-type';
import {ClientServingStartedDto} from '../../models/dtos/ClientServingStartedDto';
import {ClientServedDto} from '../../models/dtos/ClientServedDto';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socket!: WebSocket;
  private clientCreatedMessageSubject = new Subject<ClientCreatedDto>()
  private clientServingStartedSubject = new Subject<ClientServingStartedDto>();
  private clientServedSubject = new Subject<ClientServedDto>();

  constructor() { }

  public connect(): void {
    this.socket = new WebSocket('ws://localhost:8080/ws');

    this.socket.onopen = () => {
      console.log('WebSocket connected');
    };

    this.socket.onmessage = (event) => {
      const message = JSON.parse(event.data);
      console.log(message)

      if(message.type.toString() === EventTypes[EventTypes.ClientCreated]) {
        this.clientCreatedMessageSubject.next(message);
      }
      else if(message.type.toString() === EventTypes[EventTypes.ClientServingStarted]) {
        this.clientServingStartedSubject.next(message);
      }
      else if (message.type.toString() === EventTypes[EventTypes.ClientServed]) {
        this.clientServedSubject.next(message);
      }
    };

    this.socket.onclose = () => {
      console.log('WebSocket closed');
    };

    this.socket.onerror = (error) => {
      console.log('WebSocket error:', error);
    };
  }

  public getCreatedClientsMessages() {
    return this.clientCreatedMessageSubject.asObservable();
  }

  public getClientServingStartedMessages() {
    return this.clientServingStartedSubject.asObservable();
  }

  public getClientServedMessages() {
    return this.clientServedSubject.asObservable();
  }

  public sendMessage(message: string): void {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(message);
    }
  }

  sendJoinedQueueEvent(client: Client, cashPointId: number): void {
    const payload = {
      clientId: client.clientID,
      queueId: cashPointId
    };

    // Create the message object
    /*const message = {
      eventType: EventTypes[EventTypes.JoinedQueue],
      payload: payload
    };*/

    this.socket.send(JSON.stringify(payload));
    console.log('Sent JoinedQueue event:', payload);
  }

  public closeConnection(): void {
    if (this.socket) {
      this.socket.close();
    }
  }
}
