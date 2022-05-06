import {Component, OnInit} from '@angular/core';
import {log} from "util";
import { UserData } from 'src/app/shared/models/UserData';
import { HogaramaBackendService } from 'src/app/services/HogaramaBackendService/hogarama-backend.service';

@Component({
  selector: 'app-user-name',
  templateUrl: './user-name.component.html',
  styleUrls: ['./user-name.component.scss']
})
export class UserNameComponent implements OnInit {

  userData: UserData;

  constructor(private rs: HogaramaBackendService) {
  }

  ngOnInit() {
    this.rs.users.getByBearer().then((userData: UserData) => {
      this.userData = userData;
      console.log(this.userData);
    });
  }
}
