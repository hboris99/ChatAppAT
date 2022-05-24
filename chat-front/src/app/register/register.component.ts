import { Component, OnInit } from '@angular/core';
import { User } from '../model/User';
import { UserServiceService } from '../service/user-service.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  user = new User('', '')

  constructor(private userService: UserServiceService) { }

  ngOnInit(): void {
  }
  submit(){
    this.userService.login(this.user);
  }
}
