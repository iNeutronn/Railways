import {Client} from './client';
import {Position} from '../position';

export interface CashDesk {
  id: number;
  position: Position;
  isActive: boolean;
  isReserve: boolean;
  queue: Client[];
  serviceTime: number;
  isQueueUpdating: boolean;
}
