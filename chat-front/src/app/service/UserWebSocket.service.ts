import { Injectable } from "@angular/core";
import { map, Subject } from "rxjs";
import { Message } from "../model/Message";
import { User } from "../model/User";
import { UserServiceService } from "./user-service.service";
import { WsService } from "./ws.service";
@Injectable({
  providedIn: 'root',
})

export class UserWebSocketService{
  websocket = "ws://localhost:8080/Chat-war/ws/";

  public activeUsers: Subject<User>;
  public registeredUsers: Subject<User>;
  public messages: Subject<Message>;
  constructor(private wsService : WsService, private userService: UserServiceService){
    this.websocket = this.websocket + userService.getActiveUser();

    this.messages = <Subject<Message>>(
      wsService.connect(this.websocket).pipe(
        map((response : MessageEvent) => {
          let responseString: string = response.data;
          if(responseString.startsWith('LOGIN') || responseString.startsWith('REGISTRATION') || responseString.startsWith('LOGOUT')){
            return;
          }else{
            console.log(response.data);
            let data = JSON.parse(response.data);
            return data;
          }
        }
      )
    )
    );


    this.activeUsers = <Subject<User>>(
      wsService.connect(this.websocket).pipe(
        map((response : MessageEvent) => {
          let responseString: string = response.data;
          let username : string = responseString.split('%')[1];
          if(responseString.startsWith('LOGIN')){
            return new User(username, 'LOGIN');
          }
          else if(responseString.startsWith('LOGOUT')){
            return new User(username, 'LOGOUT');
          }
          else{
            return;
          }
        })
      )

    );

    this.registeredUsers = <Subject<User>>(
      wsService.connect(this.websocket).pipe(
        map((response : MessageEvent) => {
          let responseString: string = response.data;
          let username : string = responseString.split('%')[1];
          if(responseString.startsWith('REGISTRATION')){
            console.log('Do i exist?' + username)
            return new User(username, 'LOGOUT');
          }

          else{
            return;
          }
        })      )
    )

  }


}
