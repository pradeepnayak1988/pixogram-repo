import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MymediaComponent } from './mymedia.component';

describe('MymediaComponent', () => {
  let component: MymediaComponent;
  let fixture: ComponentFixture<MymediaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MymediaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MymediaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
