import { TestBed } from '@angular/core/testing';

import { HogaramaBackendService } from './hogarama-backend.service';
import {HttpClientModule} from '@angular/common/http';

describe('HogaramaBackendService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientModule]
  }));

  it('should be created', () => {
    const service: HogaramaBackendService = TestBed.get(HogaramaBackendService);
    expect(service).toBeTruthy();
  });
});
