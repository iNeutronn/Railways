import {GenerationTypes} from '../enums/generation-type';
import {Client} from '../entities/client';

export interface ClientCreatedDto {
  type: GenerationTypes,
  data: {
    client: Client,
    entranceId: number,
    ticketOfficeId: number,
    time: string
  },
}
