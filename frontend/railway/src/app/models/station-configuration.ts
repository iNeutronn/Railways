import {ClientGenerationType} from './enums/client-generation';

export interface StationConfiguration {
  cashDeskCount: number | null;
  entranceCount: number | null;
  serviceTime: number | null;
  generationType: ClientGenerationType;
}
