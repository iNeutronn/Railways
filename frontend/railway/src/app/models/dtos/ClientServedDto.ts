import {EventTypes} from '../enums/event-type';

export interface ClientServedDto {
  type: EventTypes.ClientServed,
  data: {
    clientID: number,
    endTime: string,
    startTime: string,
    ticketOfficeID: number,
    ticketsBought: number
  }
}
