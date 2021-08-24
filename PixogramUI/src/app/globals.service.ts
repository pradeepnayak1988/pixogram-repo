import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GlobalsService {

  constructor() { }

  public jwttoken: string=null;
  public username: string=null;
  public media: any=null;

  setToken(token:string){
      this.jwttoken = token;
        }
  setUsername(username:string){
      this.username = username;
         }
  setMedia(media:any){
        this.media = media;
           }
}


