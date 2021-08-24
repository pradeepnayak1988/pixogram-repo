import { Component, OnInit, ViewChild, ElementRef,Input  } from '@angular/core';
import { HttpEventType, HttpErrorResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { FileUploadService } from '../file-upload.service';
import { GlobalsService } from '../globals.service';



@Component({
  selector: 'app-uploadmedia',
  templateUrl: './uploadmedia.component.html',
  styleUrls: ['./uploadmedia.component.css']
})
export class UploadmediaComponent  {
  public username: string = this.globals.username;
  title: string = null;
  desc: string = null;
  tags: string = null;
  effect:string=null
 @ViewChild("fileUpload", {static: false}) fileUpload: ElementRef;files  = [];
 // Inject service
 constructor(private uploadService: FileUploadService,private globals: GlobalsService) { }
 ngOnInit(): void {  }

 uploadFile(file) {

     const formData = new FormData();
     console.log(file.data)
     formData.append('file', file.data);
     formData.append('title',this.title);
     formData.append('desc', this.desc);
     formData.append('tags', this.tags);
     formData.append('effect',this.effect);
     formData.append('userId', '1');
     file.inProgress = true;
     this.uploadService.upload(formData).pipe(
       map(event => {
         switch (event.type) {
           case HttpEventType.UploadProgress:
             file.progress = Math.round(event.loaded * 100 / event.total);
             break;
           case HttpEventType.Response:
             return event;
         }
       }),
       catchError((error: HttpErrorResponse) => {
         file.inProgress = false;
         return of(`${file.data.name} upload failed.`);
       })).subscribe((event: any) => {
         if (typeof (event) === 'object') {
           console.log(event.body);
         }
       });
   }
private uploadFiles() {
    this.fileUpload.nativeElement.value = '';
    this.files.forEach(file => {
      this.uploadFile(file);
    });
}
onClick() {
    const fileUpload = this.fileUpload.nativeElement;fileUpload.onchange = () => {
    for (let index = 0; index < fileUpload.files.length; index++)
    {
     const file = fileUpload.files[index];
     console.log(file);
     this.files.push({ data: file, inProgress: false, progress: 0});
    }
      this.uploadFiles();
    };
    fileUpload.click();
}



}
