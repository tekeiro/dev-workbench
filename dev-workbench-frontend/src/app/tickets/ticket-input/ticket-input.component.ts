import {Component, Input, OnInit, ViewChild} from '@angular/core';
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
    this.queryStatus = 'Querying for code: ' + this.codeInput.nativeElement.value;
    EmitterService.get(this.id).emit(this.nativeElement.value);
  }
}
