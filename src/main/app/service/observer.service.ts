import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ObserverService {

  usernameObservable = new Subject<string>();

  constructor() {
  }

  emitUsername(user: string): void {
    this.usernameObservable.next(user);
  }

}
