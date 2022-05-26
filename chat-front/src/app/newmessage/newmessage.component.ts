import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ChatInterfaceComponent } from '../chat-interface/chat-interface.component';
import { Message } from '../model/Message';
import { User } from '../model/User';
import { UserServiceService } from '../service/user-service.service';
import { UserWebSocketService } from '../service/UserWebSocket.service';

@Component({
  selector: 'app-newmessage',
  templateUrl: './newmessage.component.html',
  styleUrls: ['./newmessage.component.css']
})
export class NewmessageComponent implements OnInit {

  messageForm = new FormGroup({});
  users : User[] = []
  constructor(private userService: UserServiceService, private userWebSocketService: UserWebSocketService) {
    this.messageForm = new FormGroup({
      recipient : new FormControl(null),
      subject: new FormControl(null),
      content : new FormControl(null)

    })

   }

  ngOnInit(): void {
    this.userWebSocketService.activeUsers.subscribe((user) =>{
      if(user!= undefined){
        if(user.password == 'LOGIN' && sessionStorage.getItem('username') != user.username &&  !this.userExists(user.username)){
          this.users.push(user);
        }else if(user.password == 'LOGOUT'){
          this.removeUser(user.username);
        }
      }
    });
    if (ChatInterfaceComponent.hasConnection) {
      this.userService.getAllLoggedInUsers().subscribe();
    } else {
      setTimeout(() => {
        this.userService.getAllLoggedInUsers().subscribe();
      }, 400);
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
  onChange(){
    console.log(this.messageForm.controls['recipient'].value);
  }
  send(){
    console.log('Sending message')
    let message = new Message(
      this.messageForm.controls['recipient'].value,
      this.userService.getActiveUser()!,
      new Date(),
      this.messageForm.controls['subject'].value,
      this.messageForm.controls['content'].value

    );
    this.userService.sendMessage(message).subscribe((data) =>{
      this.messageForm.controls['recipient'].setValue(this.users[0].username);
      this.messageForm.controls['subject'].markAsPristine();
      this.messageForm.controls['content'].markAsPristine();
      alert('Message sent!');
    })
  }
}
