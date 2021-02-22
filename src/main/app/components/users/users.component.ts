import { Component, OnInit } from '@angular/core';
import {UserModel} from '../model/UserModel';
import {UserService} from '../../service/user.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  isUserListEmpty: boolean;
  userList: UserModel[];
  userListPage: UserModel[] = [];
  actualPage: number;
  totalPages: number;
  pageList: number[];
  pageSize = 10;
  isSpinnerDeletingEnabled: boolean;

  constructor(private userService: UserService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.userList = this.route.snapshot.data.users;
    if (this.userList.length !== 0) {
      this.actualPage = 1;
      this.getPageList(this.pageSize);
      this.getListForPage(this.actualPage, this.pageSize);
    } else {
      this.isUserListEmpty = true;
    }
  }

  public getPage(page: number): void {
    this.actualPage = page;
    this.getListForPage(page, this.pageSize);
    window.scrollTo(0, 0);
  }

  public deleteUser(id: number): void {
    this.isSpinnerDeletingEnabled = true;
    this.userService.deleteUserByAdmin(id).subscribe(() => {
      this.userService.getUserList().subscribe(users => {
        this.userList = users;
        this.getPageList(this.pageSize);
        if (this.totalPages === 0) {
          this.isUserListEmpty = true;
        }
        if (this.actualPage > this.totalPages) {
          this.getPage(1);
        } else {
          this.getPage(this.actualPage);
        }
        this.isSpinnerDeletingEnabled = false;
      });
    });
  }

  private getListForPage(page: number, size: number): void {
    if (page === this.totalPages) {
      this.userListPage = [];
      for (let i = 0, j = page * size - size; i < this.userList.length - (page - 1) * size; i++, j++) {
        this.userListPage[i] = this.userList[j];
      }
    } else {
      for (let i = 0, j = page * size - size; i < size; i++, j++) {
        this.userListPage[i] = this.userList[j];
      }
    }
  }

  private getPageList(size: number): void {
    this.totalPages = Math.ceil(this.userList.length / size);
    this.pageList = Array.from(Array(this.totalPages).keys());
  }

}
