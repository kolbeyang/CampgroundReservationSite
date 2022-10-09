import { Component, OnInit } from '@angular/core';
import { Product } from '../Product';
import { PRODUCTS } from '../mockproducts'

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {

  constructor() { }

  products = PRODUCTS;
  selectedProduct?:Product;
  ngOnInit(): void {
  }

  onSelect(product: Product): void {
    this.selectedProduct = product;
  }
}
