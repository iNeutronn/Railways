import {CustomerGenerationType} from './enums/customer-generation';

export interface StationConfiguration {
  cashDeskCount: number | null;
  entranceCount: number | null;
  serviceTime: number | null;
  generationType: CustomerGenerationType;
}
