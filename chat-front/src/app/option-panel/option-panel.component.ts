import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserServiceService } from '../service/user-service.service';

@Component({
  selector: 'app-option-panel',
  templateUrl: './option-panel.component.html',
  styleUrls: ['./option-panel.component.css']
})
export class OptionPanelComponent implements OnInit {

  currentUrl : string = ''
  constructor(private userService : UserServiceService, private router :Router) { }

  username  = this.userService.getActiveUser();
  ngOnInit(): void {
  }

  changePage(){
    this.router.navigate([this.currentUrl]);
  }
}
