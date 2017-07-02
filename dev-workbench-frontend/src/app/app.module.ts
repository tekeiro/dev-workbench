import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { TicketPanelComponent } from './tickets/tickets.component';
import { TicketLinkComponent } from './tickets/ticket-link/ticket-link.component';
import { TicketInputComponent } from './tickets/ticket-input/ticket-input.component';
import { TicketListComponent } from './tickets/ticket-list/ticket-list.component';
import { HeaderComponent } from './header/header.component';
import {TraceableLinkService} from './traceable-link.service';

@NgModule({
  declarations: [
    AppComponent,
    TicketPanelComponent,
    TicketLinkComponent,
    TicketInputComponent,
    TicketListComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [TraceableLinkService],
  bootstrap: [AppComponent]
})
export class AppModule { }
