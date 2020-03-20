import { Component, OnInit, HostBinding } from '@angular/core';

@Component({
  selector: 'app-side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.scss'],
})
export class SideMenuComponent implements OnInit {
  
  @HostBinding('class.open') isOpen: boolean = false;

  constructor() { }

  ngOnInit() {}

  switchMenu() {
    this.isOpen = ! this.isOpen;
  }
}
