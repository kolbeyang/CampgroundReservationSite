import { Component, OnInit, ResolvedReflectiveFactory } from '@angular/core';
import { LoginService } from '../login.service';
import { ProductService } from '../product.service';
import { ReservationService } from '../reservation.service';
import { Reservation} from '../Reservation';
import { Observable, Subject } from 'rxjs';
import { Product } from '../Product';
import { Campsite } from '../Campsite';
import { User } from '../user';



@Component({
  selector: 'app-view-reservations',
  templateUrl: './view-reservations.component.html',
  styleUrls: ['../app.component.css','./view-reservations.component.css']
})
export class ViewReservationsComponent implements OnInit {

  constructor(private loginService: LoginService, 
    private productService: ProductService, 
    private reservationService: ReservationService) { }

  reservation?: Reservation;
  reservations: Reservation[] = [];
  campsite?: Campsite;
  campsites: Campsite[] = [];

  ngOnInit(): void {
    this.getReservations();
  }

  getCampsiteName(campsiteId: number) : string {
    for (var campsite of this.campsites) {
      if (campsite.id === campsiteId) {
        return campsite.name;
      }
    }
    return "campsite not found";
  }


  getCampsites(): void{
    this.productService.getProducts()
      .subscribe(campsites => {this.campsites = campsites;});
  }

  getReservations(): void{
    this.loginService.getPaidReservations(this.loginService.getUserName())
      .subscribe(reservations => {this.reservations = reservations; this.getCampsites();});
  }
  // onSelect(reservation: Reservation): void {
  //   this.selectedReservation = reservation;
  // }

  getUsername(): String {
    return this.loginService.getUserName();
  }

  isLoggedIn(): boolean{
    return this.loginService.isLoggedIn();
  }
  
  isAdmin(): boolean{
    console.log(this.loginService.adminLoggedIn());
    return this.loginService.adminLoggedIn();
  }


  // getAdminReservations(): void {
  //   this.reservationService.getReservations().subscribe(reservations => this.adminReservations = reservations);
  // }

  getStartDate(reservation: Reservation): Date {
    const milliseconds = reservation.startDate;
    const startDate = new Date(milliseconds);
    return startDate;
  }

  getEndDate(reservation: Reservation): Date {
    const milliseconds = reservation.endDate;
    const endDate = new Date(milliseconds);
    return endDate;
  }

  deleteReservation(reservation: Reservation):void {
    this.reservationService.deleteReservation(reservation.id).subscribe(() => this.getReservations());
  }
  
}
      
