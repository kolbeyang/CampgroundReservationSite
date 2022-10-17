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
      tap(_ => this.log(`deleted Campsite id=${id}`)),
      catchError(this.handleError<Campsite>('deleteCampsite'))
    );
  }

  
  log(arg0: string): void {

  }
  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
   private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
}
}