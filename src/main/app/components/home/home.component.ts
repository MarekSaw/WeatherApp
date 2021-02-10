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
  firstFormGroup: FormGroup;
  cityFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern('[a-zA-Z ]*'),
  ]);
  latitudeFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern('^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$'),
  ]);
  longitudeFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern('^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$'),
  ]);
  weatherLocation = '';
  weatherParametersModal: WeatherForecastModel = {temperature: 0, pressure: 0, humidity: 0, windSpeed: 0, windDeg: 0};

  constructor(private formBuilder: FormBuilder, private weatherForecastService: WeatherForecastService) {
    this.isCity = true;
  }

  ngOnInit(): void {
    $('.select').on('change', () => {
      this.isCity = !this.isCity;
    });
    this.firstFormGroup = this.formBuilder.group({
      firstCtrl: ['', Validators.required]
    });
  }

  public findWeather(location: string, latitude: number, longitude: number): void {
    this.isSpinnerLoadingEnabled = true;
    if (this.isCity) {
      this.weatherLocation = location;
      this.weatherForecastService.findWeatherForCity(location).subscribe(value => {
        this.weatherForecast = value;
        this.loadDataToModal();
        ($('#weatherParameters') as any).modal('show');
      });
    } else {
      this.weatherLocation = `lat: ${latitude.toFixed(3)}, lon: ${longitude.toFixed(3)}`;
      this.weatherForecastService.findWeatherForCoordinates(latitude, longitude).subscribe(value => {
        this.weatherForecast = value;
        this.loadDataToModal();
        ($('#weatherParameters') as any).modal('show');
      });
    }
  }

  public loadDataToModal(): void {
    for (const key in this.weatherParametersModal) {
      if (this.weatherForecast[key] !== null) {
        this.weatherParametersModal[key] = this.weatherForecast[key];
      }
    }
    this.isSpinnerLoadingEnabled = false;
  }
}
