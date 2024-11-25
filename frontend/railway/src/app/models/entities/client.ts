import {PrivilegeEnum} from '../enums/privilege-enum';
import {Ticket} from './ticket';
import {Position} from '../position';

export interface Client {
  clientID: number;
  position: Position;
  firstName: string;
  lastName: string;
  ticketsToBuy: number;
  privilege: PrivilegeEnum;
  tickets: Ticket[];
}
