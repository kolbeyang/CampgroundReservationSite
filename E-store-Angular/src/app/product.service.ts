import { Injectable } from '@angular/core';
import { Product } from './Product';
import { Campsite } from './Campsite';
import { Reservation } from './Reservation';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private productUrl = 'http://localhost:8080/campsites'
  campsite: Campsite = new Campsite("Possible Campsite Location", 900, 10, 0, 0);



  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  setPossibleCampsiteLocation(x: number, y: number): void{
    if(x > 460 || y > 460 ){
        // do nothing
    }
    else{
      this.campsite = new Campsite(this.campsite.name, this.campsite.id, this.campsite.rate, x, y);    
    }
  }

  private resetPossibleCampsite():void {
    this.campsite = new Campsite("Possible Campsite Location", -1, -12, 0, 0);
  }

  getPossibleCampsite(): Campsite{
    return this.campsite;
  }

  constructor(
    private http: HttpClient) { }

  getProducts(): Observable<Campsite[]>{
    return this.http.get<Campsite[]>(this.productUrl)
  } 

  /** GET campsite by id. Will 404 if id not found */
  getProduct(id: number): Observable<Campsite> {
    const url = `${this.productUrl}/${id}`;
    return this.http.get<Campsite>(url).pipe(
      tap(_ => this.log(`fetched hero id=${id}`)),
      catchError(this.handleError<Campsite>(`getProduct id=${id}`))
    );
  }

  updateProduct(product: Product): Observable<any> {
    this.resetPossibleCampsite();
    return this.http.put(this.productUrl, product, this.httpOptions).pipe(
      tap(_ => this.log(`updated campsite id=${product.id}`)),
      catchError(this.handleError<any>('updateCampsite'))
    );
    
  }

    /* GET products whose name contains search term */
    searchProducts(term: string): Observable<Campsite[]> {
      term = term.toLowerCase().trim();
      console.log("Product Service term " + term );

      if (term === "") {
        // if not search term, return empty hero array.
        return this.getProducts();
      }

      return this.http.get<Campsite[]>(`${this.productUrl}/?name=${term.toLowerCase().trim()}`).pipe(
        tap(x => x.length ?
           this.log(`found campsites matching "${term}"`) :
           this.log(`no campsites matching "${term}"`)),
        catchError(this.handleError<Campsite[]>('searchCampsites', []))
      );
    }

  deleteProduct(id: number): Observable<Campsite> {
    const url = `${this.productUrl}/${id}`;

    return this.http.delete<Campsite>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted Campsite id=${id}`)),
      catchError(this.handleError<Campsite>('deleteCampsite'))
    );
  }

  addProduct(campsite: Campsite){
    this.resetPossibleCampsite();
    return this.http.post<Campsite>(this.productUrl, campsite, this.httpOptions).pipe(     
    catchError(this.handleError<any>('addCampsite')));
  }
  
  log(arg0: string): void {

  }


  getReservationsOfCampsite(id: number): Observable<Reservation[]> {
    const url = `${this.productUrl}/${id}/reservations`;
    return this.http.get<Reservation[]>(url, this.httpOptions).pipe(
      catchError(this.handleError<any>('getReservationsOfCampsite')));
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