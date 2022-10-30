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
  errorMessage = '';
  campsite = this.productService.getPossibleCampsite();

  constructor(private productService: ProductService, 
    private loginService: LoginService,
    private reservationService: ReservationService,
    private location : Location) { }

  ngOnInit(): void {
    this.products$ = this.productService.searchProducts("");
  }

  search(term: string): void {
    this.products$ = this.productService.searchProducts(term.toLowerCase());
  }

  randomFunction(e: MouseEvent): void{
    console.log(e.offsetX);
    console.log(e.offsetY);
    this.productService.setPossibleCampsiteLocation(e.offsetX,e.offsetY);
    this.campsite = this.productService.getPossibleCampsite();


    }

    
  }


