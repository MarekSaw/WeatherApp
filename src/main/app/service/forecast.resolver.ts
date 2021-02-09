import { Injectable } from '@angular/core';
import {
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import { Observable} from 'rxjs';
import {ForecastModel} from '../components/model/ForecastModel';
import {ForecastService} from './forecast.service';

@Injectable({
  providedIn: 'root'
})
export class ForecastResolver implements Resolve<ForecastModel[]> {

  constructor(private forecastService: ForecastService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ForecastModel[]> {
    return this.forecastService.getAllForecasts();
  }
}
