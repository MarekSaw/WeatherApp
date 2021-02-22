import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';
import {TokenStorageService} from '../../service/token-storage.service';
import {ObserverService} from '../../service/observer.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-main-nav',
  templateUrl: './main-nav.component.html',
  styleUrls: ['./main-nav.component.css']
})
export class MainNavComponent implements OnInit {

  isSideNavOpen: boolean;
  isLoggedIn: boolean;
  isAdminLoggedIn: boolean;
  username: string;

  constructor(private tokenStorage: TokenStorageService, private subscriber: ObserverService, private router: Router) {
    this.subscriber.usernameObservable.subscribe(value =>  this.username = value);
  }

  ngOnInit(): void {
    $('.side-nav-content').hide();
    $('.content').on('click', () => {
      if (this.isSideNavOpen) {
        this.toggleNav();
      }
    });

    this.isLoggedIn = !!this.tokenStorage.getToken();
    if (this.isLoggedIn) {
      const user = this.tokenStorage.getUser();
      this.isAdminLoggedIn = user.roles.includes('ROLE_ADMIN');
      this.username = user.username;
    }
  }

  logout(): void {
    this.tokenStorage.logout();
    this.isLoggedIn = false;
    this.router.navigateByUrl('');
  }

  public toggleNav(): void {
    $('.side-nav-content').animate({width: 'toggle', opacity: 'toggle'});
    this.isSideNavOpen = !this.isSideNavOpen;
  }

}
