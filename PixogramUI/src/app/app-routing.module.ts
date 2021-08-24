import { NgModule } from '@angular/core';
import {RouterModule, Routes } from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {MymediaComponent} from './mymedia/mymedia.component';
import {UploadmediaComponent} from './uploadmedia/uploadmedia.component';
import {MediadetailComponent} from './mediadetail/mediadetail.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'mymedia', component: MymediaComponent },
  { path: 'uploadmedia', component: UploadmediaComponent },
  { path: 'mediadetail', component: MediadetailComponent },
  {path : '', component : LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
