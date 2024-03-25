import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ContentPageService } from 'src/app/service/content-page.service';
import { ContentPage } from 'src/app/interfaces/ContentPage';

@Component({
  selector: 'app-content-page-modal',
  templateUrl: './content-page-modal.component.html',
  styleUrls: ['./content-page-modal.component.css']
})
export class ContentPageModalComponent implements OnInit{
  editContentPage!: ContentPage;
  contentPageForm: FormGroup = this.initializeForm();

  constructor(
    public modelRef: BsModalRef,
    private formbuilder: FormBuilder,
    private contentPageService: ContentPageService

  ){}

  ngOnInit(): void {
    this.contentPageForm = this.initializeForm(this.editContentPage);
  }

  initializeForm(contentPage?: ContentPage): FormGroup {
    const defaultValues = {
        prompt: '',
        response: '',
        modeltype: '',
        reviewed: '',
        rating: 5
    };

    if (contentPage) {
        return this.formbuilder.group({
            ...defaultValues,
            prompt: contentPage.prompt,
            response: contentPage.response,
            modeltype: contentPage.modelType,
            reviewed: contentPage.reviewed,
            rating: contentPage.rating,
            id: contentPage.id,
            date: new Date()
        });
    } else {
        return this.formbuilder.group(defaultValues);
    }
  }

  onSubmit(): void {
    //for patch
    if(this.contentPageForm.valid && this.editContentPage){
      const patchContentPage = this.contentPageForm.value;
      console.log(patchContentPage);
      this.contentPageService.patch$(patchContentPage).subscribe(
        response => {
          // alert('Successfully updated database!');

          this.dismiss();
        },
        error => {
          console.error('Error while attempting to update database:', error);
        }
      )
    //for post
    }else if(this.contentPageForm.valid){
      console.log('OnSubmit() Called!');
      const newContentPage = this.contentPageForm.value;
      console.log(newContentPage);
      this.contentPageService.save$(newContentPage).subscribe(
        response => {
          // alert('Successfully saved to Database!');
          this.dismiss();
        },
        error => {
          console.error('Error while attempting to save to database:', error);
        }
      )
    }
  }
  dismiss(): void {
    this.modelRef.hide();
  }
}
