import {Component, OnInit} from '@angular/core';
import {WeatherForecastService} from '../../service/weather-forecast.service';
import {WeatherForecastModel} from '../model/WeatherForecastModel';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {TokenStorageService} from '../../service/token-storage.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  isLoggedIn: boolean;
  currentUser: any;
  cityName: string;
  weatherForecast: WeatherForecastModel;
  isCity: boolean;
  isSpinnerLoadingEnabled: boolean;
  locationGroup = new FormGroup({
    city: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z ]*'),
    ]),
    latitude: new FormControl('', [
      Validators.required,
      Validators.pattern('^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$'),
    ]),
    longitude: new FormControl('', [
      Validators.required,
      Validators.pattern('^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$'),
    ]),
  });

  weatherLocation = '';
  weatherParametersModal: WeatherForecastModel = {temperature: 0, pressure: 0, humidity: 0, windSpeed: 0, windDeg: 0};
  isAlertActive: boolean;

  constructor(private tokenStorage: TokenStorageService, private weatherForecastService: WeatherForecastService) {
    this.isCity = true;
  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorage.getToken();
    this.currentUser = this.tokenStorage.getUser();
    $('.content').on('click', () => {
      if (this.isAlertActive) {
        $('.toast').animate({opacity: '0'});
        this.isAlertActive = false;
      }
    });
    $('.select').on('change', () => {
      this.isCity = !this.isCity;
    });
  }

  public findWeather(): void {
    this.isSpinnerLoadingEnabled = true;
    if (this.isCity) {
      this.weatherLocation = this.locationGroup.get('city').value;
      if (this.isLoggedIn) {
        this.weatherForecastService.findWeatherForCityForUser(this.currentUser.id, this.weatherLocation).subscribe(
          value => {
            this.searchWeatherSuccess(value);
          },
          error => {
           this.searchWeatherError();
          });
      } else {
        this.weatherForecastService.findWeatherForCity(this.weatherLocation).subscribe(
          value => {
            this.searchWeatherSuccess(value);
          },
          error => {
           this.searchWeatherError();
          });
      }
    } else {
      const lat = this.locationGroup.get('latitude').value;
      const lon = this.locationGroup.get('longitude').value;
      this.weatherLocation = `lat: ${lat.toFixed(3)}, lon: ${lon.toFixed(3)}`;
      if (this.isLoggedIn) {
        this.weatherForecastService.findWeatherForCoordinatesForUser(this.currentUser.id, lat, lon).subscribe(
          value => {
            this.searchWeatherSuccess(value);
          },
          error => {
           this.searchWeatherError();
          });
      } else {
        this.weatherForecastService.findWeatherForCoordinates(lat, lon).subscribe(
          value => {
            this.searchWeatherSuccess(value);
          },
          error => {
           this.searchWeatherError();
          });
      }
    }
  }

  private searchWeatherSuccess(value: any): void {
    this.weatherForecast = value;
    this.loadDataToModal();
    ($('#weatherParameters') as any).modal('show');
  }

  private searchWeatherError(): void {
    this.isSpinnerLoadingEnabled = false;
    $('.toast').animate({opacity: '1'});
    this.isAlertActive = true;
  }

  private loadDataToModal(): void {
    for (const key in this.weatherParametersModal) {
      if (this.weatherForecast[key] !== null) {
        this.weatherParametersModal[key] = this.weatherForecast[key];
      }
    }
    this.isSpinnerLoadingEnabled = false;
  }
}
