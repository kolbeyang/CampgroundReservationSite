import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Campsite } from '../Campsite';
import { Product } from '../Product';
import { ProductService } from '../product.service';
import { Reservation } from '../Reservation';
import { ReservationService } from '../reservation.service';

@Component({
  selector: 'app-hero-detail',
  templateUrl: './reservation-detail.component.html',
  styleUrls: ['./reservation-detail.component.css']
})
export class ReservationDetailComponent implements OnInit {
  
  @Input() reservation?: Reservation;
  @Input() campsite?: Campsite;

  constructor(
    private route: ActivatedRoute,
    private reservationService: ReservationService,
    private productService: ProductService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getReservation();
  }

  handleReservation(reservation: Reservation) : void {
    this.reservation = reservation;
    const campsiteId = reservation.campsiteId;
    this.productService.getProduct(campsiteId)
      .subscribe(campsite => this.campsite = campsite);
  }
  
  getReservation(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.reservationService.getReservation(id)
      .subscribe(reservation => this.handleReservation(reservation));
  }
}
