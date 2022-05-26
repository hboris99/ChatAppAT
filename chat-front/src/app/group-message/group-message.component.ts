import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ChatInterfaceComponent } from '../chat-interface/chat-interface.component';
import { Message } from '../model/Message';
import { User } from '../model/User';
import { UserServiceService } from '../service/user-service.service';
import { UserWebSocketService } from '../service/UserWebSocket.service';

@Component({
  selector: 'app-group-message',
  templateUrl: './group-message.component.html',
  styleUrls: ['./group-message.component.css']
})
export class GroupMessageComponent implements OnInit {

  messageForm = new FormGroup({});
  constructor(private userService: UserServiceService) {
    this.messageForm = new FormGroup({
      subject: new FormControl(null),
      content : new FormControl(null)

    })

   }

  ngOnInit(): void {


  }



  send(){
    console.log('Sending message')
    let message = new Message(
      '',
      this.userService.getActiveUser()!,
      new Date(),
      this.messageForm.controls['subject'].value,
      this.messageForm.controls['content'].value

    );
    this.userService.sendMessageToEveryoneActive(message).subscribe((data) =>{
      this.messageForm.controls['subject'].markAsPristine();
      this.messageForm.controls['content'].markAsPristine();
      alert('Group Message sent!');
    })
  }
}
