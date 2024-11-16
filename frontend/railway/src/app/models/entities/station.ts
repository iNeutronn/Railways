import {CashDesk} from './cash-desk';
import {Customer} from './сustomer';
import {Entrance} from './entrance';

export interface Station {
  cashDesks: CashDesk[];
  entrances: Entrance[];
  maxCapacity: number;
  currentOccupancy: number;
  entryPoints: number;
  customers: Customer[];
}
