import { Component, OnInit } from '@angular/core';
import { Product } from '../Product';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {

  constructor(private productService: ProductService) { }

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
}
