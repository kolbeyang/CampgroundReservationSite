import { Injectable } from '@angular/core';
import { Product } from './Product';
import { PRODUCTS } from './mockproducts';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor() { }

  getProducts(): Observable<Product[]>{
    const products = of(PRODUCTS);
    return products;
  } 


}
