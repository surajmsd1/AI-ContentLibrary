import { Component, OnInit } from '@angular/core';
import { ContentPageService } from 'src/app/service/content-page.service';
import { AppState } from 'src/app/interfaces/app-state';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { CustomResponse } from 'src/app/interfaces/CustomResponse';
import { DataState } from 'src/app/enums/data-state';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ContentPageModalComponent } from '../content-page-modal/content-page-modal.component';
import { ContentPage } from 'src/app/interfaces/ContentPage';

@Component({
  selector: 'app-content-pages',
  templateUrl: './content-pages.component.html',
  styleUrls: ['./content-pages.component.css']
})
export class ContentPagesComponent {

  dataStateEnum = DataState;
  appState$: Observable<AppState<CustomResponse>> = of({ dataState: DataState.LOADING_STATE });
  searchQuery!: string; // Bound to the input field
  bsModalRef!: BsModalRef;

  constructor(
    private contentPageService: ContentPageService,
    private modalService: BsModalService,
    ) {}

  ngOnInit(): void {
    this.getAllPages();
  }

  //open a modal and create ref for adding new contentpage to db
  onOpenAddModal(): void {
    this.bsModalRef = this.modalService.show(ContentPageModalComponent);
    // console.log("OpenAddModal() Called");
  }

  onOpenEditModal(contentPage: ContentPage): void {
    this.bsModalRef = this.modalService.show(ContentPageModalComponent, {
      initialState: {
        editContentPage: contentPage
      }
    });

    this.bsModalRef.onHide?.subscribe(() => {
      // this.onDetails(contentPageid);
      this.getAllPages();
    })
  }

  onSearch(): void {
    if(this.searchQuery){
      this.appState$ = this.contentPageService.search$(this.searchQuery)
      .pipe(
        map(response => ({ dataState: DataState.LOADED_STATE, appData: response })),
        catchError((error: string) => of({ dataState: DataState.ERROR_STATE, error }))
      );
    }else{
      this.getAllPages();
    }
    this.searchQuery='';
  }

  onDelete(pageId: number): void {
    // console.log(pageId+" called onDelete!");
    this.appState$ = this.contentPageService.delete$(pageId)
      .pipe(
        map(response => ({dataState: DataState.LOADED_STATE, appData: response})),
        catchError((error: string) => of({dataState: DataState.ERROR_STATE, error}))
      );
    alert(`Successfully Deleted page #${pageId}!`);
    this.getAllPages();
  }

  getAllPages() {
    this.appState$ = this.contentPageService.ContentPages$
      .pipe(
        map(response => ({ dataState: DataState.LOADED_STATE, appData: response })),
        catchError((error: string) => of({ dataState: DataState.ERROR_STATE, error }))
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

