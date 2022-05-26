import { Component, OnInit } from '@angular/core';
import { ChatInterfaceComponent } from '../chat-interface/chat-interface.component';
import { User } from '../model/User';
import { UserServiceService } from '../service/user-service.service';
import { UserWebSocketService } from '../service/UserWebSocket.service';

@Component({
  selector: 'app-registered-users',
  templateUrl: './registered-users.component.html',
  styleUrls: ['./registered-users.component.css']
})
export class RegisteredUsersComponent implements OnInit {

  users : User[] = [];
  loading : Boolean = true;
  constructor(private userService: UserServiceService, private userWebSocketService : UserWebSocketService) { }

  ngOnInit(): void {
    console.log('Started web socket')
    this.userWebSocketService.registeredUsers.subscribe((msg) => {
      console.log(msg)
      if(msg!=undefined){
        console.log(msg)
        this.users.push(msg);
      }
  });
    if(ChatInterfaceComponent.hasConnection){
      this.userService.getAllRegisteredUsers().subscribe();
      this.loading = false;
    }else{
      setTimeout(() =>{
        this.userService.getAllRegisteredUsers().subscribe();
        this.loading = false;
      }, 400
      )
    }
  }


}
