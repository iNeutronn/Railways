import {Position} from '../models/position';
import {Client} from '../models/entities/client';
import {MapSize} from '../models/dtos/map-size';
import {CashDesk} from '../models/entities/cash-desk';

export class MapPositionHelper {

  findFreeCellNear(clients: Client[], targetX: number, targetY: number): Position | null {
    const directions: Position[] = [
      { x: 0, y: -1 },
      { x: 0, y: 1 },
      { x: -1, y: 0 },
      { x: 1, y: 0 }
    ];

    for (const direction of directions) {
      const newX = targetX + direction.x;
      const newY = targetY + direction.y;

      if (this.isCellAvailable(clients, newX, newY)) {
        return { x: newX + 6, y: newY };
      }
    }

    return null;
  }

  isCellAvailable(clients: Client[], x: number, y: number): boolean {
    const targetPosition: Position = { x: x, y: y };
    return !clients.some(c => c.position.x === targetPosition.x && c.position.y === targetPosition.y);
  }

  findCashServingPoint(map: MapSize, cashDesk: CashDesk): Position{
    const cashDeskPosition: Position = cashDesk.position;
    const maxIndexX = map.height - 1;
    const maxIndexY = map.width - 1;

    let position: Position = {
      x: cashDeskPosition.x,
      y: cashDeskPosition.y,
    }

    console.log(cashDeskPosition, maxIndexX, maxIndexY);
    if(cashDeskPosition.x == 0) {
      position.x += cashDesk.queue.length + 1;
    }

    if(cashDeskPosition.x >= maxIndexX) {
      position.x -= cashDesk.queue.length - 1;
    }

    if(cashDeskPosition.y == 0) {
      position.y += cashDesk.queue.length + 1;
    }

    if(cashDeskPosition.y >= maxIndexY) {
      console.log('invoked')
      position.y -= (cashDesk.queue.length + 1);
    }

    return position;
  }

  isCellOccupied(point: Position, clients: Client[]): boolean {
    return clients.some(c => c.position.x === point.x && c.position.y === point.y);
  }
}
