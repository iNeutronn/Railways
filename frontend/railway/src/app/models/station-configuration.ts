import {ClientGenerationType} from './enums/client-generation';
import {Position} from './position';

export interface StationConfiguration {
  cashPointCount: number | null;
  entranceCount: number | null;

  cashpointConfigs: Position[]
  entranceConfigs: Position[]
  reservCashPointConfig: Position;

  minServiceTime: number;
  maxServiceTime: number;
  generationType: ClientGenerationType;

  maxPeopleAllowed: number;

}
