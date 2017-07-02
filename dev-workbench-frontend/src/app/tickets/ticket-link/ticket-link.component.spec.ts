import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketLinkComponent } from './ticket-link.component';

describe('TicketLinkComponent', () => {
  let component: TicketLinkComponent;
  let fixture: ComponentFixture<TicketLinkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TicketLinkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TicketLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
