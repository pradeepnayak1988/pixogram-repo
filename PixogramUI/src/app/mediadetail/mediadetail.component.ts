import { Component, OnInit } from '@angular/core';
import { GlobalsService } from '../globals.service';

@Component({
  selector: 'app-mediadetail',
  templateUrl: './mediadetail.component.html',
  styleUrls: ['./mediadetail.component.css']
})
export class MediadetailComponent implements OnInit {
public username: string = this.globals.username;
public media: any = this.globals.media;

constructor(private globals: GlobalsService) { }

  ngOnInit(): void {
  }

}
