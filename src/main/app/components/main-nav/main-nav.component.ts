import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';
import {TokenStorageService} from '../../service/token-storage.service';

@Component({
  selector: 'app-main-nav',
  templateUrl: './main-nav.component.html',
  styleUrls: ['./main-nav.component.css']
})
export class MainNavComponent implements OnInit {

  isSideNavOpen: boolean;
  isLoggedIn: boolean;
  username: string;

  constructor(private tokenStorage: TokenStorageService) { }

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
      this.username = user.username;
    }
  }

  logout(): void {
    this.tokenStorage.logout();
    window.location.reload();
  }

  public toggleNav(): void {
    $('.side-nav-content').animate({width: 'toggle', opacity: 'toggle'});
    this.isSideNavOpen = !this.isSideNavOpen;
  }

}
