import {Component, OnInit} from '@angular/core';
import {CategoryService} from '../category.service';
import {Category} from '../category.model';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css'],
  providers: [CategoryService]
})
export class CategoriesComponent implements OnInit {
  categories: Category[];

  constructor(private _categoryService: CategoryService) {
  }

  ngOnInit() {
    this.categories = this._categoryService.getCategories();
    this._categoryService.categoriesChanged
      .subscribe(
        (categories: Category[]) => {
          this.categories = categories;
        }
      )
  }

}
