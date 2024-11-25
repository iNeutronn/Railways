import {ClientGenerationType} from './enums/client-generation';
import {Position} from './position';

export interface StationConfiguration {
  cashPointCount: number | null;
  entranceCount: number | null;

  cashpointConfigs: {
    position: Position;
  }

  entranceConfigs: {
    position: Position;
  }

  minServiceTime: number | null;
  maxServiceTime: number | null;
  generationType: ClientGenerationType;

  maxPeopleAllowed: number;

}
