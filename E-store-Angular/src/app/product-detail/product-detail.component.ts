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
    console.log("searching");
    this.products$ = this.productService.searchProducts(term.toLowerCase());
  }

  editProduct(): void{

      console.log("This ran" + this.selectedProduct);
      if (this.selectedProduct) {
        this.productService.updateProduct(this.selectedProduct)
        .subscribe();
      }
    
  }

  createProduct(id: string, name: string, rate: string):void{
    let idnum = new Number(id);
    let ratenume = new Number(id);
    let campsite = new Campsite(name, idnum.valueOf(), ratenume.valueOf()); 
    console.log("Attempt to create Product with: " + name);
    console.log("Does name contain campsite:" + name.toLowerCase().includes('campsites'));
    console.log('Does name.trim() give a empty value' + name.trim() === '');

    if(name.trim() === '' || !name.toLowerCase().includes('campsite')){
      this.errorMessage = 'Invalid Campsite Name, Try Again';
      console.log("Error message was written");
    }
    else{
      this.productService.addProduct(campsite).subscribe(campsite => {
        this.search("");
      });
    }

  }

  deleteProduct(product: Product):void{
    //this.products = this.products.filter(h => h !== product);
    this.productService.deleteProduct(product.id).subscribe(() => this.search(""));
}

  onSelectAdmin(product:Product): void {
    console.log('Admin Clicked to view some products')
    console.log(this.isAdmin());
    console.log(this.isLoggedIn());
    if(this.isAdmin()){
      this.selectedProduct = product;
    }
  }


  createReservation(start: string, end: string):void{
    console.log("Start value" + start);
    console.log("End Value" + end);
    let startDate = new Date(start);
    let endDate = new Date(end);
    console.log("Start Date:" + startDate.getTime());
    console.log("End Date:" + endDate.getTime());
  }

  isLoggedIn(): boolean{
    return this.loginService.isLoggedIn();
  }
  
  isAdmin(): boolean{
    return this.loginService.adminLoggedIn();
  }

}
