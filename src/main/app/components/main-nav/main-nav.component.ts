import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';

@Component({
  selector: 'app-main-nav',
  templateUrl: './main-nav.component.html',
  styleUrls: ['./main-nav.component.css']
})
export class MainNavComponent implements OnInit {

  isSideNavOpen: boolean;

  constructor() { }

  ngOnInit(): void {
    $('.side-nav-content').hide();
    $('.content').on('click', () => {
      if (this.isSideNavOpen) {
        this.toggleNav();
      }
    });
  }

  public toggleNav(): void {
    $('.side-nav-content').animate({width: 'toggle', opacity: 'toggle'});
    this.isSideNavOpen = !this.isSideNavOpen;
  }

}
