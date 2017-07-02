import { Component, OnInit } from '@angular/core';
import { TraceableLink } from '../traceable-link.model';

@Component({
  selector: 'app-ticket-list',
  templateUrl: './ticket-list.component.html',
  styleUrls: ['./ticket-list.component.css']
})
export class TicketListComponent implements OnInit {

  ticketLinkList: TraceableLink[] = [
    new TraceableLink(
      'TST-1234',
      'http://test.de/tst-1234',
      'TST-1234: xxxxxxx'
    ),
    new TraceableLink(
      'TST-5678',
      'http://test.de/tst-5678',
      'TST-5678: xxxxxxx'
    )
  ];

  constructor() { }

  ngOnInit() {
  }

}
