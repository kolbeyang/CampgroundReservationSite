import { Component, OnInit } from '@angular/core';
import { Product } from '../Product';
import { ProductService } from '../product.service';
import { LoginService } from '../login.service';
import { User } from '../user';
import { Location } from '@angular/common';
import { Campsite } from '../Campsite';



@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {

  constructor(private productService: ProductService, private loginService: LoginService, private location : Location) { 
    
  }

  products: Product[] = [];
  selectedProduct?:Product;
  
  ngOnInit(): void {
    this.getProducts();
  }

  onSelect(product: Product): void {
    this.selectedProduct = product;
  }



  getProducts(): void{
    this.productService.getProducts().subscribe(products => this.products = products);
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
    this.productService.addProduct(campsite).subscribe(campsite => {
      this.products.push(campsite);
    });
  }

  deleteProduct(product: Product):void{
    this.products = this.products.filter(h => h !== product);
    this.productService.deleteProduct(product.id).subscribe();
}

  onSelectAdmin(product:Product): void {
    console.log('Admin Clicked to view some products')
    console.log(this.isAdmin());
    console.log(this.isLoggedIn());
    if(this.isAdmin()){
      this.selectedProduct = product;
    }
  }


  createReservation():void{

  }

  isLoggedIn(): boolean{
    return this.loginService.isLoggedIn();
  }
  
  isAdmin(): boolean{
    console.log(this.loginService.adminLoggedIn());
    return this.loginService.adminLoggedIn();
  }


  


}
