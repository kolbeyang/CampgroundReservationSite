import { outputAst } from '@angular/compiler';
import { Component, Input, OnInit } from '@angular/core';
import { Campsite } from '../Campsite';
import { ProductService } from '../product.service';
import { Reservation } from '../Reservation';
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
  //0 is nothing, 1 is reserved, 2 is selected
  week1color: Array<Array<number>>;
  week2color: Array<Array<number>>;

  startDate?: Date;
  endDate?: Date;

  calendarStartDate: Date;
  calendarTitle: string = "";

  reservations?: Reservation[];

  @Input() campsite?: Campsite;

  color = "#FF0000";

  constructor( private productService: ProductService) {
    this.calendarStartDate = new Date();
    this.calendarStartDate.setHours(0,0,0,0);
    this.calendarStartDate = this.getPreviousSunday(this.calendarStartDate);
    console.log("New Calendar Start Date is " + this.calendarStartDate);

    this.week1 = new Array<Date>(7);
    this.week2 = new Array<Date>(7);

    this.week1color = new Array<Array<number>>(7);
    this.week2color = new Array<Array<number>>(7);
    for (var i = 0; i < 7; i++) {
      this.week1color[i] = [0,0];
      this.week2color[i] = [0,0];
    }

    this.updateCalendar();  

  }

  ngOnInit(): void {
    console.log("campsite is " + this.campsite?.id);
    if (this.campsite) {
      this.productService.getReservationsOfCampsite(this.campsite.id).subscribe(
        reservations => {
          this.reservations = reservations;
          console.log("got reservations from product service, quantity:" + reservations.length);
          for (let reservation of reservations) {
            console.log("Reservation of campsite id " + reservation.campsiteId);
          }
          this.updateColors();
        }
      );
    }
  }

  getPreviousSunday(date: Date): Date {
    let output = new Date(date.getTime() - date.getDay() * 24 * 60 * 60 * 1000 );
    output.setHours(0,0,0,0);
    return output;
  }

  showDate(date: Date): void {
    this.calendarStartDate = this.getPreviousSunday(date);
    this.updateCalendar();
  }

  updateColor(date: Date, firstHalf: boolean, secondHalf: boolean, colorIndex: number): void {
    let index = Math.floor((date.getTime() - this.calendarStartDate.getTime()) / (24 * 60 * 60 * 1000));
    //console.log("update color at index " + index + " and date " + date + " with calendarStartDate of " + this.calendarStartDate);
    if (0 <= index && index <= 6) {
      if (firstHalf) {
        this.week1color[index][0] = colorIndex;
      } if (secondHalf) {
        this.week1color[index][1] = colorIndex;
      }
    } else if (7 <= index && index <= 13) {
      //week 2
      index = index - 7;
      if (firstHalf) {
        this.week2color[index][0] = colorIndex;
      } if (secondHalf) {
        this.week2color[index][1] = colorIndex;
      }
    }
  }

  updateSelectionColors() {
    if (! (this.startDate && this.endDate)) {
      return;
    }
    if (this.startDate.getTime() > this.endDate.getTime()) return;

    let startDate = new Date(this.startDate);
    let endDate = new Date(this.endDate);

    this.updateColor(startDate, false, true, 2);
    this.updateColor(endDate, true, false, 2)

    startDate.setDate(startDate.getDate() + 1);

    while (startDate.getTime() < endDate.getTime())  {
      this.updateColor(startDate, true, true, 2);
      startDate.setDate(startDate.getDate() + 1);
    }
  }

  updateColors(): void {
    //clear colors
    for (var i = 0; i < 7; i++) {
      this.week1color[i] = [0,0];
      this.week2color[i] = [0,0];
    }


    if (!this.reservations) return;

    let startDate: Date;
    let endDate: Date;
    let calendarEndDate = new Date(this.calendarStartDate.getTime() + 13 * 24 * 60 * 60 * 1000);

    for (let reservation of this.reservations) {
      startDate = new Date(reservation.startDate);
      endDate = new Date(reservation.endDate);


      this.updateColor(startDate, false, true, 1);
      this.updateColor(endDate, true, false, 1)

      startDate.setDate(startDate.getDate() + 1);

      while (startDate.getTime() < endDate.getTime())  {
        this.updateColor(startDate, true, true, 1);
        startDate.setDate(startDate.getDate() + 1);
      }


    }

    this.updateSelectionColors();
  }

  updateCalendar(): void {
    //force dates to be in the previous day by subtracting a second
    let week1Date = this.getPreviousSunday(this.calendarStartDate);
    let week2Date = new Date(week1Date.getTime() + 7 * 24 * 60 * 60 * 1000);

    let firstDate = week1Date;
    let lastDate = new Date(week1Date.getTime() + 13 * 24 * 60 * 60 * 1000);

    for (let index = 0; index < 7; index++) {
      this.week1[index] = new Date(week1Date);
      this.week2[index] = new Date(week2Date);
      week1Date = new Date(week1Date.setDate(week1Date.getDate() + 1));
      week2Date = new Date(week2Date.setDate(week2Date.getDate() + 1));
    }

    this.calendarTitle = this.months[firstDate.getMonth()] + " " + firstDate.getDate() + " - " + this.months[lastDate.getMonth()] + " " + lastDate.getDate();
    this.updateColors();
  }

  changePreviousWeek(): void {
    //undershoot and round down to avoid Daylight Savings Issues
    this.calendarStartDate = new Date(this.calendarStartDate.getTime() - 6 * 24 * 60 * 60 * 1000);
    this.calendarStartDate = this.getPreviousSunday(this.calendarStartDate);
    console.log("New Calendar Start Date is " + this.calendarStartDate)
    this.updateCalendar();
  }

  changeNextWeek(): void {
    //overshoot and round down to avoid Daylight Savings Issues
    this.calendarStartDate = new Date(this.calendarStartDate.getTime() + 8 * 24 * 60 * 60 * 1000);
    this.calendarStartDate = this.getPreviousSunday(this.calendarStartDate);
    console.log("New Calendar Start Date is " + this.calendarStartDate)
    this.updateCalendar();
  }

  getColor(date: Date, section: number): string {

    //section 0 means top triangle, section 1 means lower triangle
    let index = Math.floor((date.getTime() - this.calendarStartDate.getTime()) / (24 * 60 * 60 * 1000));
    let colorIndex = 0;
    if (0 <= index && index <= 6) {
      colorIndex = this.week1color[index][section];
    } else if (7 <= index && index <= 13) {
      //week 2
      index = index - 7;
      colorIndex = this.week2color[index][section]
    }
    if (colorIndex === 0) {
      return "#D9D9D9";
    } else if (colorIndex === 1) {
      return "#919191";
    } else if (colorIndex === 2) {
      return "#F9CF7D";
    }

    return "#D9D9D9";


  }

  setStartDate(date: string | null): void {
    console.log("Setting Start Date");
    if (date === null) {return;}
    let newDate = new Date(date.replace(/-/g, '\/'));
    this.startDate = newDate;
    //if the start date is not shown, show it
    if (!(this.calendarStartDate.getTime() <= newDate.getTime() && newDate.getTime() <= this.calendarStartDate.getTime() + 14 * 24 * 60 * 60 * 1000)) {
      console.log("showwing date");
      this.showDate(newDate);
    }

    this.updateSelectionColors();
  } 
  setEndDate(date: string): void {
    if (date === null) {return;}
    this.endDate = new Date(date.replace(/-/g, '\/'));

    this.updateSelectionColors();
  } 

}
