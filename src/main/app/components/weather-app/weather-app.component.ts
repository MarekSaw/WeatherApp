import { Component, OnInit } from '@angular/core';
import {WeatherForecastModel} from '../model/WeatherForecastModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {WeatherForecastService} from '../../service/weather-forecast.service';

@Component({
  selector: 'app-weather-app',
  templateUrl: './weather-app.component.html',
  styleUrls: ['./weather-app.component.css']
})
export class WeatherAppComponent implements OnInit {

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
  dateFormControl: FormControl;

  weatherLocation = '';
  weatherParametersModal: WeatherForecastModel = {temperature: 0, pressure: 0, humidity: 0, windSpeed: 0, windDeg: 0};
  tomorrow: Date;
  eightDaysForward: Date;
  minDate: string;
  maxDate: string;

  constructor(private formBuilder: FormBuilder, private weatherForecastService: WeatherForecastService) {
    this.isCity = true;
    this.tomorrow = new Date(new Date().setDate(new Date(Date.now()).getDate() + 1));
    this.eightDaysForward = new Date(new Date().setDate(new Date(Date.now()).getDate() + 8));
    this.minDate = this.toInputDateString(this.tomorrow);
    this.maxDate = this.toInputDateString(this.eightDaysForward);
    this.dateFormControl = new FormControl(this.minDate, [
      Validators.required,
    ]);
  }

  ngOnInit(): void {
    $('.select').on('change', () => {
      this.changeLocalizationType();
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

  public isDateValid(): boolean {
    return Date.parse(this.dateFormControl.value) > new Date().setDate(new Date(Date.now()).getDate())
      && Date.parse(this.dateFormControl.value) < new Date().setDate(new Date(Date.now()).getDate() + 8);
  }

  public loadDataToModal(): void {
    for (const key in this.weatherParametersModal) {
      if (this.weatherForecast[key] !== null) {
        this.weatherParametersModal[key] = this.weatherForecast[key];
      }
    }
    this.isSpinnerLoadingEnabled = false;
  }

  public goToStep(step: number): void {
    document.getElementById(`steph${step}`).classList.add('active');
    document.getElementById(`step${step}`).classList.add('active');
  }

  private changeLocalizationType(): void {
    this.isCity = !this.isCity;
    document.getElementById(`steph2`).classList.remove('active');
    document.getElementById(`step2`).classList.remove('active');
    document.getElementById(`steph3`).classList.remove('active');
    document.getElementById(`step3`).classList.remove('active');
  }

  private toInputDateString(date: Date): string {
    return `${date.getFullYear()}-${date.getMonth() < 9 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-${date.getDate() < 10 ? `0${date.getDate()}` : date.getDate()}`;
  }

}
