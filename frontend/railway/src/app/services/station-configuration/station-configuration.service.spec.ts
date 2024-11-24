import { TestBed } from '@angular/core/testing';

import { StationConfigurationService } from './station-configuration.service';

describe('StationConfigurationService', () => {
  let service: StationConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StationConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
