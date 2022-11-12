import { DOCUMENT } from '@angular/common';
import { outputAst } from '@angular/compiler';
import { Component, EventEmitter, Inject, Input, OnInit, Output } from '@angular/core';
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
  value: Date = new Date();

  calendarStartDate: Date;
  calendarTitle: string = "";
  errorMessage: string = "";

  reservations?: Reservation[];
  @Input() campsite?: Campsite;
  @Output() dateRange: EventEmitter<Date[]> = new EventEmitter();
  @Input() forAdmin: boolean = false;

  color = "#FF0000";

  constructor( private productService: ProductService, @Inject(DOCUMENT) private document: Document) {
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
    if (this.startDate && !this.endDate) {
      this.updateColor(this.startDate, false, true, 2);
    }
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
    let week2Date = new Date(new Date(week1Date).setDate(week1Date.getDate() + 7));
    console.log("Updating calendar with week1 of " + week1Date + " to " + week2Date);

    for (let index = 0; index < 7; index++) {
      this.week1[index] = new Date(week1Date);
      this.week2[index] = new Date(week2Date);
      week1Date = new Date(week1Date.setDate(week1Date.getDate() + 1));
      week2Date = new Date(week2Date.setDate(week2Date.getDate() + 1));
    }

    let firstDate = week1Date;
    let lastDate = this.week2[6];

    this.calendarTitle = this.months[this.calendarStartDate.getMonth()] + " " + this.calendarStartDate.getDate() + " - " + this.months[lastDate.getMonth()] + " " + lastDate.getDate();
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

  handleCalendarClick(date: Date) {
    //if this is admin view, disregard click input
    if (this.forAdmin) return;

    let index = Math.floor((date.getTime() - this.calendarStartDate.getTime()) / (24 * 60 * 60 * 1000));

    if (this.startDate && this.endDate) {
      if (date.getTime() < (this.startDate.getTime() + this.endDate.getTime())/2) {
        this.validateStartDate(date);
      } else {
        this.validateEndDate(date);
      }
    } else if (!this.startDate && this.endDate) {
      this.validateStartDate(date);
    } else if (this.startDate && !this.endDate) {
      this.validateEndDate(date);
    } else if (!this.startDate && !this.endDate) {
      this.validateStartDate(date);
    } 
  }

  emitDateRange():void {
    if (!(this.startDate && this.endDate)) return;
    this.dateRange.emit([this.startDate, this.endDate]);
  }

  setStartDate(date?: Date): void {
    //assume date is formatted correctly and date range is valid
    console.log("setting StartDate");
    this.startDate = date;
    this.updateCalendar();
    this.emitDateRange();

    let startDateElement = this.document.querySelector("#startDate") as HTMLInputElement;
    let DateOrNull: Date | null;
    if (this.startDate) DateOrNull = this.startDate;
    else DateOrNull = null;
    if (startDateElement) {
      startDateElement.valueAsDate = DateOrNull;
    }
  }

  setEndDate(date?: Date): void {
    //assume date is formatted correctly and date range is valid
    console.log("setting EndDate");
    this.endDate = date;
    this.updateCalendar();
    this.emitDateRange();

    let endDateElement = this.document.querySelector("#endDate") as HTMLInputElement;
    let DateOrNull: Date | null;
    if (this.endDate) DateOrNull = this.endDate;
    else DateOrNull = null;
    if (endDateElement) {
      endDateElement.valueAsDate = DateOrNull;
    }
  }

  setDateRangeIfValid(date1: Date, date2: Date): boolean {
    console.log("validating date range " + date1 + " to " + date2);
    let valid: boolean = true;

    if (date1.getTime() >= date2.getTime()) {
      //start date cannot be equal or after end date
      console.log("Start date cannot be equal or after end date");
      valid = false;
    }

    if (this.reservations && valid) {

      let ResStartDate: Date;
      let ResEndDate: Date;

      for (let reservation of this.reservations) {
        ResStartDate = new Date(reservation.startDate);
        ResEndDate = new Date(reservation.endDate);


        if (ResStartDate.getTime() === date1.getTime()) {
          valid = false;
          break;
        } else if (ResEndDate.getTime() === date2.getTime()) {
          valid = false;
          break;
        } else if (ResStartDate.getTime() < date1.getTime() && date1.getTime() < ResEndDate.getTime()) {
          valid = false;
          break;
        } else if (ResStartDate.getTime() < date2.getTime() && date2.getTime() < ResEndDate.getTime()) {
          valid = false;
          break;
        } else if (date1.getTime() < ResStartDate.getTime() && ResStartDate.getTime() < date2.getTime()) {
          valid = false;
          break;
        }
      }

      if (!valid) {
        console.log("CONFLICTING with a reservation");
      }

    }

    if (valid) {
      this.setStartDate(date1);
      this.setEndDate(date2);
      this.errorMessage = "";
    } else {
      this.errorMessage = "Invalid date range";
    }
    return valid;;
  }

  //checks input start date and updates if valid
  validateStartDate(date: Date): boolean {
    console.log("validating startDate");
    let valid: boolean = true;

    if (this.endDate) {
      if (this.setDateRangeIfValid(date, this.endDate)) {
        //end Date is set but the date range is invalid
        valid = true;
      } else {
        valid = false;
      }
    } else {
        let nextDay = new Date(date);
        nextDay.setDate(nextDay.getDate() + 1);
        valid = this.setDateRangeIfValid(date, nextDay)

    }
    return valid;
  }

  //checks input end date and updates if valid
  validateEndDate(date: Date): boolean {
    console.log("validating endDate");
    let valid: boolean = true;

    if (this.startDate) {
      if (this.setDateRangeIfValid(this.startDate, date)) {
        //start Date is set but the date range in invalid
        valid = true;
      } else {
        valid = false;
      }
    } else {
      this.setEndDate(date);
    }
    return valid;
  }

  clearSelection(): void {
    this.setStartDate(undefined);
    this.setEndDate(undefined);
    this.updateCalendar();
    this.emitDateRange();
    this.errorMessage = "";
  }

  handleStartDate(date: string | null): void {
    console.log("Setting Start Date");
    if (date === null) {return;}
    let startDate = new Date(date.replace(/-/g, '\/'));
    this.validateStartDate(startDate);
    //if the start date is not shown, show it
    if (!(this.calendarStartDate.getTime() <= startDate.getTime() && startDate.getTime() <= this.calendarStartDate.getTime() + 14 * 24 * 60 * 60 * 1000)) {
      console.log("showwing date");
      this.showDate(startDate);
    }

    this.updateCalendar();
    this.emitDateRange();
  } 
  handleEndDate(date: string): void {
    if (date === null) {return;}
    let endDate = new Date(date.replace(/-/g, '\/'));
    this.validateEndDate(endDate);

    this.updateCalendar();
    this.emitDateRange();
  } 

}
