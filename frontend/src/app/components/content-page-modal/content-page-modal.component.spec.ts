import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentPageModalComponent } from './content-page-modal.component';

describe('ContentPageModalComponent', () => {
  let component: ContentPageModalComponent;
  let fixture: ComponentFixture<ContentPageModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContentPageModalComponent]
    });
    fixture = TestBed.createComponent(ContentPageModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
