import {Component, Input, OnInit} from '@angular/core';
import {TraceableLink} from '../traceable-link.model';
import {TraceableLinkService} from '../../traceable-link.service';

@Component({
  selector: 'app-ticket-link',
  templateUrl: './ticket-link.component.html',
  styleUrls: ['./ticket-link.component.css']
})
export class TicketLinkComponent implements OnInit {
  @Input() ticketLink: TraceableLink;

  constructor(private _traceableLinkService: TraceableLinkService) { }

  ngOnInit() {
  }

  onLinkClicked() {
    this._traceableLinkService.promoteLink(this.ticketLink.code).subscribe(followLink)
  }
}

function followLink(uri) {
  console.log('link to follow ' + uri);
  window.location.href = uri.toString();
}
