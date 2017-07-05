import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {EmitterService} from '../../emitter.service';
import {TraceableLinkService} from '../../traceable-link.service';

@Component({
  selector: 'app-ticket-input',
  templateUrl: './ticket-input.component.html',
  styleUrls: ['./ticket-input.component.css']
})
export class TicketInputComponent implements OnInit {
  @Input() id: string;
  @ViewChild('codeInput') codeInput: ElementRef;
  queryStatus = '';

  constructor(private _traceableLinkService: TraceableLinkService) { }

  ngOnInit() {
  }

  onPromoteLink() {
    const code = this.codeInput.nativeElement.value;
    this.queryStatus = 'Querying for code: ' + code;
    EmitterService.get(this.id).emit(code);
    this._traceableLinkService.promoteLink(code).subscribe(followLink);
  }
}

function followLink(uri) {
  console.log('link to follow ' + uri);
  window.location.href = uri.toString();
}
