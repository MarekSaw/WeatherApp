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

  formGroup: FormGroup;
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
  isAlertActive: boolean;

  today: Date;
  sevenDaysForward: Date;
  minDate: string;
  maxDate: string;

  constructor(private formBuilder: FormBuilder, private weatherForecastService: WeatherForecastService) {
    this.isCity = true;
    this.today = new Date(new Date().setDate(new Date(Date.now()).getDate()));
    this.sevenDaysForward = new Date(new Date().setDate(new Date(Date.now()).getDate() + 7));
    this.minDate = this.toInputDateString(this.today);
    this.maxDate = this.toInputDateString(this.sevenDaysForward);
    this.dateFormControl = new FormControl(this.minDate, [
      Validators.required,
    ]);
    this.formGroup = this.formBuilder.group({
      firstCtrl: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    $('.content').on('click', () => {
      if (this.isAlertActive) {
        ($('.toast') as any).animate({opacity: '0'});
        this.isAlertActive = false;
      }
    });
    $('.select').on('change', () => {
      this.changeLocalizationType();
    });
  }

  public findWeather(location: string, latitude: number, longitude: number, date: string): void {
    this.isSpinnerLoadingEnabled = true;
    if (this.isCity) {
      this.weatherLocation = location;
      this.weatherForecastService.findWeatherForCityWithDate(location, date).subscribe(
        value => {
          this.weatherForecast = value;
          this.loadDataToModal();
          ($('#weatherParameters') as any).modal('show');
        },
        error => {
          this.isSpinnerLoadingEnabled = false;
          ($('.toast') as any).animate({opacity: '1'});
          this.isAlertActive = true;
        });
    } else {
      this.weatherLocation = `lat: ${latitude.toFixed(3)}, lon: ${longitude.toFixed(3)}`;
      this.weatherForecastService.findWeatherForCoordinatesWithDate(latitude, longitude, date).subscribe(
        value => {
          this.weatherForecast = value;
          this.loadDataToModal();
          ($('#weatherParameters') as any).modal('show');
        },
        error => {
          this.isSpinnerLoadingEnabled = false;
          ($('.toast') as any).animate({opacity: '1'});
          this.isAlertActive = true;
        });
    }
  }

  public isDateValid(): boolean {
    return Date.parse(this.dateFormControl.value) > new Date().setDate(new Date(Date.now()).getDate() - 1)
      && Date.parse(this.dateFormControl.value) < new Date().setDate(new Date(Date.now()).getDate() + 7);
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
