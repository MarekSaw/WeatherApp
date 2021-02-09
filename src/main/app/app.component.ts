import { Component } from '@angular/core';
import {NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router, RouterEvent} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'WeatherApp';
  isLoadingSpinnerEnabled: boolean;

  constructor(private router: Router) {
    this.router.events.subscribe((event: RouterEvent) => this.navigationEvents(event));
  }

  private navigationEvents(event: RouterEvent): void {
    if (event instanceof NavigationStart) {
      this.isLoadingSpinnerEnabled = true;
    }
    if (event instanceof NavigationEnd) {
      this.isLoadingSpinnerEnabled = false;
    }
    if (event instanceof NavigationCancel) {
      this.isLoadingSpinnerEnabled = false;
    }
    if (event instanceof NavigationError) {
      this.isLoadingSpinnerEnabled = false;
    }
  }

}
