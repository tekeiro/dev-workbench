import {Component, Input, OnInit} from '@angular/core';
import {EmitterService} from '../../emitter.service';
import {TraceableLinkService} from '../../traceable-link.service';

@Component({
  selector: 'app-ticket-input',
  templateUrl: './ticket-input.component.html',
  styleUrls: ['./ticket-input.component.css']
})
export class TicketInputComponent implements OnInit {
  @Input() id: string;
  queryStatus = '';
  code = '';

  constructor(private _traceableLinkService: TraceableLinkService) { }

  ngOnInit() {
  }

  onPromoteLink() {
    this.queryStatus = 'Querying for code: ' + this.code;
    EmitterService.get(this.id).emit(this.code);
  }

  onUpdateCode(event) {
    this.code = event.target.value;
  }

}
