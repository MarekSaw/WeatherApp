import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {WeatherForecast} from '../components/model/WeatherForecast';

@Injectable({
  providedIn: 'root'
})
export class WeatherForecastService {

  private url: string;

  constructor(private http: HttpClient) {
    this.url = 'http://localhost:8080/weather-api/forecast';
  }

  public findWeatherForLocation(city: string): Observable<WeatherForecast> {
    return this.http.get<WeatherForecast>(`${this.url}?city=${city}`);
  }
}
