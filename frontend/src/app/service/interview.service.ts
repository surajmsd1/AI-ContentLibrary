import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { CustomResponse } from '../interfaces/CustomResponse';
import { AnalysisRequest } from '../interfaces/analysis-request';

@Injectable({
  providedIn: 'root'
})
export class InterviewService {

  private interviewApiUrl = "http://localhost:8080/interview";

  constructor(private http: HttpClient) { }
  getQuestions$(resume: string): Observable<string[]> {
    return this.http.post<string[]>(`${this.interviewApiUrl}/getQuestions`, { resume })
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );
  }
  getAnalysis$(request: AnalysisRequest): Observable<string> {
    return this.http.post<string>(`${this.interviewApiUrl}/getAnalysis`, request, {
      responseType: 'text' as 'json' // This tells HttpClient to handle the response as plain text.
    })
    .pipe(
      tap(console.log),
      catchError(this.handleError)
    );
  }

  handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    return throwError(() => new Error(`An Error Occured - Error code: ${error.status}`));
  }
}
