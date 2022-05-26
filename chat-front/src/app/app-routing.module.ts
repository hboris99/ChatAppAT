import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChatInterfaceComponent } from './chat-interface/chat-interface.component';
import { LogInComponent } from './log-in/log-in.component';
import { NewmessageComponent } from './newmessage/newmessage.component';
import { OnlineUsersComponent } from './online-users/online-users.component';
import { RegisterComponent } from './register/register.component';
import { RegisteredUsersComponent } from './registered-users/registered-users.component';

const routes: Routes = [
  {path: 'register', component: RegisterComponent},
  {path: 'login', component:LogInComponent},
  {path: 'newmessage', component: NewmessageComponent},
  {path: 'chatui', component: ChatInterfaceComponent},
  {path: 'activeUsers', component: OnlineUsersComponent},
  {path: 'registeredUsers', component:RegisteredUsersComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
