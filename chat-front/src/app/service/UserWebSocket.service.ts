import { Injectable } from "@angular/core";
import { map, Subject } from "rxjs";
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
  constructor(private wsService : WsService, private userService: UserServiceService){
    this.websocket = this.websocket + userService.getActiveUser();

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
