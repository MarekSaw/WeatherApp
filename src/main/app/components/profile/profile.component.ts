import { Component, OnInit } from '@angular/core';
import {TokenStorageService} from '../../service/token-storage.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: any;
  isEditModeEnabled: boolean;

  constructor(private token: TokenStorageService) { }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
  }

  toggleEditMode(): void {
     const input = ($('.edit-input') as any);
     if (this.isEditModeEnabled) {
      this.isEditModeEnabled = !this.isEditModeEnabled;
      input.attr('readonly', 'readonly');
      input.removeClass('form-control');
      input.addClass('form-control-plaintext');
     } else {
      this.isEditModeEnabled = !this.isEditModeEnabled;
      input.removeAttr('readonly');
      input.removeClass('form-control-plaintext');
      input.addClass('form-control');
      }
  }
}
