import {EventTypes} from '../enums/event-type';

export interface ClientServingStartedDto {
  type: EventTypes.ClientServingStarted,
  data: {
    clientId: number,
    ticketOfficeId: number,
    timeNeeded: number,
  }
}
