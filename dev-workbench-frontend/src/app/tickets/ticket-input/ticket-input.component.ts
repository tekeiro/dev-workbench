import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {EmitterService} from '../../emitter.service';
import {TraceableLinkService} from '../../traceable-link.service';
import {FollowLink} from '../../link-forwarder';

@Component({
  selector: 'app-ticket-input',
  templateUrl: './ticket-input.component.html',
  styleUrls: ['./ticket-input.component.css']
})
export class TicketInputComponent implements OnInit {
  @Input() id: string;

  constructor(private _traceableLinkService: TraceableLinkService) { }

  ngOnInit() {
  }

  onPromoteLink(codeInput: HTMLInputElement) {
    const code = codeInput.value;
    codeInput.value = '';
    this._traceableLinkService.promoteLink(code).subscribe(link => {EmitterService.get(this.id).emit(code); FollowLink(link)});
  }
}
