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
  isCity = true;
  isSpinnerLoadingEnabled: boolean;

  today = new Date(new Date().setDate(new Date(Date.now()).getDate()));
  sevenDaysForward = new Date(new Date().setDate(new Date(Date.now()).getDate() + 7));
  minDate = this.toInputDateString(this.today);
  maxDate = this.toInputDateString(this.sevenDaysForward);

  weatherGroup = new FormGroup({
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
    date: new FormControl(this.minDate, [
      Validators.required,
    ])
  });

  weatherLocation = '';
  weatherParametersModal: WeatherForecastModel = {temperature: 0, pressure: 0, humidity: 0, windSpeed: 0, windDeg: 0};
  isAlertActive: boolean;

  constructor(private formBuilder: FormBuilder, private weatherForecastService: WeatherForecastService) {

  }

  ngOnInit(): void {
    $('.content').on('click', () => {
      if (this.isAlertActive) {
        $('.toast').animate({opacity: '0'});
        this.isAlertActive = false;
      }
    });
    $('.select').on('change', () => {
      this.changeLocalizationType();
    });
  }

  public findWeather(): void {
    this.isSpinnerLoadingEnabled = true;
    const date = this.weatherGroup.get('date').value;
    if (this.isCity) {
      this.weatherLocation = this.weatherGroup.get('city').value;
      this.weatherForecastService.findWeatherForCityWithDate(this.weatherLocation, date).subscribe(
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
      const lat = this.weatherGroup.get('latitude').value;
      const lon = this.weatherGroup.get('longitude').value;
      this.weatherLocation = `lat: ${lat.toFixed(3)}, lon: ${lon.toFixed(3)}`;
      this.weatherForecastService.findWeatherForCoordinatesWithDate(lat, lon, date).subscribe(
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

  public isDateValid(): boolean {
    return Date.parse(this.weatherGroup.get('date').value) > new Date().setDate(new Date(Date.now()).getDate() - 1)
      && Date.parse(this.weatherGroup.get('date').value) < new Date().setDate(new Date(Date.now()).getDate() + 7);
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
    $(`#steph${step}`).addClass('active');
    $(`#step${step}`).addClass('active');
  }

  private changeLocalizationType(): void {
    this.isCity = !this.isCity;
    $(`#steph2`).removeClass('active');
    $(`#step2`).removeClass('active');
    $(`#steph3`).removeClass('active');
    $(`#step3`).removeClass('active');
  }

  private toInputDateString(date: Date): string {
    return `${date.getFullYear()}-${date.getMonth() < 9 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-${date.getDate() < 10 ? `0${date.getDate()}` : date.getDate()}`;
  }

}
