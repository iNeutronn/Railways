import {Customer} from './сustomer';
import {Position} from '../position';

export interface CashDesk {
  id: number;
  position: Position;
  isActive: boolean;
  isReserve: boolean;
  queue: Customer[];
  serviceTime: number;
}
