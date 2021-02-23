import { Component, OnInit } from '@angular/core';
import {ForecastService} from '../../service/forecast.service';
import {ForecastModel} from '../model/ForecastModel';
import {WeatherForecastModel} from '../model/WeatherForecastModel';
import {TokenStorageService} from '../../service/token-storage.service';

@Component({
  selector: 'app-forecasts',
  templateUrl: './forecasts.component.html',
  styleUrls: ['./forecasts.component.css']
})
export class ForecastsComponent implements OnInit {

  currentUser: any;
  isAdminLoggedIn: boolean;
  isForecastListAvailable: boolean;
  forecastList: ForecastModel[];
  forecastListPage: ForecastModel[] = [];
  actualPage: number;
  totalPages: number;
  pageList: number[];
  pageSize = 10;
  weatherLocation = '';
  weatherParametersModal: WeatherForecastModel = {temperature: 0, pressure: 0, humidity: 0, windSpeed: 0, windDeg: 0};
  isLoadingSpinnerEnabled: boolean;
  isSpinnerDeletingEnabled: boolean;

  constructor(private tokenStorage: TokenStorageService, private forecastService: ForecastService) {
  }

  ngOnInit(): void {
    this.isLoadingSpinnerEnabled = true;
    this.currentUser = this.tokenStorage.getUser();
    this.isAdminLoggedIn = this.currentUser.roles.includes('ROLE_ADMIN');
    if (this.isAdminLoggedIn) {
      this.forecastService.getAllForecasts().subscribe(value => {
        this.forecastList = value;
        if (this.forecastList.length !== 0) {
          this.actualPage = 1;
          this.getPageList(this.pageSize);
          this.getListForPage(this.actualPage, this.pageSize);
          this.isForecastListAvailable = true;
        }
        this.isLoadingSpinnerEnabled = false;
      });
    } else {
      this.forecastService.getAllForecastsByUserId(this.currentUser.id).subscribe(value => {
        this.forecastList = value;
        if (this.forecastList.length !== 0) {
          this.actualPage = 1;
          this.getPageList(this.pageSize);
          this.getListForPage(this.actualPage, this.pageSize);
          this.isForecastListAvailable = true;
        }
        this.isLoadingSpinnerEnabled = false;
      });
    }
  }

  public getPage(page: number): void {
    this.actualPage = page;
    this.getListForPage(page, this.pageSize);
    window.scrollTo(0, 0);
  }

  public deleteForecast(id: number): void {
    this.isSpinnerDeletingEnabled = true;
    this.forecastService.deleteForecast(id).subscribe(() => {
      if (this.isAdminLoggedIn) {
        this.forecastService.getAllForecasts().subscribe(forecasts => {
          this.forecastList = forecasts;
          this.getPageList(this.pageSize);
          if (this.totalPages === 0) {
            this.isForecastListAvailable = false;
          }
          if (this.actualPage > this.totalPages) {
            this.getPage(1);
          } else {
            this.getPage(this.actualPage);
          }
          this.isSpinnerDeletingEnabled = false;
        });
      } else {
        this.forecastService.getAllForecastsByUserId(this.currentUser.id).subscribe(forecasts => {
          this.forecastList = forecasts;
          this.getPageList(this.pageSize);
          if (this.totalPages === 0) {
            this.isForecastListAvailable = false;
          }
          if (this.actualPage > this.totalPages) {
            this.getPage(1);
          } else {
            this.getPage(this.actualPage);
          }
          this.isSpinnerDeletingEnabled = false;
        });
      }
    });
  }

  public loadDataToModal(forecast: ForecastModel): void {
    this.weatherLocation = forecast.localization;
    for (const key in this.weatherParametersModal) {
      if (forecast.weatherForecast[key] !== null) {
        this.weatherParametersModal[key] = forecast.weatherForecast[key];
      }
    }
  }

  private getListForPage(page: number, size: number): void {
    if (page === this.totalPages) {
      this.forecastListPage = [];
      for (let i = 0, j = page * size - size; i < this.forecastList.length - (page - 1) * size; i++, j++) {
        this.forecastListPage[i] = this.forecastList[j];
      }
    } else {
      for (let i = 0, j = page * size - size; i < size; i++, j++) {
        this.forecastListPage[i] = this.forecastList[j];
      }
    }
  }

  private getPageList(size: number): void {
    this.totalPages = Math.ceil(this.forecastList.length / size);
    this.pageList = Array.from(Array(this.totalPages).keys());
  }

}
