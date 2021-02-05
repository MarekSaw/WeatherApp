import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MainNavComponent} from './components/main-nav/main-nav.component';
import {AppComponent} from './app.component';
import {HomeComponent} from './components/home/home.component';

const routes: Routes = [
  { path: '', component: HomeComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
