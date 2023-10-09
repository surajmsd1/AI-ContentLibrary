import { Component, OnInit } from '@angular/core';
import { ContentPageService } from './service/content-page.service';
import { AppState } from './interfaces/app-state';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { CustomResponse } from './interfaces/CustomResponse';
import { DataState } from './enums/data-state';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ContentPageModalComponent } from './components/content-page-modal/content-page-modal.component';

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
    private modalService: BsModalService
    ) {}

  ngOnInit(): void {
    this.appState$ = this.contentPageService.ContentPages$
    .pipe(
      map(response => ({ dataState: DataState.LOADED_STATE, appData: response })),
      catchError((error: string) => of({ dataState: DataState.ERROR_STATE, error }))
    );
  }

  //open a modal and create ref for adding new contentpage to db
  onOpenAddModal(): void {
    this.bsModalRef = this.modalService.show(ContentPageModalComponent);
    console.log("OpenAddModal() Called");
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

