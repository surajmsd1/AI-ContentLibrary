import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ContentPageService } from 'src/app/service/content-page.service';

@Component({
  selector: 'app-content-page-modal',
  templateUrl: './content-page-modal.component.html',
  styleUrls: ['./content-page-modal.component.css']
})
export class ContentPageModalComponent implements OnInit{
  
  contentPageForm!: FormGroup;
  constructor(
    public modelRef: BsModalRef,
    private formbuilder: FormBuilder,
    private contentPageService: ContentPageService
  ){}

  ngOnInit(): void {
    this.contentPageForm = this.formbuilder.group ({
      prompt: ['',Validators.required],
      response: ['',Validators.required],
      modeltype: ['',Validators.required],
      reviewed: ['', Validators.required],
      rating: [5, Validators.max(5)]
    });
  }

  onSubmit(): void {
    if(this.contentPageForm.valid){
      console.log('OnSubmit() Called!');
      const newContentPage = this.contentPageForm.value;
      console.log(newContentPage);
      this.contentPageService.save$(newContentPage).subscribe(
        response => {
          console.log('Successfully saved to Database!');
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
