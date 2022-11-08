import { TestBed } from '@angular/core/testing';

import { AppCookieServiceService } from './app-cookie-service.service';

describe('AppCookieServiceService', () => {
  let service: AppCookieServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppCookieServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
