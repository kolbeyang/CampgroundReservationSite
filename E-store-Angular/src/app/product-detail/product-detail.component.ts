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
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['../app.component.css','./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {

  products$!: Observable<Campsite[]>;
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
}

  onSelectAdmin(product:Product): void {

    if(this.isAdmin()){
      this.selectedProduct = product;
    }
  }

  createProduct(name: string, rate: string):void{
    const DUMMYNUMBER = 23;
    let ratenume = new Number(rate);
    let campsite = new Campsite(name, DUMMYNUMBER, ratenume.valueOf()); 
    console.log("Rate nume Value: " + ratenume);
    console.log('Rate nume: type' + typeof(ratenume));
    console.log(ratenume === NaN);
    console.log("rate nume " + ratenume.valueOf());
    if(name.trim() === '' || !name.toLowerCase().includes('campsite')){
      this.errorMessage = 'Invalid Campsite Name, Try Again';
      console.log("Error message was written");
    }
    else if(!ratenume.valueOf() || ratenume <= 1 || ratenume > 1000000){
      this.errorMessage = 'Invalid Rate Value, Try Again';
    }
    else {
      this.errorMessage = '';
      this.productService.addProduct(campsite).subscribe(campsite => {
        this.search("")});
    }

  }

  createReservation(start: string, end: string, site: Product):void{
    console.log("Start value" + start);
    console.log("End Value" + end);

    let startDate = new Date(start);
    let endDate = new Date(end);

    console.log("Start Date:" + startDate.getTime());
    console.log("End Date:" + endDate.getTime());

    let camp = new Campsite(site.name, site.id, site.rate);

    let reserve = new Reservation(1001, Number(camp.id), Number(startDate), Number(endDate), this.loginService.getUserName(), false, camp.rate);
    
    //calling to reservation service add reservation 
    this.reservationService.addReservation(reserve).subscribe();//reservation =>{this.search("")
    
  }



  isLoggedIn(): boolean{
    return this.loginService.isLoggedIn();
  }
  
  isAdmin(): boolean{
    return this.loginService.adminLoggedIn();
  }

}
