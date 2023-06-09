import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Campsite } from '../Campsite';
import { Product } from '../Product';
import { ProductService } from '../product.service';
import { Reservation } from '../Reservation';
import { ReservationService } from '../reservation.service';

@Component({
  selector: 'app-reservation-detail',
  templateUrl: './reservation-detail.component.html',
  styleUrls: ['./reservation-detail.component.css']
})
export class ReservationDetailComponent implements OnInit {
  
  @Input() reservation?: Reservation;
  @Input() campsite?: Campsite;

  @Output() deleteSelf: EventEmitter<any> = new EventEmitter();

  errorMessage = '';

  constructor(
    private reservationService: ReservationService,
    private productService: ProductService,
  ) {}

  ngOnInit(): void {
    console.log("ReservationDetailComponent initialized");
    if (this.reservation) {
      this.getData(this.reservation);
    }
  }

  getData(reservation: Reservation) : void {
    this.reservation = reservation;
    console.log("Displaying the data of a reservation from " + new Date(reservation.startDate) + " to " + new Date(reservation.endDate));
    const campsiteId = reservation.campsiteId;

    if(campsiteId == -1) {
      this.errorMessage = "The campsite for this reservation is no longer available. This reservation has been deleted."; 
    }
    else {
      this.productService.getProduct(campsiteId)
        .subscribe(campsite => this.campsite = campsite);
    }
  }

  getStartDate(): Date {
    if (this.reservation)  {return new Date(this.reservation.startDate);}
    return <Date> <unknown> undefined;
  }
  getEndDate(): Date {
    if (this.reservation)  {
      return new Date(this.reservation.endDate);
    }

    return <Date> <unknown> undefined;
  }

  deleteReservation():void {
    if (this.reservation) {this.reservationService.deleteReservation(this.reservation.id).subscribe(() => this.deleteSelf.emit());}
  }

}
