import {CustomerPrivilege} from '../enums/customer-privilege';
import {Ticket} from './ticket';

export interface Customer {
  id: number;
  name: string;
  status: CustomerPrivilege;
  tickets: Ticket[];
}
