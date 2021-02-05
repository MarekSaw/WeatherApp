import { Component, OnInit } from '@angular/core';
import {ForecastService} from '../../service/forecast.service';
import {ForecastModel} from '../model/ForecastModel';

@Component({
  selector: 'app-forecasts',
  templateUrl: './forecasts.component.html',
  styleUrls: ['./forecasts.component.css']
})
export class ForecastsComponent implements OnInit {

  forecastList: ForecastModel[];
  actualPage: number;
  totalPages: number;
  pageList: number[];
  isDataAvailable: boolean;

  constructor(private forecastService: ForecastService) { }

  ngOnInit(): void {
    this.actualPage = 1;
    this.forecastService.getAllForecasts(1).subscribe(value => {
      this.forecastList = value;
      this.getPageList();
    });
  }

  public getPage(page: number): void {
    this.actualPage = page;
    this.forecastService.getAllForecasts(page).subscribe(value => {
      this.forecastList = value;
      this.getPageList();
    });
    window.scrollTo(0, 0);
  }

  public deleteForecast(id: number): void {
    this.forecastService.deleteForecast(id).subscribe(() => {
      this.forecastService.getForecastsCount().subscribe(count => {
        this.totalPages = Math.ceil(count / 10);
        if (this.actualPage > this.totalPages) {
          this.getPage(1);
        } else {
          this.getPage(this.actualPage);
        }
      });
    });
  }

  private getPageList(): void {
    this.forecastService.getForecastsCount().subscribe(count => {
      this.totalPages = Math.ceil(count / 10);
      this.pageList = Array.from(Array(Math.ceil(count / 10)).keys());
      this.isDataAvailable = true;
    });
  }
}
