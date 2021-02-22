import { Injectable } from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import {UserModel} from '../components/model/UserModel';
import {UserService} from './user.service';

@Injectable({
  providedIn: 'root'
})
export class UserResolver implements Resolve<UserModel[]> {

  constructor(private userService: UserService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<UserModel[]> {
    return this.userService.getUserList();
  }
}
