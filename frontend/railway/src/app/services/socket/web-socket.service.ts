import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ClientCreatedDto } from '../../models/dtos/ClientCreatedDto';
import {Client} from '../../models/entities/client';
import {EventTypes} from '../../models/enums/event-type';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socket!: WebSocket;
  private clientCreatedMessageSubject = new Subject<ClientCreatedDto>();
  constructor() { }

  public connect(): void {
    this.socket = new WebSocket('ws://localhost:8080/ws');

    this.socket.onopen = () => {
      console.log('WebSocket connected');
    };

    this.socket.onmessage = (event) => {
      const message: ClientCreatedDto = JSON.parse(event.data);
      this.clientCreatedMessageSubject.next(message);
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