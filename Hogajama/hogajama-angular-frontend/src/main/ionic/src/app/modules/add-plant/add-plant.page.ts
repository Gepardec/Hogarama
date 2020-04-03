import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-plant',
  templateUrl: './add-plant.page.html',
  styleUrls: ['./add-plant.page.scss'],
})
export class AddPlantPage implements OnInit {

  constructor(
    private router: Router
  ) { }

  ngOnInit() {
  }

  nextStep() {
    this.router.navigateByUrl('config-start-info');
  }
}
