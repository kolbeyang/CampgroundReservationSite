import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Product } from '../Product';
import { ProductService } from '../product.service';
import { LoginService } from '../login.service';
import { User } from '../user';
import { Location } from '@angular/common';
import { Campsite } from '../Campsite';
import { ReservationService } from '../reservation.service';
import { DeclarationListEmitMode } from '@angular/compiler';
import { Observable, Subject } from 'rxjs';
import {MatDialog} from '@angular/material/dialog';


import {
  debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';
import { Reservation } from '../Reservation';
import { ActivatedRoute, Router } from '@angular/router';
import { BrowseCampsitesComponent } from '../browse-campsites/browse-campsites.component';
import { AppRoutingModule } from '../app-routing.module';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {

  @Input() campsite?: Campsite;
  reservations: Reservation[] = [];

  products$!: Observable<Campsite[]>;
  private searchTerms = new Subject<string>();
  selectedProduct?:Product;
  errorMessage = '';
  successMessage = '';

  startDate?: Date;
  endDate?: Date;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService, 
    private loginService: LoginService,
    private reservationService: ReservationService,
    private location : Location,
    private appRoutingModule : AppRoutingModule, 
    private router: Router ,
    public dialog: MatDialog) {
  }
  
  ngOnInit(): void {

    this.products$ = this.productService.searchProducts("");
    this.getCampsite();
  }

  getReservations(): void {
    this.reservationService.getReservations().subscribe(
      (reservations) => {
        this.reservations = reservations.filter((reservation: Reservation) => {return reservation.campsiteId === this.campsite?.id});
        console.log("Got reservations of campsiteId " + this.campsite?.id);
      }
    );
  }

  onSelect(product: Product): void {
    this.selectedProduct = product;
  }

  /*
  getProducts(): void{
    this.productService.searchProducts("").subscribe(products => this.products$ = products);
  }
  */

  goBackToBrowse(): void{
    this.location.go('http://localhost:4200/browse');
  }

  getCampsite(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    console.log("Campsite id is " + id)
    this.productService.getProduct(id)
      .subscribe(campsite => {
        this.campsite = campsite;
        this.getReservations();
      });
  }

  // Push a search term into the observable stream.
  search(term: string): void {
    this.products$ = this.productService.searchProducts(term.toLowerCase());
  }

  editProduct(): void{
    this.errorMessage = '';
    this.successMessage = '';
    let ratenume = new Number((this.campsite?.rate));

    console.log('Selected product rate' +this.campsite?.rate);
    console.log(this.campsite?.rate.valueOf());

    if(this.campsite?.name.trim() === '' || !this.campsite?.name.toLowerCase().includes('campsite')){
      this.errorMessage = 'Invalid Campsite Name, Try Again';
      console.log("Error message was written");
    }
    else if (!ratenume.valueOf() || ratenume <= 1 || ratenume > 1000000){
      this.errorMessage = 'Invalid Rate, Try again';
    }else if(this.campsite) {
        this.errorMessage = '';
        this.successMessage = 'Successful purchase';
        let x = this.getPossiblex();
        let y = this.getPossibley();
        console.log("Setting coordinates to be " + x + " , " + y);
        let updatedCampsite = new Campsite(this.campsite.name,this.campsite.id,this.campsite.rate,x,y);
        this.productService.updateProduct(updatedCampsite)
        .subscribe(() => this.goBack());
      }
    
  }

  editConfirmation(): void {
    const dialogRef = this.dialog.open(EditContentDialog);
    dialogRef.afterClosed().subscribe(result => {
      this.editProduct();
    });

  }


  deleteConfirmation(product: Product):void{
      const dialogRef = this.dialog.open(DeleteContentDialog);

      dialogRef.afterClosed().subscribe(result => {
        if(result) this.deleteProduct(product);
      });
  }

  deleteProduct(product: Product):void{
    //this.products = this.products.filter(h => h !== product);
    this.productService.deleteProduct(product.id).subscribe( 
      () => {this.search("");
        //window.location.reload();
        this.router.navigate(["/browse"]);
      }
    );

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

  updateDateRange(dateRange: Date[]): void {
    this.startDate = dateRange[0];
    this.endDate = dateRange[1];
    console.log("Product detail received date range from " + this.startDate + " to " + this.endDate);
  }

  getPossiblex(): number{
    let campsite = this.productService.getPossibleCampsite();
    return campsite.x;
  }

  getPossibley(): number{
    let campsite = this.productService.getPossibleCampsite();
    return campsite.y;
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


  createReservation(start?: Date, end?: Date):void{
    // console.log("Start value" + start);
    // console.log("End Value" + end);
    if (!(start && end && this.campsite)) {
      return;
    }
    let currentDate = new Date();
    let startDate = new Date(start);
    let endDate = new Date(end);
    let site = this.campsite;

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

@Component({
  selector: 'delete-content-dialog',
  templateUrl: 'delete-content-dialog.html',
})
export class DeleteContentDialog {}



@Component({
  selector: 'edit-content-dialog',
  templateUrl: 'edit-content-dialog.html'
})
export class EditContentDialog {}
