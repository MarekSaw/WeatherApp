import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {WeatherForecastModel} from '../components/model/WeatherForecastModel';

@Injectable({
  providedIn: 'root'
})
export class WeatherForecastService {

  private readonly url: string;

  constructor(private http: HttpClient) {
    this.url = 'http://localhost:8080/weather-api/weather-forecast';
  }

  public findWeatherForLocation(city: string): Observable<WeatherForecastModel> {
    return this.http.get<WeatherForecastModel>(`${this.url}?city=${city}`);
  }
}
