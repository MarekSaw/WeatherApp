import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {ForecastsComponent} from './components/forecasts/forecasts.component';
import {ForecastResolver} from './service/forecast.resolver';
import {WeatherAppComponent} from './components/weather-app/weather-app.component';
import {ContactComponent} from './components/contact/contact.component';
import {AboutComponent} from './components/about/about.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {ProfileComponent} from './components/profile/profile.component';
import {UsersComponent} from './components/users/users.component';
import {UserResolver} from './service/user.resolver';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'weather-app', component: WeatherAppComponent },
  { path: 'forecasts', component: ForecastsComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'about', component: AboutComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'users', component: UsersComponent, resolve: { users: UserResolver } },
  { path: 'page-not-found', component: PageNotFoundComponent },
  { path: '**', redirectTo: 'page-not-found' }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  providers: [ForecastResolver],
  exports: [RouterModule]
})
export class AppRoutingModule { }
