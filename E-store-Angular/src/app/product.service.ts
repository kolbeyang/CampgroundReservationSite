import { Injectable } from '@angular/core';
import { Product } from './Product';
import { Observable, of } from 'rxjs';
import { Campsite } from './Campsite';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private productUrl = 'http://localhost:8080/campsites'


  constructor(
    private http: HttpClient) { }

  getProducts(): Observable<Product[]>{
    return this.http.get<Product[]>(this.productUrl)
  } 

  

  //addProduct(name: string, id: number, rate: number): Observable<Campsite>{
    //const campsite = of(Campsite);
    //return campsite;
  //}
}
