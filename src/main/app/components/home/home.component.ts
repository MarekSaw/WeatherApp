import {Component, OnInit} from '@angular/core';
import {WeatherForecastService} from '../../service/weather-forecast.service';
import {WeatherForecastModel} from '../model/WeatherForecastModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

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

  constructor(private formBuilder: FormBuilder, private weatherForecastService: WeatherForecastService) {
    this.isCity = true;
  }

  ngOnInit(): void {
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
      this.weatherForecastService.findWeatherForCity(this.weatherLocation).subscribe(
        value => {
          this.weatherForecast = value;
          this.loadDataToModal();
          ($('#weatherParameters') as any).modal('show');
        },
        error => {
          this.isSpinnerLoadingEnabled = false;
          $('.toast').animate({opacity: '1'});
          this.isAlertActive = true;
        });
    } else {
      const lat = this.locationGroup.get('latitude').value;
      const lon = this.locationGroup.get('longitude').value;
      this.weatherLocation = `lat: ${lat.toFixed(3)}, lon: ${lon.toFixed(3)}`;
      this.weatherForecastService.findWeatherForCoordinates(lat, lon).subscribe(
        value => {
          this.weatherForecast = value;
          this.loadDataToModal();
          ($('#weatherParameters') as any).modal('show');
        },
        error => {
          this.isSpinnerLoadingEnabled = false;
          $('.toast').animate({opacity: '1'});
          this.isAlertActive = true;
        });
    }
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
