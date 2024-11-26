import { Injectable } from '@angular/core';
import {Client} from '../../models/entities/client';
import {Position} from '../../models/position';
import {interval, Subscription, takeWhile} from 'rxjs';
import {CashDesk} from '../../models/entities/cash-desk';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor() {}

  moveClientToPoint(client: Client, target: Position, stepSize: number = 1, intervalMs: number = 100): Subscription {
    return interval(intervalMs).pipe(
      takeWhile(() => {
        const distance = this.calculateDistance(client.position, target);
        return distance > 0;
      })
    ).subscribe(() => {
      this.moveClient(client, target, stepSize);
    });
  }

  // Обчислення відстані між двома позиціями
  private calculateDistance(position1: Position, position2: Position): number {
    const deltaX = position2.x - position1.x;
    const deltaY = position2.y - position1.y;
    return Math.sqrt(deltaX ** 2 + deltaY ** 2);
  }

  // Метод для переміщення клієнта в нову позицію
  private moveClient(client: Client, target: Position, stepSize: number) {
    const deltaX = target.x - client.position.x;
    const deltaY = target.y - client.position.y;

    const distance = this.calculateDistance(client.position, target);
    if (distance < 6) {
      client.position = target;
    } else {
      client.position.x += Math.sign(deltaX) * stepSize;
      client.position.y += Math.sign(deltaY) * stepSize;
    }
  }

  // Додавання клієнта до черги
  addClientToQueue(client: Client, cashDesk: CashDesk) {
    cashDesk.queue.push(client);
  }
}
