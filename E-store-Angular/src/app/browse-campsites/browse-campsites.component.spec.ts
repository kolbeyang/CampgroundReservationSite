import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowseCampsitesComponent } from './browse-campsites.component';

describe('BrowseCampsitesComponent', () => {
  let component: BrowseCampsitesComponent;
  let fixture: ComponentFixture<BrowseCampsitesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BrowseCampsitesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BrowseCampsitesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
