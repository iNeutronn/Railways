import {ClientGenerationType} from './enums/client-generation';
import {Position} from './position';
import {ElementDto} from './dtos/elementDto';

export interface StationConfiguration {
  cashPointCount: number | null;
  entranceCount: number | null;

  cashpointConfigs: ElementDto[]
  entranceConfigs: ElementDto[]
  reservCashPointConfig: ElementDto

  minServiceTime: number;
  maxServiceTime: number;
  generationType: ClientGenerationType;

  maxPeopleAllowed: number;

}
