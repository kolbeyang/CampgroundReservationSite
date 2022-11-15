import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Campsite } from '../Campsite';
import { LoginService } from '../login.service';
import { ProductService } from '../product.service';
import { ReservationService } from '../reservation.service';

@Component({
  selector: 'app-create-campsite',
  templateUrl: './create-campsite.component.html',
  styleUrls: ['./create-campsite.component.css']
})
export class CreateCampsiteComponent implements OnInit {

  errorMessage = '';

  constructor(    
    private productService: ProductService, 
    private loginService: LoginService,
    private reservationService: ReservationService,
    private router: Router
  ) { }

  ngOnInit(): void {
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
    } else if(!ratenume.valueOf() || ratenume <= 1 || ratenume > 1000000){
      this.errorMessage = 'Invalid Rate Value, Try Again';
    }
    else if(isNaN(campsite.x) || isNaN(campsite.y)){
      this.errorMessage = 'Invalid Campsite Location';
    }
    else {
      this.errorMessage = '';
      this.productService.addProduct(campsite).subscribe({
        next: (campsite) => {
          this.router.navigate(['/response'], {state: {responseType : "campsiteCreated-success"}});
        },
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
