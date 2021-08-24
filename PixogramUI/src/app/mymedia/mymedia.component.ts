import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpEvent, HttpErrorResponse, HttpEventType,HttpHeaders } from  '@angular/common/http';
import { GlobalsService } from '../globals.service';
import { MatCarousel, MatCarouselComponent } from '@ngmodule/material-carousel';
import {Router} from '@angular/router';

@Component({
  selector: 'app-mymedia',
  templateUrl: './mymedia.component.html',
  styleUrls: ['./mymedia.component.css']
})


export class MymediaComponent implements OnInit {

// API url
  SERVER_URL = "http://localhost:8086/media/user/19"
  public jwttoken: string = 'Bearer '+ this.globals.jwttoken;
   public username: string = this.globals.username;
   media: any = [];
  constructor(private httpClient: HttpClient,private globals: GlobalsService,private router: Router) { }


  ngOnInit(): void {
  console.log(this.jwttoken);
  const httpHeaders= new HttpHeaders()
    .set('Access-Control-Allow-Origin', '*');
    //.set('Authorization',this.jwttoken);
     let options = {headers: httpHeaders};

    this.httpClient.get(this.SERVER_URL,
             options).subscribe({
                               next: data => {
                                  console.log(data)
                                  this.media=data

                                },
                               error: error => {
                                  console.error('There was an error!', error);
                                  alert("Invalid Read")
                               }
                           });
  }
onClick(media) {
console.log(media);
this.globals.media=media;
this.router.navigate(["mediadetail"]);
}

}
