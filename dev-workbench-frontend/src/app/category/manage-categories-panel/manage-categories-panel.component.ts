import { Component, OnInit } from '@angular/core';
import {Category} from '../category.model';
import {CategoryService} from '../category.service';

@Component({
  selector: 'app-manage-categories-panel',
  templateUrl: './manage-categories-panel.component.html',
  styleUrls: ['./manage-categories-panel.component.css']
})
export class ManageCategoriesPanelComponent implements OnInit {

  constructor(private _categoryService: CategoryService) { }

  ngOnInit() {
  }

  onAddCategory(titleInput: HTMLInputElement) {
    const title = titleInput.value;
    titleInput.value = '';
    this._categoryService.addCategory(new Category(null, 'code', 'linkPattern', title));
  }

}
