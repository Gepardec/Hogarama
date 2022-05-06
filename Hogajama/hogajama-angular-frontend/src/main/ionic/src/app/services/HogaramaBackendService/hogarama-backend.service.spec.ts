import { TestBed } from '@angular/core/testing';

import { HogaramaBackendService } from './hogarama-backend.service';

describe('HogaramaBackendService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: HogaramaBackendService = TestBed.get(HogaramaBackendService);
    expect(service).toBeTruthy();
  });
});
