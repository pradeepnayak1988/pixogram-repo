import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpErrorResponse, HttpEventType,HttpHeaders } from  '@angular/common/http';
import { map } from  'rxjs/operators';
import { GlobalsService } from './globals.service';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  // API url
  SERVER_URL = "http://localhost:8086/media/singlefileupload/user/19"

 constructor(private httpClient: HttpClient,private globals: GlobalsService) { }
 public jwttoken: string = 'Bearer '+ this.globals.jwttoken;


 public upload(formData) {

const httpHeaders= new HttpHeaders()
  .set('Access-Control-Allow-Origin', '*');
  //.set('Authorization',this.jwttoken);
   let options = {headers: httpHeaders};
console.log(formData)
  /* this.httpClient.post(this.SERVER_URL,
           formData,options).subscribe({
                             next: data => {
                                console.log(data)
                              },
                             error: error => {
                                console.error('There was an error!', error);
                                alert("Invalid File")
                             }
                         }); */

  	  return this.httpClient.post<any>(this.SERVER_URL, formData, {
       reportProgress: true,
       observe: 'events'

     });
 }


}
