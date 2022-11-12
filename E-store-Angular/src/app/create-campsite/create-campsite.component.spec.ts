import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCampsiteComponent } from './create-campsite.component';

describe('CreateCampsiteComponent', () => {
  let component: CreateCampsiteComponent;
  let fixture: ComponentFixture<CreateCampsiteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateCampsiteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateCampsiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
