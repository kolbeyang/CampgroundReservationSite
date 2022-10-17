import { Component, OnInit } from '@angular/core';
import { Product } from '../Product';
import { ProductService } from '../product.service';
import { LoginService } from '../login.service';
import { User } from '../user';
import { Location } from '@angular/common';



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

  onSelectAdmin(product:Product): void {
    console.log('This was clicked')
    console.log(this.isAdmin());
    console.log(this.isLoggedIn());
    if(this.isAdmin()){
      this.selectedProduct = product;
    }
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


  goBack(): void {
    this.location.back();
  }



  createProduct():void{

  }

  deleteProduct(product: Product):void{
      this.products = this.products.filter(h => h !== product);
      this.productService.deleteProduct(product.id).subscribe();
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
