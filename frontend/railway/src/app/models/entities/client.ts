import {PrivilegeEnum} from '../enums/privilege-enum';
import {Ticket} from './ticket';

export interface Client {
  clientID: number;
  firstName: string;
  lastName: string;
  ticketsToBuy: number;
  privilege: PrivilegeEnum;
  tickets: Ticket[];
}
