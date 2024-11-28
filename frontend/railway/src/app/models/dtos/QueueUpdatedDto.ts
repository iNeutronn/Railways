import {EventTypes} from '../enums/event-type';

export interface QueueUpdatedDto {
    type: EventTypes.QueueUpdated;
    data: {
      ticketOfficeID: number;
      queue: number[];
    };
  }