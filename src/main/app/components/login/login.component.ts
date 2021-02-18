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
  form: any = {
    username: null,
    password: null
  };
  loginGroup = new FormGroup( {
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });
  isLoggedIn: boolean;
  isLoginFailed: boolean;
  errorMessage = '';
  roles: string[] = [];

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService) { }

  ngOnInit(): void {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }
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
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        this.reloadPage();
      },
      error => {
        this.errorMessage = error.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  public reloadPage(): void {
    window.location.reload();
  }
}
