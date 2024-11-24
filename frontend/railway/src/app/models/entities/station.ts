import {CashDesk} from './cash-desk';
import {Client} from './client';
import {Entrance} from './entrance';

export interface Station {
  cashDesks: CashDesk[];
  entrances: Entrance[];
  maxCapacity: number;
  currentOccupancy: number;
  entryPoints: number;
  clients: Client[];
}
