import { Component, OnInit } from '@angular/core';
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
}
