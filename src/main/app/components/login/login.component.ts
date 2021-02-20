import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../service/auth.service';
import {TokenStorageService} from '../../service/token-storage.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginGroup = new FormGroup( {
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });
  isLoggedIn: boolean;
  isLoginFailed: boolean;
  isAlertActive: boolean;
  errorMessage = '';
  roles: string[] = [];
  username: string;

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService) { }

  ngOnInit(): void {
    this.username = this.tokenStorage.getUser().username;
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }
    $('.content').on('click', () => {
      if (this.isAlertActive) {
        ($('.toast') as any).animate({opacity: '0'});
        this.isAlertActive = false;
      }
    });
  }

  onSubmit(): void {
    if (this.loginGroup.valid) {
      this.logIn();
    } else {
      if (!this.loginGroup.get('username').valid) {
        document.getElementById('username').classList.add('is-invalid');
      }
      if (!this.loginGroup.get('password').valid) {
        document.getElementById('password').classList.add('is-invalid');
      }
    }

  }

  checkForm(): void {
    if (this.loginGroup.get('username').valid) {
      document.getElementById('username').classList.remove('is-invalid');
    }
    if (this.loginGroup.get('password').valid) {
      document.getElementById('password').classList.remove('is-invalid');
    }
  }

  public logIn(): void {
    this.authService.login(this.loginGroup.get('username').value, this.loginGroup.get('password').value).subscribe(
      data => {
        this.tokenStorage.saveToken(data.token);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        this.username = this.tokenStorage.getUser().username;
        this.reload();
      },
      error => {
        this.errorMessage = error.error.message;
        this.isLoginFailed = true;
        ($('#errorToast') as any).animate({opacity: '1'});
        this.isAlertActive = true;
      }
    );
  }

  public reload(): void {
    window.location.reload();
  }
}
