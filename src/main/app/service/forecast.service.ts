import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ForecastModel} from '../components/model/ForecastModel';

@Injectable({
  providedIn: 'root'
})
export class ForecastService {

  private readonly url: string;

  constructor(private http: HttpClient) {
    this.url = 'http://localhost:8080/weather-api/forecast';
  }

  public getAllForecastsPagination(page: number): Observable<ForecastModel[]> {
    return this.http.get<ForecastModel[]>(`${this.url}?page=${page}`);
  }

  public getAllForecasts(): Observable<ForecastModel[]> {
    return this.http.get<ForecastModel[]>(`${this.url}`);
  }

  public getAllForecastsByUserId(userId: number): Observable<ForecastModel[]> {
    return this.http.get<ForecastModel[]>(`${this.url}/${userId}`);
  }

  public deleteForecast(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }
}
