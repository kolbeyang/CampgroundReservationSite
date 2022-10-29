import { Component, OnInit } from '@angular/core';
import { Product } from '../Product';
import { ProductService } from '../product.service';
import { LoginService } from '../login.service';
import { User } from '../user';
import { Location } from '@angular/common';
import { Campsite } from '../Campsite';
import { ReservationService } from '../reservation.service';
import { DeclarationListEmitMode } from '@angular/compiler';
import { Observable, Subject } from 'rxjs';

@Component({
  selector: 'app-campsite-map',
  templateUrl: './campsite-map.component.html',
  styleUrls: ['./campsite-map.component.css']
})
export class CampsiteMapComponent implements OnInit {
  products$!: Observable<Campsite[]>;

  constructor(private productService: ProductService, 
    private loginService: LoginService,
    private reservationService: ReservationService,
    private location : Location) { }

  ngOnInit(): void {
    this.products$ = this.productService.searchProducts("");

  }

}
