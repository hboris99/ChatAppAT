import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RemoteService {
    private actionSource = new Subject<boolean>();

    getConnection$ = this.actionSource.asObservable();

  notifyConnection(){
    this.actionSource.next(true);
    console.log('Connection works');
  }

}
