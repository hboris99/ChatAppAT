import { Injectable } from '@angular/core';
import { Observable, Observer, Subject } from 'rxjs';
import { ChatInterfaceComponent } from '../chat-interface/chat-interface.component';
import { RemoteService } from './remote.service';

@Injectable({
  providedIn: 'root'
})
export class WsService {

  constructor(private remoteService: RemoteService) { }

  private subject: Subject<MessageEvent> | undefined;

  public connect(url: string): Subject<MessageEvent>{
    if(!this.subject){
      this.subject = this.create(url);
      setTimeout(() => {
        ChatInterfaceComponent.hasConnection = true;
        console.log('connected succesfully')
      }, 500);

    }
    return this.subject;
  }

  private create(url: string):  Subject<MessageEvent>{
    let webSocket = new WebSocket(url);
    let event = Observable.create((o: Observer<MessageEvent>) => {
      webSocket.onmessage = o.next.bind(o);
      webSocket.onerror = o.error.bind(o);
      webSocket.onclose = o.complete.bind(o);
      return webSocket.close.bind(webSocket);
    });
    let eventWatcher = {
      next: (data: Object) => {
        if(webSocket.readyState === WebSocket.OPEN){
          webSocket.send(JSON.stringify(data));
        }
      },
    };
    return Subject.create(eventWatcher, event);
  }
}
