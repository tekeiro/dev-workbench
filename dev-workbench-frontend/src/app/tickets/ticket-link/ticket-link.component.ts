import {Component, Input, OnInit} from '@angular/core';
import {TraceableLink} from '../traceable-link.model';
import {TraceableLinkService} from '../../traceable-link.service';
import {FollowLink} from '../../link-forwarder';
import {EmitterService} from '../../emitter.service';

@Component({
  selector: 'app-ticket-link',
  templateUrl: './ticket-link.component.html',
  styleUrls: ['./ticket-link.component.css']
})
export class TicketLinkComponent implements OnInit {
  @Input() id: string;
  @Input() ticketLink: TraceableLink;

  constructor(private _traceableLinkService: TraceableLinkService) { }

  ngOnInit() {
  }

  onLinkClicked() {
    const code = this.ticketLink.code;
    this._traceableLinkService.promoteLink(code).subscribe(link => {EmitterService.get(this.id).emit(code); FollowLink(link)});

  }
}
