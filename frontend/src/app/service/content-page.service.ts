import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { ContentPage } from '../interfaces/ContentPage';
import { CustomResponse } from '../interfaces/CustomResponse';


@Injectable({  providedIn: 'root' })
export class ContentPageService {
  
  private apiUrl = "localhost:8080/content-pages";

  constructor(private http: HttpClient) {}

  ContentPages$ = <Observable<CustomResponse>> 
  this.http.get(`${this.apiUrl}/`)
  .pipe(
    tap(console.log),
    catchError(this.handleError)
  );

  ContentPage$ = (pageId: number) => <Observable<CustomResponse>> 
  this.http.get(`${this.apiUrl}/${pageId}`)
  .pipe(
    tap(console.log),
    catchError(this.handleError)
  );
  
  save$ = (ContentPage: ContentPage) => <Observable<CustomResponse>>
  this.http.post(`${this.apiUrl}/save`, ContentPage)
  .pipe(
    tap(console.log),
    catchError(this.handleError)
  );

  delete$ = (pageId: number) => <Observable<CustomResponse>>
  this.http.delete(`${this.apiUrl}/delete/${pageId}`)
  .pipe(
    tap(console.log),
    catchError(this.handleError)
  );
  
  search$ = (query: string) => <Observable<CustomResponse>>
  this.http.get(`${this.apiUrl}/search?query=${query}`)
  .pipe(
    tap(console.log),
    catchError(this.handleError)
  );


  handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    return throwError(() => new Error(`An Error Occured - Error code: ${error.status}`));
  }

}
