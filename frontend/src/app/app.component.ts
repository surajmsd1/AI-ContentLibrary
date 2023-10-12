import { Component, OnInit } from '@angular/core';
import { ContentPageService } from './service/content-page.service';
import { AppState } from './interfaces/app-state';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { CustomResponse } from './interfaces/CustomResponse';
import { DataState } from './enums/data-state';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ContentPageModalComponent } from './components/content-page-modal/content-page-modal.component';
import { ContentPage } from './interfaces/ContentPage';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  dataStateEnum = DataState;
  appState$: Observable<AppState<CustomResponse>> = of({ dataState: DataState.LOADING_STATE });
  searchQuery!: string; // Bound to the input field
  bsModalRef!: BsModalRef;  
  
  constructor(
    private contentPageService: ContentPageService,
    private modalService: BsModalService,
    ) {}

  ngOnInit(): void {
    this.appState$ = this.contentPageService.ContentPages$
      .pipe(
        map(response => ({dataState: DataState.LOADED_STATE, appData: response})),
        catchError((error: string) => of({dataState: DataState.ERROR_STATE, error}))
      );
  }

  //open a modal and create ref for adding new contentpage to db
  onOpenAddModal(): void {
    this.bsModalRef = this.modalService.show(ContentPageModalComponent);
    console.log("OpenAddModal() Called");
  }

  onOpenEditModal(contentPage: ContentPage): void {
    this.bsModalRef = this.modalService.show(ContentPageModalComponent, {
      initialState: {
        editContentPage: contentPage
      }
    });
  }

  onSearch(): void {
    if(this.searchQuery){
      this.appState$ = this.contentPageService.search$(this.searchQuery)
      .pipe(
        map(response => ({ dataState: DataState.LOADED_STATE, appData: response })),
        catchError((error: string) => of({ dataState: DataState.ERROR_STATE, error }))
      );
    }else{
      this.appState$ = this.contentPageService.ContentPages$
      .pipe(
        map(response => ({dataState: DataState.LOADED_STATE, appData: response})),
        catchError((error: string) => of({dataState: DataState.ERROR_STATE, error}))
      );
    }
    this.searchQuery='';
  }

  onDelete(pageId: number): void {
    this.contentPageService.delete$(pageId)
    .pipe(
      map(response => ({dataState: DataState.LOADED_STATE, appData: response})),
      catchError((error: string) => of({dataState: DataState.ERROR_STATE, error}))
    );
    
  }

  onDetails(pageId: number): void {
    this.appState$ = this.contentPageService.ContentPage$(pageId)
    .pipe(
      map(response => ({ dataState: DataState.LOADED_STATE, appData: response })),
      catchError((error: string) => of({ dataState: DataState.ERROR_STATE, error }))
    );
  }
}

