import { Component, ChangeDetectorRef } from '@angular/core';
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
  resume: string = "Java Springboot angular Rest api";
  interviewQuestions: { question: string, answer: string, analysis: string }[]=[];
  appState$:Observable<AppState<String[]>> = of({ dataState: DataState.INITIAL_STATE });
  audioChunks: Blob[] = [];
  mediaRecorder?: MediaRecorder;
  isRecording: Boolean[] = [];
  //testmode: initializes 10 questions from array instead of fetching from gpt
  testMode:Boolean = true;

  constructor(
  private interviewService: InterviewService,
  private cdr: ChangeDetectorRef
  ){}

  postResume() {
    // Trigger LOADING_STATE immediately when the function is called
    console.log("Sent Resume! Compiling questions!");
    if(!this.testMode){
    // Perform the HTTP request and update appState$ accordingly
    this.appState$ = this.interviewService.getQuestions$(this.resume).pipe(
      map(response => {
        this.interviewQuestions = response.map(question => ({ question, answer: '',analysis: '' }));
        this.isRecording = new Array(this.interviewQuestions.length).fill(false);
        // Upon success, set the state back to LOADED_STATE
        return { dataState: DataState.LOADED_STATE};//,appData: response };
      }),
      catchError((error: string) => {
        // Handle errors and update the state to ERROR_STATE
        return of({ dataState: DataState.ERROR_STATE, error });
      }),
      startWith({dataState:DataState.LOADING_STATE}));
    }else{
      this.interviewQuestions = [{question:"Can you explain the concept of dependency injection in the Spring framework?",answer:"",analysis:""},
       {question:"How do you handle exceptions and errors in a Spring Boot application?",answer:"",analysis:""},
       {question:"Can you describe the role of Angular in building single-page applications?",answer:"",analysis:""},
       {question:"How do you implement routing in Angular applications?",answer:"",analysis:""},
       {question:"What is REST API and how does it differ from SOAP?",answer:"",analysis:""},
       {question:"Can you discuss the advantages of using Spring Boot for developing web applications?",answer:"",analysis:""},
       {question:"What is the purpose of annotations in Java Spring?",answer:"",analysis:""},
       {question:"How do you manage authentication and authorization in a Spring Boot application?",answer:"",analysis:""},
       {question:"Can you explain the concept of data binding in Angular?",answer:"",analysis:""},
       {question:"How do you integrate an Angular frontend with a Spring Boot backend to build a full-stack application?",answer:"",analysis:""}]
       this.appState$ = of({ dataState: DataState.LOADED_STATE });
      }

  }
  toggleRecording(i:number) {
    // this.interviewQuestions.map(question => (console.log(`{question:"${question.question}",answer:"${question.answer}",analysis:"${question.analysis}"},`)));
    this.isRecording[i] ? this.stopRecording(i) : this.startRecording(i);
  }

  startRecording(i:number) {
    navigator.mediaDevices.getUserMedia({ audio: true })
      .then(stream => {
        this.mediaRecorder = new MediaRecorder(stream);
        this.mediaRecorder.ondataavailable = (event) => {
          this.audioChunks.push(event.data);
        };
        this.mediaRecorder.start();
        this.isRecording[i] = true;
      })
      .catch(error => console.error('Error accessing media devices.', error));
  }

  //add index for specific question icon feedback
  stopRecording(i:number) {
    this.mediaRecorder!.stop();
    this.isRecording[i] = false;    
    this.mediaRecorder!.onstop = () => {
      const audioBlob = new Blob(this.audioChunks, { type: 'audio/wav' });
      this.audioChunks = [];
      const audioUrl = URL.createObjectURL(audioBlob);
      const audio = new Audio(audioUrl);
      audio.play();
      console.log("Fetching Transcription...");
      this.uploadBlob(audioBlob,(transcription)=>{
        console.log("This is the transcription: "+ transcription);
        this.interviewQuestions[i].answer+=transcription;
        this.cdr.detectChanges();
        console.log(this.interviewQuestions[i].answer);
      });
    };
  }

  uploadBlob(blob:Blob, callback: (transcription:string)=> void){
    this.interviewService.getText$(blob).subscribe({next: (response)=>{
      const parsedResponse = JSON.parse(response);
      callback(parsedResponse.text);
    },
    error: (error) => {
      console.error('Error fetching Transcription:', error);
    }})
  }


  getAnalysis(index: number) {
    const request: AnalysisRequest = {
      question:this.interviewQuestions[index].question,
      answer:this.interviewQuestions[index].answer
    };

    console.log(`Getting Analysis for question ${index+1}\n`+`
                Question: ${request.question}.\n`+`
                Answer: ${request.answer}.`);
    
    if (request.question && request.answer) {
      this.interviewService.getAnalysis$(request)
        .subscribe({
          next: (response) => {
            this.interviewQuestions[index].analysis = response;
            console.log(`Feedback for question ${index+1}.\n`+`
                Question: ${this.interviewQuestions[index].question}.\n`+`
                Answer: ${this.interviewQuestions[index].answer}.\n`+`
                Analysis: ${this.interviewQuestions[index].analysis}.`);
          },
          error: (error) => {
            console.error('Error fetching analysis:', error);
          }
        });
    } else {
      // Handle the case where the index is out of bounds or questionEntry is undefined
      console.error('Request entry not correct at index:', index+1);
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

