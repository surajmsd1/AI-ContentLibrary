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
  resume: string = ""; // Java Springboot angular Rest api
  difficulty: string = 'intermediate';
  interviewQuestions: { question: string, answer: string, analysis: string }[]=[];
  overallAnalysis: string = '';
  appState$:Observable<AppState<String[]>> = of({ dataState: DataState.INITIAL_STATE });
  audioChunks: Blob[] = [];
  mediaRecorder?: MediaRecorder;
  isRecording: Boolean[] = []; //keeps tracking of which question is being answered with mic
  //testmode: initializes 10 questions from array instead of fetching from gpt
  testMode: Boolean = false;
  topics: { name: string; src: string; short: string }[] = [
    { name: "Java",
      src: "assets/icons/java/java.svg",
      short: "A versatile language for cross-platform applications."
    },
    { name: "React",
      src: "assets/icons/reactjs/reactjs.svg",
      short: "A library for building dynamic web user interfaces."
    },
    { name: "SQL",
      src: "assets/icons/mysql/mysql.svg",
      short: "Language for managing and querying relational databases."
    },
    { name: "HTML",
      src: "assets/icons/html/html.svg",
      short: "The standard markup language for web pages."
    },
    { name: "CSS",
      src: "assets/icons/css/css.svg",
      short: "Stylesheet language for designing web page layouts."
    },
    { name: "Angular",
      src: "assets/icons/angular/angular.svg",
      short: "A framework for building scalable web applications."
    },
    { name: "AWS",
      src: "assets/icons/aws/aws.svg",
      short: "A comprehensive cloud platform for diverse services."
    },
    { name: "REST Api",
      src: "assets/icons/rest-api/rest-api.svg",
      short: "Architectural style for networked applications."
    },
    { name: "Springboot",
      src: "assets/icons/spring/spring.svg",
      short: "A framework for easy Java-based applications development."
    },
    { name: "Python",
      src: "assets/icons/python/python.svg",
      short: "A language known for simplicity and versatility."
    },
    // { name: "Microserives",
    //   src: "assets/icons/microservices/Microservices.png",
    //   short: "An Architectural style for large projects."
    // }
  ];
  timer: any;
  elapsedTime: string = "00:00:00";

  constructor(
  private interviewService: InterviewService,
  private cdr: ChangeDetectorRef
  ){}

  clear(i:number) {
    this.interviewQuestions[i].answer = '';
    this.interviewQuestions[i].analysis = '';
    this.cdr.detectChanges();
  }

  postResume() {
    // Trigger LOADING_STATE immediately when the function is called
    console.log("Sent Resume! Compiling questions!");
    if(!this.testMode){
    // Perform the HTTP request and update appState$ accordingly
    this.appState$ = this.interviewService.getQuestions$(this.resume, this.difficulty).pipe(
      map(response => {
        this.interviewQuestions = response.map(question => ({ question, answer: '',analysis: '' }));
        this.isRecording = new Array(this.interviewQuestions.length).fill(false);
        // Upon success, set the state back to LOADED_STATE
        this.stopTimer();
        return { dataState: DataState.LOADED_STATE};//,appData: response };
      }),
      catchError((error: string) => {
        // Handle errors and update the state to ERROR_STATE
        this.stopTimer();
        return of({ dataState: DataState.ERROR_STATE, error });
      }),
      startWith({dataState:DataState.LOADING_STATE}));
      this.startTimer();
    }else{
      this.interviewQuestions = [{question:"Can you explain the concept of dependency injection in the Spring framework?",answer:"the idea of not using new to create objects and use DI to inject objects in a singleton pattern, and have a central location to change injected dependencies.",analysis:""},
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

  startTimer(){
    console.log('timer started');
    let startTime = Date.now();
    this.timer = setInterval(()=>{
      let currentTime = Date.now();
      let timeElapsed = new Date(currentTime-startTime);
      let hours = timeElapsed.getUTCHours().toString().padStart(2,'0');
      let minutes = timeElapsed.getMinutes().toString().padStart(2,'0');
      let seconds = timeElapsed.getSeconds().toString().padStart(2,'0');
      this.elapsedTime = `${hours}:${minutes}:${seconds}`;

    },1000);
  }

  stopTimer(){
    if(this.timer){
      clearInterval(this.timer);
      this.timer= null;
      this.elapsedTime = '00:00:00';
    }
  }

  toggleRecording(i:number) {
    // this.interviewQuestions.map(question => (console.log(`{question:"${question.question}",answer:"${question.answer}",analysis:"${question.analysis}"},`)));
    // this.isRecording[i] ? this.stopRecording(i) : this.startRecording(i);
    if(this.isRecording[i]){
      this.stopRecording(i);
    }else{
      this.startRecording(i);
    }
  }

  startRecording(i:number) {
    if (this.isRecording[i]) {
      return; // Recording already in progress, don't start another one
    }
    navigator.mediaDevices.getUserMedia({ audio: true })
      .then(stream => {
        this.mediaRecorder = new MediaRecorder(stream);
        this.mediaRecorder.ondataavailable = (event) => {
          this.audioChunks.push(event.data);
        };
        this.mediaRecorder.start();
        this.isRecording[i] = true;
        this.cdr.detectChanges();
      })
      .catch(error => console.error('Error accessing media devices.', error));
  }

  //add index for specific question icon feedback
  stopRecording(i:number) {
    if (!this.isRecording[i]) {
      return; // No recording in progress, nothing to stop
    }
    this.mediaRecorder!.stop();
    this.isRecording[i] = false;    
    this.cdr.detectChanges();
    this.mediaRecorder!.onstop = () => {
      const audioBlob = new Blob(this.audioChunks, { type: 'audio/wav' });
      this.audioChunks = [];
      // const audioUrl = URL.createObjectURL(audioBlob);
      // const audio = new Audio(audioUrl);
      // audio.play();
      console.log("Fetching Transcription...");
      this.uploadBlob(audioBlob,(transcription)=>{
        console.log("This is the transcription: "+ transcription);
        this.interviewQuestions[i].answer+=transcription;
        this.cdr.detectChanges();
        this.getAnalysis(i);
      });
    };
  }

  uploadBlob(blob: Blob, callback: (transcription: string) => void){
    this.interviewService.getText$(blob).subscribe({
      next: (response) => {
        const parsedJson = JSON.parse(response); //parsed because in service response is treated as text, hacky
        // console.log(parsedJson.text);
        callback(parsedJson.text);
      },
      error: (error) => {
        console.error('Error fetching Transcription:', error);
      }
    })
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
            this.cdr.detectChanges();
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

  getOverallAnalysis(){
    this.appState$ = of({ dataState: DataState.FINISHED_STATE });
    console.log("called");
    this.interviewService.getOverallAnalysis$(this.interviewQuestions)
    .subscribe({
      next: (response) => {
        console.log(response);
        this.overallAnalysis = response;
      },
      error: (error) => {
        console.error('Error fetching analysis:', error);
      }
    })
  }

  addTopic(name: string) {
    this.resume += (' '+name+',');
  }
}

