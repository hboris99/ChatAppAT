import { Injectable } from '@angular/core';
import {Message} from '../model/Message';
import { User } from '../model/User';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

const url = 'http://localhost:8080/Chat-war/api/users/';

@Injectable({
  providedIn: 'root'
})


export class UserServiceService {

  user: User = new User('', '');
  isLoggedIn = false;
  registeredUsers: User[] = [];
  loggedInUsers : User[] = [];

  constructor(private http : HttpClient, private toastr: ToastrService, private router: Router) { }

  register(user : User){
      return this.http.post(url + 'register', user).subscribe({
          next: () => (this.toastr.success("Succesfully registered")),
          error: () => (this.toastr.error("Failed to register."))
      });
  }


  login(user: User){
    return this.http.post(url + 'login', user).subscribe({
      next: (user) => {
      this.user = user as User;
      this.isLoggedIn = true;
      initSocket(this, this.router, this.toastr);
      this.router.navigate(['newmessage']);
    },
    error: () => (this.toastr.error("Bad password or username"))
    });
  }

  signOut(){
   return this.http.delete(url + 'loggedIn/' + this.user.username).subscribe({
     next: () => {
       this.isLoggedIn = false;
       this.user = new User('', '');
     }
   })
  }

  getAllLoggedInUsers(){
    return this.http.get(url + 'loggedIn').subscribe();
  }

  getAllRegisteredUsers(){
    return this.http.get(url + 'registered').subscribe();
  }





}

function initSocket(userService: UserServiceService,router: Router, toastr: ToastrService){
  let connection: WebSocket|null = new WebSocket("ws://localhost:8080/Chat-wr/ws/" + userService.user.username);
  connection.onopen = function(){
    console.log("Opened socket");
  }

  connection.onmessage = function(message){
    const info = message.data.split("!");
    if(info[0] === "LOGGEDIN"){
      let users : User[] = [];
      info[1].split("|").array.forEach((user: string) => {
        if(user){
          let userInfo = user.split(",");
          users.push(new User(userInfo[0], userInfo[1]));
        }
      });
      userService.loggedInUsers = users;
    }else if(info[0] ==="REGISTERED"){
      let users: User[] = []
      info[1].split("|").array.forEach((user: string) => {
        if(user){
          let userInfo = user.split(",");
          users.push(new User(userInfo[0], userInfo[1]));
        }
      });
      userService.registeredUsers = users;
    }
  }

}
