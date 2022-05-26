import { Component, OnInit } from '@angular/core';
import { ChatInterfaceComponent } from '../chat-interface/chat-interface.component';
import { User } from '../model/User';
import { UserServiceService } from '../service/user-service.service';
import { UserWebSocketService } from '../service/UserWebSocket.service';

@Component({
  selector: 'app-online-users',
  templateUrl: './online-users.component.html',
  styleUrls: ['./online-users.component.css']
})
export class OnlineUsersComponent implements OnInit {

  users : User[] = [];
  loading : Boolean = true;
  constructor(private userService: UserServiceService, private userWebSocketService : UserWebSocketService) { }

  ngOnInit(): void {
    console.log('Started web socket')
    this.userWebSocketService.activeUsers.subscribe((msg) => {
      if(msg!=undefined){
        if(msg.password == 'LOGIN' && !this.userExists(msg.username)){
          this.users.push(msg);
        }
        else if(msg.password == 'LOGOUT'){
          this.removeUser(msg.username)
        }
      }
  });
    if(ChatInterfaceComponent.hasConnection){
      this.userService.getAllLoggedInUsers().subscribe();
      this.loading = false;
    }else{
      setTimeout(() =>{
        this.userService.getAllLoggedInUsers().subscribe();
        this.loading = false;
      }, 400
      )
    }
  }
  removeUser(username: string): boolean{
    for(let user of this.users){
      if(user.username == username){
        return true;
      }
    }
    return false;

  }
  userExists(username: string): boolean{
    for(let user of this.users){
      if(user.username == username){
        return true;
      }
    }
    return false;
  }

  }



