import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../service/auth.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';


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

  isSuccessful: boolean;
  isRegisterFailed: boolean;
  errorMessage = '';


  constructor(private authService: AuthService) {
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    if (this.registerGroup.valid) {
      this.register();
    } else {
      if (!this.registerGroup.get('username').valid) {
        document.getElementById('username').classList.add('is-invalid');
      }
      if (!this.registerGroup.get('email').valid) {
        document.getElementById('email').classList.add('is-invalid');
      }
      if (!this.registerGroup.get('password').valid) {
        document.getElementById('password').classList.add('is-invalid');
      }
      if (!this.registerGroup.get('passwordConfirm').valid) {
        document.getElementById('passwordConfirm').classList.add('is-invalid');
      }
    }
  }

  checkForm(): void {
    if (this.registerGroup.get('username').valid) {
      document.getElementById('username').classList.remove('is-invalid');
    }
    if (this.registerGroup.get('email').valid) {
      document.getElementById('email').classList.remove('is-invalid');
    }
    if (this.registerGroup.get('password').valid) {
      document.getElementById('password').classList.remove('is-invalid');
    }
    if (this.registerGroup.get('passwordConfirm').valid) {
      document.getElementById('passwordConfirm').classList.remove('is-invalid');
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
      },
      error => {
        this.errorMessage = error.error.message;
        this.isRegisterFailed = true;
      }
    );
  }

}
