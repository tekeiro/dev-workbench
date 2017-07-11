export class Category {
  public id: number;
  public code: string;
  public linkPattern: string;
  public title: string;

  constructor(id: number, code: string, linkPattern: string, title: string) {
    this.id = id;
    this.code = code;
    this.linkPattern = linkPattern;
    this.title = title;
  }
}
