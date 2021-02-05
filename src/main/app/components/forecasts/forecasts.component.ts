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

  constructor(private forecastService: ForecastService) { }

  ngOnInit(): void {
    this.actualPage = 1;
    this.forecastService.getAllForecasts(1).subscribe(value => this.forecastList = value);
    this.forecastService.getForecastsCount().subscribe(value => {
      this.totalPages = Math.ceil(value / 10);
      this.pageList = Array.from(Array(Math.ceil(value / 10)).keys());
    });
  }

  public getPage(page: number): void {
    this.actualPage = page;
    this.forecastService.getAllForecasts(page).subscribe(value => this.forecastList = value);
  }
}
