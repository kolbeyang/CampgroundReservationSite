import { Injectable } from '@angular/core';
import { Product } from './Product';
import { Campsite } from './Campsite';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private productUrl = 'http://localhost:8080/campsites'

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient) { }

  getProducts(): Observable<Product[]>{
    return this.http.get<Product[]>(this.productUrl)
  } 

  updateProduct(product: Product): Observable<any> {
    return this.http.put(this.productUrl, product, this.httpOptions).pipe(
      tap(_ => this.log(`updated hero id=${product.id}`)),
      catchError(this.handleError<any>('updateCampsite'))
    );
    
  }

  deleteProduct(id: number): Observable<Campsite> {
    const url = `${this.productUrl}/${id}`;

    return this.http.delete<Campsite>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted hero id=${id}`)),
      catchError(this.handleError<Campsite>('deleteCampsite'))
    );
  }

  
  log(arg0: string): void {
    throw new Error('Method not implemented.');
  }
  handleError<T>(arg0: string): (err: any, caught: Observable<Object>) => import("rxjs").ObservableInput<any> {
    throw new Error('Method not implemented.');
  }
  

  //addProduct(name: string, id: number, rate: number): Observable<Campsite>{
    //const campsite = of(Campsite);
    //return campsite;
  //}
}
