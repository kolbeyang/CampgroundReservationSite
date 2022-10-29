import { Component, OnInit, Input } from '@angular/core';
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
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {

  @Input() campsite?: Campsite;

  products$!: Observable<Campsite[]>;
  private searchTerms = new Subject<string>();
  selectedProduct?:Product;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService, 
    private loginService: LoginService,
    private reservationService: ReservationService,
    private location : Location) {
  }
  
  ngOnInit(): void {

    this.products$ = this.productService.searchProducts("");
    this.getCampsite();
  }

  onSelect(product: Product): void {
    this.selectedProduct = product;
  }

  /*
  getProducts(): void{
    this.productService.searchProducts("").subscribe(products => this.products$ = products);
  }
  */

  getCampsite(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    console.log("Campsite id is " + id)
    this.productService.getProduct(id)
      .subscribe(campsite => this.campsite = campsite);
  }

  // Push a search term into the observable stream.
  search(term: string): void {
    this.products$ = this.productService.searchProducts(term.toLowerCase());
  }

  editProduct(): void{
    this.errorMessage = '';
    let ratenume = new Number((this.campsite?.rate));

    console.log('Selected product rate' +this.campsite?.rate);
    console.log(this.campsite?.rate.valueOf());
    
    // if(console.log(this.selectedProduct?.rate instanceof String)) {

    // }
    if(this.campsite?.name.trim() === '' || !this.campsite?.name.toLowerCase().includes('campsite')){
      this.errorMessage = 'Invalid Campsite Name, Try Again';
      console.log("Error message was written");
    }
    else if (!ratenume.valueOf() || ratenume <= 1 || ratenume > 1000000){
      this.errorMessage = 'Invalid Rate, Try again';
    }else if(this.campsite) {
        this.errorMessage = '';
        this.productService.updateProduct(this.campsite)
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

  createProduct(name: string, rate: string, x: string, y: string):void{
    const DUMMYNUMBER = 23;
    let ratenume = new Number(rate);
    let xcoord = new Number(x);
    let ycoord = new Number(y);
    let campsite = new Campsite(name, DUMMYNUMBER, ratenume.valueOf(), xcoord.valueOf(), ycoord.valueOf()); 
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

  //   /**
  //  * Handles Errors from a failed login
  //  * @param error The error to check
  //  */
  //    handleCreateReservationError(error: any) {
  //     console.log("Error caught in component ");
  //     console.log(error.status);
  //     this.errorMessage = 'Reservation has a conflict.';
  //   }

  createReservation(start: string, end: string, site: Campsite):void{
    // console.log("Start value" + start);
    // console.log("End Value" + end);
    let currentDate = new Date();
    let startDate = new Date(start);
    let endDate = new Date(end);

    // console.log("Start Date:" + startDate.getTime());
    // console.log("End Date:" + endDate.getTime());
    this.errorMessage = '';
    if(startDate.getTime() < currentDate.getTime()){
      this.errorMessage = 'Cannot Create Reservation in the past'
    }
    else if(endDate.getFullYear() > currentDate.getFullYear()){
      this.errorMessage = 'Cannot Book Reservations outside of this year';
    }
    else if(endDate.getTime() <= startDate.getTime()){
      this.errorMessage = 'Invalid Reservation Time';
    }
    else{
      let camp = new Campsite(site.name, site.id, site.rate, site.x, site.y);

      let reserve = new Reservation(1001, Number(camp.id), Number(startDate), Number(endDate), this.loginService.getUserName(), false, camp.rate);
      
      //calling to reservation service add reservation 
      // this.reservationService.addReservation(reserve).subscribe(
      //  (error) => this.handleCreateReservationError(error)); //reservation =>{this.search("")
      this.reservationService.addReservation(reserve).subscribe();
    }


    
  }

  goBack(): void {
    this.location.back();
  }

  isLoggedIn(): boolean{
    return this.loginService.isLoggedIn();
  }
  
  isAdmin(): boolean{
    return this.loginService.adminLoggedIn();
  }

}
