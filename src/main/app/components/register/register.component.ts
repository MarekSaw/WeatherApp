import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../service/auth.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import * as $ from 'jquery';
import {Router} from '@angular/router';


function passwordMatchValidator(g: FormGroup): any {
  return g.get('password').value === g.get('passwordConfirm').value
    ? null : {mismatch: true};
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerGroup = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.pattern('^(?![_ -])(?:(?![_ -]{2})[\\w -]){4,20}(?<![_ -])$'),
    ]),
    email: new FormControl('', [
      Validators.required,
      Validators.pattern('^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$'),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-.,_]).{8,}$'),
    ]),
    passwordConfirm: new FormControl('', [
      Validators.required,
      Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-.,_]).{8,}$'),
    ])}, passwordMatchValidator);

  isAlertActive: boolean;
  isSuccessful: boolean;
  isRegisterFailed: boolean;
  errorMessage = '';


  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    $('.content').on('click', () => {
      if (this.isAlertActive) {
        $('.toast').animate({opacity: '0'});
        this.isAlertActive = false;
      }
    });
  }

  onSubmit(): void {
    if (this.registerGroup.valid) {
      this.register();
    } else {
      if (!this.registerGroup.get('username').valid) {
        $('#username').addClass('is-invalid');
      }
      if (!this.registerGroup.get('email').valid) {
        $('#email').addClass('is-invalid');
      }
      if (!this.registerGroup.get('password').valid) {
        $('#password').addClass('is-invalid');
      }
      if (!this.registerGroup.get('passwordConfirm').valid) {
        $('#passwordConfirm').addClass('is-invalid');
      }
    }
  }

  checkForm(): void {
    if (this.registerGroup.get('username').valid) {
      $('#username').removeClass('is-invalid');
    }
    if (this.registerGroup.get('email').valid) {
      $('#email').removeClass('is-invalid');
    }
    if (this.registerGroup.get('password').valid) {
      $('#password').removeClass('is-invalid');
    }
    if (this.registerGroup.get('passwordConfirm').valid) {
      $('#passwordConfirm').removeClass('is-invalid');
    }
  }

  public register(): void {
    this.authService.register(this.registerGroup.get('username').value,
      this.registerGroup.get('email').value,
      this.registerGroup.get('password').value).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.isRegisterFailed = false;
        $('#successToast').animate({opacity: '1'});
        this.isAlertActive = true;
      },
      error => {
        this.errorMessage = error.error.message;
        this.isRegisterFailed = true;
        $('#errorToast').animate({opacity: '1'});
        this.isAlertActive = true;
      }
    );
  }

  goToLoginPage(): void {
    this.router.navigateByUrl('login');
  }

}
