import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../model/User';
import { UserServiceService } from '../service/user-service.service';


@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent implements OnInit {
  loginForm = new FormGroup({});

  user = new User('', '')
  constructor(private userService : UserServiceService) {
    this.loginForm = new FormGroup({
      username : new FormControl(null),
      password : new FormControl(null),
    });

  }
  ngOnInit(): void {
  }
  logIn(){
    this.userService.login(this.user);
    var user = new User();
    user.username = this.loginForm.controls['username'].value;
    user.password = this.loginForm.controls['password'].value;
    console.log(user);
    this.userService.login(user).subscribe(
      (res) =>{
        console.log(res);
      },
      (error) =>{
        console.log(error);
        if(error.status === 200){
          this.userService.setActiveUser(user.username);
          window.location.href = 'chatui'
        }
      }
    )
  }


}
