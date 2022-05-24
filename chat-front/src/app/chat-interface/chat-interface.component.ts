import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-chat-interface',
  templateUrl: './chat-interface.component.html',
  styleUrls: ['./chat-interface.component.css']
})
export class ChatInterfaceComponent implements OnInit {
  public static hasConnection: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

}
