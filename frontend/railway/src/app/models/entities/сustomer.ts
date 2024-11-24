import {CustomerPrivilege} from '../enums/privilege-enum';
import {Ticket} from './ticket';

export interface Customer {
  id: number;
  name: string;
  status: CustomerPrivilege;
  tickets: Ticket[];
}
