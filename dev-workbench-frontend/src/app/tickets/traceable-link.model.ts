export class TraceableLink {
  public code: string;
  public uri: string;
  public title: string;

  constructor(code: string, uri: string, title: string) {
    this.code = code;
    this.uri = uri;
    this.title = title;
  }
}
