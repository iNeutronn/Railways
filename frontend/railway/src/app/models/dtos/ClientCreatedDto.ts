import {EventTypes} from '../enums/event-type';
import {Client} from '../entities/client';

export interface ClientCreatedDto {
  type: EventTypes,
  data: {
    client: Client,
    entranceId: number,
    ticketOfficeId: number,
    time: string
  },
}
