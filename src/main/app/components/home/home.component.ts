import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {WeatherForecastService} from '../../service/weather-forecast.service';
import {WeatherForecast} from '../model/WeatherForecast';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnChanges {

  cityName: string;
  weatherForecast: WeatherForecast;
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

  public findWeather(): void {
    console.log(this.cityName);
    this.weatherForecastService.findWeatherForLocation(this.cityName).subscribe(value => {
      this.weatherForecast = value;
      console.log(this.weatherForecast.temperature);
    });

  }


  ngOnChanges(changes: SimpleChanges): void {

  }
}
