import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';

@Component({
  selector: 'app-main-nav',
  templateUrl: './main-nav.component.html',
  styleUrls: ['./main-nav.component.css']
})
export class MainNavComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    $('.side-nav-content').hide();
  }

  public toggleNav(): void {
    $('.side-nav-content').animate({width: 'toggle', opacity: 'toggle'});
  }

}
