import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {

  daysOfTheWeek = ["S", "M", "T", "W", "Th", "F", "Sa"];
  week1: Array<Date>;
  week2: Array<Date>;

  startDate?: Date;
  endDate?: Date;

  calendarStartDate = new Date();

  constructor() {
    this.week1 = new Array<Date>(7);
    this.week2 = new Array<Date>(7);
    this.setCalendarDatesArrays();  
  }

  ngOnInit(): void {
  }

  getPreviousSunday(date: Date): Date {
    return new Date(date.getTime() - date.getDay() * 24 * 60 * 60 * 1000 );
  }

  setCalendarDatesArrays(): void {
    let week1Date = this.getPreviousSunday(this.calendarStartDate);
    let week2Date = new Date(week1Date.getTime() + 7 * 24 * 60 * 60 * 1000);

    for (let index = 0; index < 7; index++) {
      this.week1[index] = week1Date;
      this.week2[index] = week2Date;

      week1Date = new Date(week1Date.getTime() + 24 * 60 * 60 * 1000);
      week2Date = new Date(week2Date.getTime() + 24 * 60 * 60 * 1000);
    }
  }

  changePreviousWeek(): void {
    this.calendarStartDate = new Date(this.calendarStartDate.getTime() - 7 * 24 * 60 * 60 * 1000);
    this.setCalendarDatesArrays();
  }

  changeNextWeek(): void {
    this.calendarStartDate = new Date(this.calendarStartDate.getTime() + 7 * 24 * 60 * 60 * 1000);
    this.setCalendarDatesArrays();
  }

  setStartDate(date: string | null): void {
    if (date === null) {return;}
    this.startDate = new Date(date);
  } 
  setEndDate(date: string): void {
    if (date === null) {return;}
    this.endDate = new Date(date);
  } 

}
