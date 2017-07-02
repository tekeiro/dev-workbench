import {Injectable} from '@angular/core';
import {TraceableLink} from './tickets/traceable-link.model';
import {Observable} from 'rxjs/Observable';
import {Headers} from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import { Http, Response } from '@angular/http';

@Injectable()
export class TraceableLinkService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: Http) {
  }

  getAll(): Observable<TraceableLink[]> {

    const traceableLinks$ = this.http
      .get(`${this.baseUrl}/links/traced`, {headers: this.getHeaders()})
      .map(mapTraceableLinks);
    return traceableLinks$;

    /*    return [
     new TraceableLink(
     'TST-1234',
     'http://test.de/tst-1234',
     'TST-1234: xxxxxxx'
     ),
     new TraceableLink(
     'TST-5678',
     'http://test.de/tst-5678',
     'TST-5678: xxxxxxx'
     ),
     new TraceableLink(
     'TST-9012',
     'http://test.de/tst-9012',
     'TST-9012: xxxxxxx'
     )
     ];*/
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
  const responseValue = response
    .json();

    return responseValue._embedded.traceableLinkResourceList
    .map(toTraceableLink)
}

function toTraceableLink(r: any): TraceableLink {
  const traceableLink = <TraceableLink>({
    id: extractId(r._links.self),
    code: r.code,
    uri: r.uri,
    title: r.title
  });
  console.log('Parsed traceableLink:', traceableLink);
  return traceableLink;
}

function extractId(traceableLinkData: any){
  const extractedId = traceableLinkData.href.replace('http://localhost:8080/api/links/traced/', '').replace('/', '');
  return parseInt(extractedId, 10);
}
