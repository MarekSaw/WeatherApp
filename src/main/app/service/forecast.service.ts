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

  public getAllForecasts(page: number): Observable<ForecastModel[]> {
    return this.http.get<ForecastModel[]>(`${this.url}?page=${page}`);
  }

  public getForecastsCount(): Observable<number> {
    return this.http.get<number>(`${this.url}/count`);
  }
}
