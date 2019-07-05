import {Component, OnInit} from '@angular/core';
import {HogaramaBackendService} from "../../../services/HogaramaBackendService/hogarama-backend.service";
import {UserData} from "../../../shared/model/UserData";
import {log} from "util";

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
    this.rs.getUser().subscribe((userData: UserData) => {
      this.userData = userData;
      log(this.userData)
    });
  }
}
