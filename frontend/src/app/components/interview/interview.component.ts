import { Component } from '@angular/core';
import { Observable, catchError, map, of, startWith, takeUntil } from 'rxjs';
import { InterviewService } from 'src/app/service/interview.service';
import { AppState } from 'src/app/interfaces/app-state';
import { DataState } from 'src/app/enums/data-state';
import { AnalysisRequest } from 'src/app/interfaces/analysis-request';
@Component({
  selector: 'app-interview',
  templateUrl: './interview.component.html',
  styleUrls: ['./interview.component.css']
})
export class InterviewComponent {
  dataStateEnum = DataState;
  resume!: string;
  interviewQuestions: { question: string, answer: string, analysis: string }[]=[];
  appState$:Observable<AppState<String[]>> = of({ dataState: DataState.INITIAL_STATE });

  constructor(
  private interviewService: InterviewService
  ){}

  postResume() {
    // Trigger LOADING_STATE immediately when the function is called
    console.log("Sent Resume Compiling questions!");

    // Perform the HTTP request and update appState$ accordingly
    this.appState$ = this.interviewService.getQuestions$(this.resume).pipe(
      map(response => {
        this.interviewQuestions = response.map(question => ({ question, answer: '',analysis: '' }));
        // Upon success, set the state back to LOADED_STATE
        return { dataState: DataState.LOADED_STATE};//,appData: response };
      }),
      catchError((error: string) => {
        // Handle errors and update the state to ERROR_STATE
        return of({ dataState: DataState.ERROR_STATE, error });
      }),
      startWith({dataState:DataState.LOADING_STATE}));
  }

  getAnalysis(index: number) {
    const request: AnalysisRequest = {
      question:this.interviewQuestions[index].question,
      answer:this.interviewQuestions[index].answer
    };

    console.log(`Getting Analysis for question ${index}./n 
                Question: ${request.question}./n
                Answer: ${request.answer}.`);
    
    if (request.question && request.answer) {
      this.interviewService.getAnalysis$(request)
        .subscribe({
          next: (response) => {
            this.interviewQuestions[index].analysis = response;
            console.log(`Feedback for question ${index}./n 
                Question: ${this.interviewQuestions[index].question}./n
                Answer: ${this.interviewQuestions[index].answer}./n
                Analysis: ${this.interviewQuestions[index].analysis}.`);
          },
          error: (error) => {
            console.error('Error fetching analysis:', error);
          }
        });
    } else {
      // Handle the case where the index is out of bounds or questionEntry is undefined
      console.error('Request entry not correct at index:', index);
    }
  }

  getAllAnalysis(){
    this.interviewQuestions.forEach((interviewQuestion, index)=>{
      if(interviewQuestion.analysis==="" && interviewQuestion.answer!==""){
        this.getAnalysis(index);
      }
    });
  }
}

