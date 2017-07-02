import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-ticket-input',
  templateUrl: './ticket-input.component.html',
  styleUrls: ['./ticket-input.component.css']
})
export class TicketInputComponent implements OnInit {
  queryStatus = '';
  code = '';

  constructor() { }

  ngOnInit() {
  }

  onPromoteLink() {
    this.queryStatus = 'Querying for code: ' + this.code;
  }

  onUpdateCode(event) {
    this.code = event.target.value;
  }

}
