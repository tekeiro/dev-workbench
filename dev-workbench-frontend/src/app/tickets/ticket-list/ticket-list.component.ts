import {Component, Input, OnChanges, OnInit} from '@angular/core';
import { TraceableLink } from '../traceable-link.model';
import {TraceableLinkService} from '../../traceable-link.service';
import {EmitterService} from "../../emitter.service";

@Component({
  selector: 'app-ticket-list',
  templateUrl: './ticket-list.component.html',
  styleUrls: ['./ticket-list.component.css']
})
export class TicketListComponent implements OnInit, OnChanges {
  @Input() id: string;

  ticketLinkList: TraceableLink[] = [];

  constructor(private _traceableLinkService: TraceableLinkService) {
  }

  ngOnInit() {
    this.refreshLinkList();
  }

  ngOnChanges() {
    EmitterService.get(this.id).subscribe(code => this.refreshLinkList());
  }

  private refreshLinkList() {
    this._traceableLinkService.getAllByRelevance().subscribe(linkList => this.ticketLinkList = linkList);
  }

}
