/*import { Component, OnInit } from '@angular/core';
import { ContentPageService } from './service/content-page.service';
import { AppState } from './interfaces/app-state';
import { Observable, startWith, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators'
import { CustomResponse } from './interfaces/CustomResponse';
import { DataState } from './enums/data-state';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  appState$: Observable<AppState<CustomResponse>> = 
  of({ dataState: DataState.LOADING_STATE });
  
  dataStateEnum = DataState;

  constructor(private contentPageService: ContentPageService) {}

  ngOnInit(): void {
    this.appState$ = this.contentPageService.ContentPages$
    .pipe(
      map(response => {
        return { dataState: DataState.LOADED_STATE, appData: response }
      }),
      //startWith({dataState: DataState.LOADING_STATE }),
      catchError((error: string) => {
        return of({ dataState: DataState.ERROR_STATE, error })
      })
    );
  }

}*/

import { Component, OnInit } from '@angular/core';
import { ContentPageService } from './service/content-page.service';
import { AppState } from './interfaces/app-state';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { CustomResponse } from './interfaces/CustomResponse';
import { DataState } from './enums/data-state';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  dataStateEnum = DataState;
  appState$: Observable<AppState<CustomResponse>> = of({ dataState: DataState.LOADING_STATE });
  searchQuery!: string; // Bound to the input field
  
  constructor(private contentPageService: ContentPageService) {}

  ngOnInit(): void {
    this.appState$ = this.contentPageService.ContentPages$
    .pipe(
      map(response => ({ dataState: DataState.LOADED_STATE, appData: response })),
      catchError((error: string) => of({ dataState: DataState.ERROR_STATE, error }))
    );
  }

  onSearch(): void {
    this.appState$ = this.contentPageService.search$(this.searchQuery)
    .pipe(
      map(response => ({ dataState: DataState.LOADED_STATE, appData: response })),
      catchError((error: string) => of({ dataState: DataState.ERROR_STATE, error }))
    );
  }

  onDelete(pageId: number): void {
    this.contentPageService.delete$(pageId).subscribe(response => {
      // Handle the response. For example, refresh the list or show a notification.
    }, error => {
      console.error(error);
      // Handle the error. Maybe show an error notification.
    });
  }

  onDetails(pageId: number): void {
    this.appState$ = this.contentPageService.ContentPage$(pageId)
    .pipe(
      map(response => ({ dataState: DataState.LOADED_STATE, appData: response })),
      catchError((error: string) => of({ dataState: DataState.ERROR_STATE, error }))
    );
  }
}

