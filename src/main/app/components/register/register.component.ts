import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../service/auth.service';
import {FormBuilder, FormControl, FormGroup, PatternValidator, Validators} from '@angular/forms';


function passwordMatchValidator(g: FormGroup): any {
  return g.get('passwordForm').value === g.get('passwordConfirmForm').value
    ? null : {mismatch: true};
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerGroup = new FormGroup({
    usernameForm: new FormControl('', [
      Validators.required,
      Validators.pattern('^(?![_ -])(?:(?![_ -]{2})[\\w -]){4,20}(?<![_ -])$'),
    ]),
    emailForm: new FormControl('', [
      Validators.required,
      Validators.pattern('^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$'),
    ]),
    passwordForm: new FormControl('', [
      Validators.required,
      Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-.,_]).{8,}$'),
    ]),
    passwordConfirmForm: new FormControl('', [
      Validators.required,
      Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-.,_]).{8,}$'),
    ])}, passwordMatchValidator);

  isSuccessful: boolean;
  isRegisterFailed: boolean;
  errorMessage = '';


  constructor(private authService: AuthService, private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.authService.register(this.registerGroup.get('usernameForm').value,
                              this.registerGroup.get('emailForm').value,
                              this.registerGroup.get('passwordForm').value).subscribe(
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
