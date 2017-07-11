import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {TicketPanelComponent} from './tickets/tickets.component';
import {TicketLinkComponent} from './tickets/ticket-link/ticket-link.component';
import {TicketInputComponent} from './tickets/ticket-input/ticket-input.component';
import {TicketListComponent} from './tickets/ticket-list/ticket-list.component';
import {HeaderComponent} from './header/header.component';
import {EmitterService} from './emitter.service';
import {CategoryPanelComponent} from './category/category-panel/category-panel.component';
import {CategoryService} from './category/category.service';
import { DevWorkbenchComponent } from './dev-workbench/dev-workbench.component';
import { ManageCategoriesPanelComponent } from './category/manage-categories-panel/manage-categories-panel.component';
import { CategoriesComponent } from './category/categories/categories.component';

@NgModule({
  declarations: [
    AppComponent,
    TicketPanelComponent,
    TicketLinkComponent,
    TicketInputComponent,
    TicketListComponent,
    HeaderComponent,
    CategoryPanelComponent,
    DevWorkbenchComponent,
    ManageCategoriesPanelComponent,
    CategoriesComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [EmitterService],
  bootstrap: [AppComponent]
})
export class AppModule { }
