import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from '../../service/token-storage.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import * as $ from 'jquery';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: any;
  isEditModeEnabled: boolean;

  updateGroup = new FormGroup({
    newUsername: new FormControl('', [
      Validators.required,
      Validators.pattern('^(?![_ -])(?:(?![_ -]{2})[\\w -]){4,20}(?<![_ -])$'),
    ]),
    newEmail: new FormControl('', [
      Validators.required,
      Validators.pattern('^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$'),
    ]),
    newPassword: new FormControl('', [
      Validators.required,
      Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-.,_]).{8,}$'),
    ])
  });
  authorizeGroup = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  errorMessage: string;
  isAlertActive: boolean;

  constructor(private tokenStorage: TokenStorageService, private auth: AuthService) {
  }

  ngOnInit(): void {
    $('.content').on('click', () => {
      if (this.isAlertActive) {
        $('#errorToast').animate({opacity: '0'});
        $('#successToast').animate({opacity: '0'});
        this.isAlertActive = false;
      }
    });
    this.currentUser = this.tokenStorage.getUser();
    console.log(this.tokenStorage.getToken());
    this.updateGroup.get('newUsername').setValue('user14');
    this.updateGroup.get('newEmail').setValue(this.currentUser.email);
    this.updateGroup.get('newPassword').setValue('Mango123.');
    this.authorizeGroup.get('password').setValue('Mango123.');
  }

  toggleEditMode(): void {
    const input = $('.edit-input');
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

  showAuthorizeToast(): void {
    if (this.updateGroup.valid) {
      $('#authorizeToast').animate({opacity: '1'}).removeClass('hide');
    } else {
      if (!this.updateGroup.get('newUsername').valid) {
        $('#newUsername').addClass('is-invalid');
      }
      if (!this.updateGroup.get('newEmail').valid) {
        $('#newEmail').addClass('is-invalid');
      }
      if (!this.updateGroup.get('newPassword').valid) {
        $('#newPassword').addClass('is-invalid');
        $('#authorizeToast').addClass('hide');
      }
    }
  }

  hideAuthorizeToast(): void {
    $('#authorizeToast').animate({opacity: '0'}).addClass('hide');
  }

  checkForm(): void {
    if (this.authorizeGroup.get('username').valid) {
      $('#username').removeClass('is-invalid');
    }
    if (this.authorizeGroup.get('password').valid) {
      $('#password').removeClass('is-invalid');
    }

    if (this.updateGroup.get('newUsername').valid) {
      $('#newUsername').removeClass('is-invalid');
    }
    if (this.updateGroup.get('newEmail').valid) {
      $('#newEmail').removeClass('is-invalid');
    }
    if (this.updateGroup.get('newPassword').valid) {
      $('#newPassword').removeClass('is-invalid');
    }
  }

  onSubmit(): void {
    if (this.authorizeGroup.valid) {
      this.update();
      this.hideAuthorizeToast();
    } else {
      if (!this.authorizeGroup.get('username').valid) {
        $('#username').addClass('is-invalid');
      }
      if (!this.authorizeGroup.get('password').valid) {
        $('#password').addClass('is-invalid');
      }
    }
  }

  private update(): void {
    const loginRequest = {username: this.authorizeGroup.get('username').value, password: this.authorizeGroup.get('password').value};
    const user = {
      username: this.updateGroup.get('newUsername').value,
      email: this.updateGroup.get('newEmail').value,
      password: this.updateGroup.get('newPassword').value
    };
    this.auth.updateUser(loginRequest, user).subscribe(
      data => {
        this.tokenStorage.saveUser(data);
        this.tokenStorage.saveToken(data.token);
        $('#successToast').animate({opacity: '1'});
        this.isAlertActive = true;
      },
      error => {
        this.errorMessage = error.error.message;
        $('#errorToast').animate({opacity: '1'});
        this.isAlertActive = true;
      }
    );
  }
}
