import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChatInterfaceComponent } from './chat-interface/chat-interface.component';
import { LogInComponent } from './log-in/log-in.component';
import { NewmessageComponent } from './newmessage/newmessage.component';
import { RegisterComponent } from './register/register.component';

const routes: Routes = [
  {path: 'register', component: RegisterComponent},
  {path: 'login', component:LogInComponent},
  {path: 'newmessage', component: NewmessageComponent},
  {path: 'chatui', component: ChatInterfaceComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
