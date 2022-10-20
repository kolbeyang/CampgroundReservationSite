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

  reservations$!: Observable<Reservation[]>;
  //reservations:Reservation[] = [];
  adminReservations:Reservation[] = [];
  reservation?: Reservation;
  campsites:Campsite[] = [];



  constructor(private loginService: LoginService, 
    private productService: ProductService, 
    private reservationService: ReservationService) { }

  // Need to be able to access username
  ngOnInit(): void {
    this.reservations$ = this.loginService.getPaidReservations(this.loginService.getUserName());
    //this.getAdminReservations(); // will want to display by campsite maybe?
  }

  // onSelect(reservation: Reservation): void {
  //   this.selectedReservation = reservation;
  // }

  isLoggedIn(): boolean{
    return this.loginService.isLoggedIn();
  }
  
  isAdmin(): boolean{
    console.log(this.loginService.adminLoggedIn());
    return this.loginService.adminLoggedIn();
  }

  getReservations(): void{
    this.reservations$ = this.loginService.getPaidReservations(this.loginService.getUserName());;
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
      
