import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login.service';
import { ProductService } from '../product.service';
import { ReservationService } from '../reservation.service';
import { Reservation} from '../Reservation';
import { Observable, Subject } from 'rxjs';
import { Product } from '../Product';
import { User } from '../user';



@Component({
  selector: 'app-view-reservations',
  templateUrl: './view-reservations.component.html',
  styleUrls: ['./view-reservations.component.css']
})
export class ViewReservationsComponent implements OnInit {

  /** For when we implement owner/user able to edit/delete reservations */
  reservations$!: Observable<Reservation[]>;
  selectedReservation?:Reservation;
  
  reservation?: Reservation;


  constructor(private loginService: LoginService, 
    private productService: ProductService, 
    private reservationService: ReservationService) { }

  // Need to be able to access username
  ngOnInit(): void {
    this.reservations$ = this.loginService.getPaidReservations("spider");
  }

  onSelect(reservation: Reservation): void {
    this.selectedReservation = reservation;
  }

  isLoggedIn(): boolean{
    return this.loginService.isLoggedIn();
  }
  
  isAdmin(): boolean{
    console.log(this.loginService.adminLoggedIn());
    return this.loginService.adminLoggedIn();
  }

}
