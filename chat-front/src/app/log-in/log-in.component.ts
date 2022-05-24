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

  user = new User('', '')
  constructor(private userService : UserServiceService) {

  }
  ngOnInit(): void {
  }
  submit(){
    this.userService.login(this.user);
  }


}
