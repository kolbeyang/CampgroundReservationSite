import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login.service';
import { ProductService } from '../product.service';
import { ReservationService } from '../reservation.service';
import { Reservation} from '../Reservation';
import { Campsite } from '../Campsite';

@Component({
  selector: 'app-view-cart',
  templateUrl: './view-cart.component.html',
  styleUrls: ['../app.component.css','./view-cart.component.css']
})
export class ViewCartComponent implements OnInit {


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
    this.loginService.getUnPaidReservations(this.loginService.getUserName())
      .subscribe(reservations => {this.reservations = reservations; this.getCampsites();});
  }

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

  calculateTotalPrice() {
    let output = 0;
    
    for (var reservation of this.reservations) {
      output += reservation.price;
    } 

    return output;
  }

  isLoggedIn(): boolean{
    return this.loginService.isLoggedIn();
  }
  
  isAdmin(): boolean{
    console.log(this.loginService.adminLoggedIn());
    return this.loginService.adminLoggedIn();
  }

  purchase(): void {
    this.loginService.purchaseCart(this.loginService.getUserName()).subscribe(response => this.reservations = []);
  }

  deleteReservation(reservation: Reservation):void {
    this.reservationService.deleteReservation(reservation.id).subscribe(() => this.getReservations());
  }

}
