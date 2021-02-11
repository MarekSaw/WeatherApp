import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {ForecastsComponent} from './components/forecasts/forecasts.component';
import {ForecastResolver} from './service/forecast.resolver';
import {WeatherAppComponent} from './components/weather-app/weather-app.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'weather-app', component: WeatherAppComponent },
  { path: 'forecasts', component: ForecastsComponent, resolve: { forecasts: ForecastResolver } }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  providers: [ForecastResolver],
  exports: [RouterModule]
})
export class AppRoutingModule { }
