import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketInputComponent } from './ticket-input.component';

describe('TicketInputComponent', () => {
  let component: TicketInputComponent;
  let fixture: ComponentFixture<TicketInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TicketInputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TicketInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
