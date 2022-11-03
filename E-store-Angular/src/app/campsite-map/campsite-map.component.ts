import { Component, Inject, OnInit } from '@angular/core';
import { Product } from '../Product';
import { ProductService } from '../product.service';
import { LoginService } from '../login.service';
import { User } from '../user';
import { DOCUMENT, Location } from '@angular/common';
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
    private location: Location,
    @Inject(DOCUMENT) private document: Document) { }

  ngOnInit(): void {
    console.log("Campsite map initting");
    this.productService.resetPossibleCampsite();
    this.campsite = this.productService.getPossibleCampsite();
    this.products$ = this.productService.searchProducts("");
  }

  search(term: string): void {
    this.products$ = this.productService.searchProducts(term.toLowerCase());
  }

  selectCampsiteLocation(e: MouseEvent): void {

    let mapDiv = this.document.querySelector(".campsite-map");
    let mapWidth = 500;
    if (mapDiv) {
      mapWidth = (this.document.querySelector(".campsite-map") as HTMLElement).offsetWidth;
    }
    console.log("MAPWIDTH " + mapWidth);

    if (((e.target) instanceof HTMLDivElement) && this.isAdmin()) {
      console.log("Mouse event x-offset " + e.offsetX + " y-offset " + e.offsetY + " with map width of " + mapWidth);
      this.productService.setPossibleCampsiteLocation(e.offsetX * 500 / mapWidth, e.offsetY * 500 / mapWidth);
      this.campsite = this.productService.getPossibleCampsite();
    }
    this.products$ = this.productService.searchProducts("");
  }

  isAdmin(): boolean {
    return this.loginService.adminLoggedIn();
  }


}


