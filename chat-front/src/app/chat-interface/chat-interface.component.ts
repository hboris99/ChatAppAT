import { Component, OnInit } from '@angular/core';
import { UserServiceService } from '../service/user-service.service';

@Component({
  selector: 'app-chat-interface',
  templateUrl: './chat-interface.component.html',
  styleUrls: ['./chat-interface.component.css']
})
export class ChatInterfaceComponent implements OnInit {
  public static hasConnection: boolean = false;

  username  = this.userService.getActiveUser();
  constructor(private userService: UserServiceService) { }

  ngOnInit(): void {
  }

}
