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

  public findWeatherForCity(city: string): Observable<WeatherForecastModel> {
    return this.http.get<WeatherForecastModel>(`${this.url}-standard?city=${city}`);
  }

  public findWeatherForCoordinates(latitude: number, longitude: number): Observable<WeatherForecastModel> {
    return this.http.get<WeatherForecastModel>(`${this.url}-standard?lat=${latitude}&lon=${longitude}`);
  }

  public findWeatherForCityForUser(userId: number, city: string): Observable<WeatherForecastModel> {
    return this.http.get<WeatherForecastModel>(`${this.url}/${userId}?city=${city}`);
  }

  public findWeatherForCoordinatesForUser(userId: number, latitude: number, longitude: number): Observable<WeatherForecastModel> {
    return this.http.get<WeatherForecastModel>(`${this.url}/${userId}?lat=${latitude}&lon=${longitude}`);
  }

  public findWeatherForCityWithDate(userId: number, city: string, date: string): Observable<WeatherForecastModel> {
    return this.http.get<WeatherForecastModel>(`${this.url}/${userId}?city=${city}&date=${date}`);
  }

  public findWeatherForCoordinatesWithDate(userId: number, latitude: number, longitude: number, date: string): Observable<WeatherForecastModel> {
    return this.http.get<WeatherForecastModel>(`${this.url}/${userId}?lat=${latitude}&lon=${longitude}&date=${date}`);
  }
}
