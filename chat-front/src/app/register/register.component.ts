import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { User } from '../model/User';
import { UserServiceService } from '../service/user-service.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm = new FormGroup({});

  user = new User('', '')
  constructor(private userService : UserServiceService) {
    this.registerForm = new FormGroup({
      username : new FormControl(null),
      password : new FormControl(null),
    });

  }
  ngOnInit(): void {
  }
  register(){
    var user = new User();
    user.username = this.registerForm.controls['username'].value;
    user.password = this.registerForm.controls['password'].value;
    console.log(user);
    this.userService.register(user).subscribe(
      (res) =>{
        console.log(res);
      },
      (error) =>{
        console.log(error);
      }
    )
  }
}
