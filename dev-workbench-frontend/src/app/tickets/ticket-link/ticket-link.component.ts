import {Component, Input, OnInit} from '@angular/core';
import {TraceableLink} from '../traceable-link.model';

@Component({
  selector: 'app-ticket-link',
  templateUrl: './ticket-link.component.html',
  styleUrls: ['./ticket-link.component.css']
})
export class TicketLinkComponent implements OnInit {
  @Input() ticketLink: TraceableLink;

  constructor() { }

  ngOnInit() {
  }

}
