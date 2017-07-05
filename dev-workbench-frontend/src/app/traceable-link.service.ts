import {Injectable} from '@angular/core';
import {TraceableLink} from './tickets/traceable-link.model';
import {Observable} from 'rxjs/Observable';
import {Headers} from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import {Http, Response} from '@angular/http';

@Injectable()
export class TraceableLinkService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: Http) {
  }

  getAllByRelevance(): Observable<TraceableLink[]> {
    const traceableLinks$ = this.http
      .get(`${this.baseUrl}/links/traced/relevance`, {headers: this.getHeaders()})
      .map(mapTraceableLinks);
    return traceableLinks$;
  }

  promoteLink(code: string): Observable<String> {
    return this.http
      .get(`${this.baseUrl}/links/traced/code/${code}/following`, {headers: this.getHeaders()})
      .map(response => response.text())
  }

  private getHeaders() {
    // I included these headers because otherwise FireFox
    // will request text/html instead of application/json
    const headers = new Headers();
    headers.append('Accept', 'application/json');
    return headers;
  }

}

function mapTraceableLinks(response: Response): TraceableLink[] {
  // The response of the API has a results
  // property with the actual results
  return response
    .json()._embedded.traceableLinkResourceList
    .map(toTraceableLink)
}

function toTraceableLink(r: any): TraceableLink {
  const traceableLink = <TraceableLink>({
    id: extractId(r._links.self),
    code: r.code,
    uri: r.uri,
    title: r.title
  });
  return traceableLink;
}

function extractId(traceableLinkData: any) {
  const extractedId = traceableLinkData.href.replace('http://localhost:8080/api/links/traced/', '').replace('/', '');
  return parseInt(extractedId, 10);
}
