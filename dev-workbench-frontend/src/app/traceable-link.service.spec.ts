import { TestBed, inject } from '@angular/core/testing';

import { TraceableLinkService } from './traceable-link.service';

describe('TraceableLinkService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TraceableLinkService]
    });
  });

  it('should be created', inject([TraceableLinkService], (service: TraceableLinkService) => {
    expect(service).toBeTruthy();
  }));
});
