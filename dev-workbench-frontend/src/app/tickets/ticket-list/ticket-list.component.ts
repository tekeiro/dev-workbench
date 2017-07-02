import { Component, OnInit } from '@angular/core';
import { TraceableLink } from '../traceable-link.model';
import {TraceableLinkService} from '../../traceable-link.service';

@Component({
  selector: 'app-ticket-list',
  templateUrl: './ticket-list.component.html',
  styleUrls: ['./ticket-list.component.css']
})
export class TicketListComponent implements OnInit {

  ticketLinkList: TraceableLink[] = [];

  constructor(private _traceableLinkService: TraceableLinkService) {
  }

  ngOnInit() {
    this._traceableLinkService.getAll().subscribe(linkList => this.ticketLinkList = linkList);
  }

}
