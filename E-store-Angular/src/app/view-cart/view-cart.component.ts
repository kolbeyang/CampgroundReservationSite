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

  private campsiteNames: Record<number,string> = {};

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

  getCampsiteNames(): void {
    this.reservations.forEach(reservation => {
        this.campsiteNames[reservation.id] = this.getCampsiteName(reservation.campsiteId);
      }  
    )
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
      .subscribe(campsites => {this.campsites = campsites; this.getCampsiteNames();});
  }

  getReservations(): void{
    this.loginService.getUnPaidReservations(this.loginService.getUserName())
      .subscribe(reservations => {this.reservations = reservations; this.getCampsites();});
  }

  isLoggedIn(): boolean{
    return this.loginService.isLoggedIn();
  }
  
  isAdmin(): boolean{
    console.log(this.loginService.adminLoggedIn());
    return this.loginService.adminLoggedIn();
  }

}
