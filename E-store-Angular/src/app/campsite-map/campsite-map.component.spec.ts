import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CampsiteMapComponent } from './campsite-map.component';

describe('CampsiteMapComponent', () => {
  let component: CampsiteMapComponent;
  let fixture: ComponentFixture<CampsiteMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CampsiteMapComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CampsiteMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
