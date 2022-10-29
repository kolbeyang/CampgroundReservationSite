import { Component, Input, OnInit } from '@angular/core';
import { Campsite } from '../Campsite';
import { ReservationService } from '../reservation.service';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {

  daysOfTheWeek = ["S", "M", "T", "W", "Th", "F", "Sa"];
  months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
  week1: Array<Date>;
  week2: Array<Date>;

  startDate?: Date;
  endDate?: Date;

  calendarStartDate = new Date();
  calendarTitle: string = "";

  @Input() campsite?: Campsite;

  color = "#FF0000";

  constructor( reservationService: ReservationService) {
    this.week1 = new Array<Date>(7);
    this.week2 = new Array<Date>(7);
    this.updateCalendar();  
  }

  ngOnInit(): void {
  }

  getPreviousSunday(date: Date): Date {
    return new Date(date.getTime() - date.getDay() * 24 * 60 * 60 * 1000 );
  }

  showDate(date: Date): void {
    this.calendarStartDate = this.getPreviousSunday(date);
    this.updateCalendar();
  }

  updateCalendar(): void {
    let week1Date = this.getPreviousSunday(this.calendarStartDate);
    let week2Date = new Date(week1Date.getTime() + 7 * 24 * 60 * 60 * 1000);

    let firstDate = week1Date;
    let lastDate = new Date(week1Date.getTime() + 13 * 24 * 60 * 60 * 1000);

    for (let index = 0; index < 7; index++) {
      this.week1[index] = week1Date;
      this.week2[index] = week2Date;

      week1Date = new Date(week1Date.getTime() + 24 * 60 * 60 * 1000);
      week2Date = new Date(week2Date.getTime() + 24 * 60 * 60 * 1000);
    }

    this.calendarTitle = this.months[firstDate.getMonth()] + " " + firstDate.getDate() + " - " + this.months[lastDate.getMonth()] + " " + lastDate.getDate();
  }

  changePreviousWeek(): void {
    this.calendarStartDate = new Date(this.calendarStartDate.getTime() - 7 * 24 * 60 * 60 * 1000);
    this.updateCalendar();
  }

  changeNextWeek(): void {
    this.calendarStartDate = new Date(this.calendarStartDate.getTime() + 7 * 24 * 60 * 60 * 1000);
    this.updateCalendar();
  }

  getColor(date: Date): string {
    return "#FFFFFF";
  }

  setStartDate(date: string | null): void {
    console.log("Setting Start Date");
    if (date === null) {return;}
    let newDate = new Date(date);
    this.startDate = newDate;
    //if the start date is not shown, show it
    console.log("calendarStartDate " + this.calendarStartDate.getTime() + " newDate " + newDate.getTime() + " 2 weeks "  + this.calendarStartDate.getTime() + 14 * 24 * 60 * 60 * 1000)
    if (!(this.calendarStartDate.getTime() <= newDate.getTime() && newDate.getTime() <= this.calendarStartDate.getTime() + 14 * 24 * 60 * 60 * 1000)) {
      console.log("showwing date");
      this.showDate(newDate);
    }
  } 
  setEndDate(date: string): void {
    if (date === null) {return;}
    this.endDate = new Date(date);
  } 

}
