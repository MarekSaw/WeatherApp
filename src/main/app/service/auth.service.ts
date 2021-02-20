import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

const AUTH_URL = 'http://localhost:8080/user';
const HTTP_OPTIONS = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  public login(username: string, password: string): Observable<any> {
    return this.http.post(`${AUTH_URL}/login`, { username, password }, HTTP_OPTIONS);
  }

  public register(username: string, email: string, password: string): Observable<any> {
    return this.http.post(`${AUTH_URL}/register`, { username, email, password }, HTTP_OPTIONS);
  }

  public updateUser(loginRequest: any, user: any): Observable<any> {
    return this.http.put(`${AUTH_URL}/update`, { loginRequest, user }, HTTP_OPTIONS);
  }

}
