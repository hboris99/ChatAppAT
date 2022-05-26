import { Injectable } from '@angular/core';
import {Message} from '../model/Message';
import { User } from '../model/User';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

const url = 'http://localhost:8080/Chat-war/api/chat/';

@Injectable({
  providedIn: 'root'
})


export class UserServiceService {



  constructor(private http : HttpClient) { }

  register(user : User){
     let urlToSend = url + "users/register";
     console.log(urlToSend)
     return this.http.post(urlToSend, user);
  }

  setActiveUser(username: string){
    sessionStorage.setItem('username', username);
  }

  login(user: User){
    let urlToSend = url + "users/login";
    return this.http.post(urlToSend, user);
  }
  getActiveUser(){
    if(this.userIsActive()){
      console.log(sessionStorage.getItem('username'));
      return sessionStorage.getItem('username');
    }
    else{
      return null;
    }
  }
  userIsActive() : boolean {
    return(
      sessionStorage.getItem('username') != '' && sessionStorage.getItem('username') != null
    );
  }
  signOut(){
    let urlToSend = url + "users/loggedIn/" + this.getActiveUser()!;
    console.log(typeof(this.getActiveUser()))
    console.log(urlToSend)
     return this.http.delete(urlToSend).subscribe((data) => {
       sessionStorage.clear();
       window.location.href = '/login'
     });
  }

  getAllLoggedInUsers(){
    const newUrl = url + 'users/loggedIn/' + this.getActiveUser()!;
    return this.http.get(newUrl);
  }

  getAllRegisteredUsers(){
    const newUrl = url + 'users/registered/' + this.getActiveUser()!;
    return this.http.get(newUrl);
  }

  sendMessage(message : Message){
    console.log(message)
    const newUrl = url + 'messages/user';
    return this.http.post(newUrl, message);
  }

  getUserMessages(){

    const newUrl = url + 'messages/' + this.getActiveUser();
    console.log(newUrl)
    return this.http.get(newUrl);
  }
  sendMessageToEveryoneActive(message: Message){
    const newUrl = url + 'messages/all';
    return this.http.post(newUrl, message);

  }

}


