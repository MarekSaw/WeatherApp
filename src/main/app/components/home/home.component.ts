import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {WeatherForecastService} from '../../service/weather-forecast.service';
import {WeatherForecastModel} from '../model/WeatherForecastModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ForecastModel} from '../model/ForecastModel';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnChanges {

  cityName: string;
  weatherForecast: WeatherForecastModel;
  isCity: boolean;
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
  isDataAvailable: boolean;
  weatherLocation = '';
  weatherParametersModal: WeatherForecastModel = {temperature: 0, pressure: 0, humidity: 0, windSpeed: 0, windDeg: 0};

  constructor(private formBuilder: FormBuilder, private weatherForecastService: WeatherForecastService) {
    this.isCity = true;
  }

  ngOnInit(): void {
    $('.select').on('change', () => {
      // works only for double =. doesnt work for triple =.
      this.isCity = $('.select').val() == 1;
    });
    this.firstFormGroup = this.formBuilder.group({
      firstCtrl: ['', Validators.required]
    });
  }

  public findWeather(location: string): void {
    this.weatherForecastService.findWeatherForLocation(location).subscribe(value => {
      this.weatherForecast = value;
      this.loadDataToModal(location);
    });
  }

  public loadDataToModal(location: string): void {
    this.weatherLocation = location;
    for (const key in this.weatherParametersModal) {
      if (this.weatherForecast[key] !== null) {
        this.weatherParametersModal[key] = this.weatherForecast[key];
      }
    }
  }


  ngOnChanges(changes: SimpleChanges): void {

  }
}
