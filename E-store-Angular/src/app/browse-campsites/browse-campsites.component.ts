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

import {
  debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';
import { Reservation } from '../Reservation';

@Component({
  selector: 'app-browse-campsites',
  templateUrl: './browse-campsites.component.html',
  styleUrls: ['./browse-campsites.component.css']
})
export class BrowseCampsitesComponent implements OnInit {

  products$!: Observable<Campsite[]>;
  private products: Campsite[] = [];
  private searchTerms = new Subject<string>();
  selectedProduct?:Product;
  errorMessage = '';

  constructor(
    private productService: ProductService, 
    private loginService: LoginService,
    private reservationService: ReservationService,
    private location : Location) {
  }
  
  ngOnInit(): void {

    this.products$ = this.productService.searchProducts("");
    this.products$.subscribe(products=> this.products = products);
  }

  onSelect(product: Product): void {
    this.selectedProduct = product;
  }

  /*
  getProducts(): void{
    this.productService.searchProducts("").subscribe(products => this.products$ = products);
  }
  */

  // Push a search term into the observable stream.
  search(term: string): void {
    this.products$ = this.productService.searchProducts(term.toLowerCase());
    this.products$.subscribe(products=> this.products = products);
  }

  editProduct(): void{
    this.errorMessage = '';
    let ratenume = new Number((this.selectedProduct?.rate))
    console.log('Selected product rate' +this.selectedProduct?.rate);
    console.log(this.selectedProduct?.rate.valueOf());
    // if(console.log(this.selectedProduct?.rate instanceof String)) {

    // }
    if(this.selectedProduct?.name.trim() === '' || !this.selectedProduct?.name.toLowerCase().includes('campsite')){
      this.errorMessage = 'Invalid Campsite Name, Try Again';
      console.log("Error message was written");
    }
    else if (!ratenume.valueOf() || ratenume <= 1 || ratenume > 1000000){
      this.errorMessage = 'Invalid Rate, Try again';
    }else if(this.selectedProduct) {
        this.errorMessage = '';
        this.productService.updateProduct(this.selectedProduct)
        .subscribe();
      }
    
  }

  deleteProduct(product: Product):void{
    //this.products = this.products.filter(h => h !== product);
    this.productService.deleteProduct(product.id).subscribe(() => this.search(""));
    alert("Campsite Successfully Deleted");
}

  onSelectAdmin(product:Product): void {

    if(this.isAdmin()){
      this.selectedProduct = product;
    }
  }

  handleCreateCampsiteError(error: any) {
    console.log("Error caught in component " + error.status);
    if (error.status == 409) {
      this.errorMessage = 'Campsite name already taken';
    }
  }

  createProduct(name: string, rate: string, x: string, y: string):void{
    const DUMMYNUMBER = 23;
    let ratenume = new Number(rate);
    let xcoord = new Number(x);
    let ycoord = new Number(y);
    //console.log("X: " + xcoord.valueOf() + " Y:"  + ycoord.valueOf());
    let campsite = new Campsite(name, DUMMYNUMBER, ratenume.valueOf(), xcoord.valueOf(), ycoord.valueOf()); 
    console.log("Campsite x: " + campsite.x + "y: " + campsite.y);
    console.log(campsite.x.valueOf());

    //console.log("Rate nume Value: " + ratenume);
    //console.log('Rate nume: type' + typeof(ratenume));
    //console.log(ratenume === NaN);
    //console.log("rate nume " + ratenume.valueOf());
    if(name.trim() === '' || !name.toLowerCase().includes('campsite')){
      this.errorMessage = 'Invalid Campsite Name, please include the word campesite in the name and try again';
      console.log("Error message was written");
    } 
    // else if (this.products.filter((product: Campsite) => product.name===name).length > 0) {
    //   this.errorMessage = 'Duplicate Campsite Name';
    // }
    else if(!ratenume.valueOf() || ratenume <= 1 || ratenume > 1000000){
      this.errorMessage = 'Invalid Rate Value, Try Again';
    }
    else if(isNaN(campsite.x) || isNaN(campsite.y)){
      this.errorMessage = 'Invalid Campsite Location';
    }
    else {
      this.errorMessage = '';
      this.productService.addProduct(campsite).subscribe({
        next: (campsite) => this.search(""),
        error: (e) => this.handleCreateCampsiteError(e)
        // (campstie)=>{this.search("")},(error)=>{this.handleCreateCampsiteError(error)}
      });
      
    }

  }


  getPossiblex(): string{
    let campsite = this.productService.getPossibleCampsite();
    let xString = new String(campsite.x);
      return xString.toString();
  }

  getPossibley(): string{
    let campsite = this.productService.getPossibleCampsite();
    let yString = new String(campsite.y);
      return yString.toString();
  }



  isLoggedIn(): boolean{
    return this.loginService.isLoggedIn();
  }
  
  isAdmin(): boolean{
    return this.loginService.adminLoggedIn();
  }

}
