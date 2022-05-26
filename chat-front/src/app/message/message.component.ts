import { Component, OnInit } from '@angular/core';
import { ChatInterfaceComponent } from '../chat-interface/chat-interface.component';
import { Message } from '../model/Message';
import { UserServiceService } from '../service/user-service.service';
import { UserWebSocketService } from '../service/UserWebSocket.service';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit {

  messages: Message [] = []
  constructor(private userService : UserServiceService, private userWebSocketService : UserWebSocketService) { }

  ngOnInit(): void {
    this.userWebSocketService.messages.subscribe((message) =>{
      if(message != undefined){
        this.messages.push(message);
      }
    });
    if(ChatInterfaceComponent.hasConnection){
      this.userService.getUserMessages().subscribe();
    }else{
      setTimeout(() => {
        this.userService.getUserMessages().subscribe();
      }, 400);
    }
  }

}
