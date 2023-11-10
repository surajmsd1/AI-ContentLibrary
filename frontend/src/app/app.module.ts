import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule} from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ContentPageModalComponent } from './components/content-page-modal/content-page-modal.component';
import { BsModalService } from 'ngx-bootstrap/modal';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { InterviewComponent } from './components/interview/interview.component';
import { ContentPagesComponent } from './components/content-pages/content-pages.component';

const appRoutes: Routes= [
  { path: '', component: HomepageComponent},
  { path: 'interview-me', component: InterviewComponent},
  { path: 'content-pages', component: ContentPagesComponent},
]
@NgModule({
  declarations: [
    AppComponent,
    ContentPageModalComponent,
    HomepageComponent,
    InterviewComponent,
    ContentPagesComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [BsModalService],
  bootstrap: [AppComponent]
})
export class AppModule { }
