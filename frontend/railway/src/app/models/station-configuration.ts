import {CustomerGenerationType} from './enums/customer-generation';

export interface StationConfiguration {
  cashDeskCount: number;
  entranceCount: number;
  serviceTime: number;
  generationType: CustomerGenerationType;
}
