import {ClientGenerationType} from './enums/client-generation';
import {Position} from './position';
import {ElementDto} from './dtos/elementDto';
import {MapSize} from './dtos/map-size';

export interface StationConfiguration {
  cashPointCount: number | null;
  entranceCount: number | null;

  cashpointConfigs: ElementDto[];
  entranceConfigs: ElementDto[];
  reservCashPointConfig: ElementDto;

  mapSize: {width: number, height: number};
  minServiceTime: number;
  maxServiceTime: number;
  generationType: ClientGenerationType;

  maxPeopleAllowed: number;
}
