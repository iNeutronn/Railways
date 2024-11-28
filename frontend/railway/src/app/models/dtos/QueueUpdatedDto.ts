import {EventTypes} from '../enums/event-type';

export interface QueueUpdatedDto {
    type: EventTypes.QueueUpdated;
    data: {
      TicketOfficeId: number;
      Queue: number[];
    };
  }
