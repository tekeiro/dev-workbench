import {EventEmitter, Injectable} from '@angular/core';
import {Category} from './category.model';

@Injectable()
export class CategoryService {
  categoriesChanged = new EventEmitter<Category[]>();

  private categories: Category[] = [
    new Category(0, 'category1', 'linkPattern', 'Category 1'),
    new Category(1, 'category2', 'linkPattern', 'Category 2')
  ]

  constructor() { }

  getCategories(): Category[] {
    return this.categories.slice();
  }

  addCategory(category: Category) {
    category.id = this.categories.length
    this.categories.push(category);
    this.categoriesChanged.emit(this.categories.slice());
  }

  deleteCategory(id: number) {
    const categoryIndex = this.categories.findIndex(category => category.id === id);
    if (categoryIndex >= 0) {
      this.categories.splice(categoryIndex, 1);
      this.categoriesChanged.emit(this.categories.slice());
    }
  }

}
