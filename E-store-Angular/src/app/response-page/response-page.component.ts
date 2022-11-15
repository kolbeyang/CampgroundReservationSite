import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-response-page',
  templateUrl: './response-page.component.html',
  styleUrls: ['./response-page.component.css']
})
export class ResponsePageComponent implements OnInit {

  responseType: string = "";

  constructor() { }

  ngOnInit(): void {
    this.responseType = history.state.responseType;
    console.log("The responseType is " + this.responseType);
  }

}
