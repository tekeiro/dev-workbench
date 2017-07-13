import {Component, Input, OnInit} from '@angular/core';
import {Category} from '../category.model';
import {CategoryService} from '../category.service';

@Component({
  selector: 'app-category-panel',
  templateUrl: './category-panel.component.html',
  styleUrls: ['./category-panel.component.css']
})
export class CategoryPanelComponent implements OnInit {
  @Input() category: Category;

  constructor(private _categoryService: CategoryService) { }

  ngOnInit() {
  }

  onDeleteCategory() {
    this._categoryService.deleteCategory(this.category.id)
  }
}
