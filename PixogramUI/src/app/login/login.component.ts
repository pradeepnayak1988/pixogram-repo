import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog'
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { GlobalsService } from '../globals.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {


  constructor(private router: Router,private httpObj: HttpClient,private globals: GlobalsService) { }


username: string='';
password: string='';

  ngOnInit(): void {
  }


 login() : void {
     this.globals.username=this.username;
     let httpHeaders = new HttpHeaders({'Content-Type' : 'application/json','Cache-Control': 'no-cache','Access-Control-Allow-Origin' :'*'});
     let options = {headers: httpHeaders};
         this.httpObj.post("http://localhost:8086/authenticate",
         {
           "username" : this.username,
           "password" : this.password
         },options).subscribe({
                           next: data => {

                              //this.globals.jwttoken=data.jwt
                               //var info=JSON.parse(JSON.stringify(data));
                               //this.globals.jwttoken = info.jwt;
                               //console.log(this.globals.jwttoken)
                              this.router.navigate(["mymedia"]);
                           },
                           error: error => {
                              console.error('There was an error!', error);
                              alert("Invalid Credentials!")

                           }
                       });




}
 register() : void {
      this.router.navigate(["register"]);
   }
}
