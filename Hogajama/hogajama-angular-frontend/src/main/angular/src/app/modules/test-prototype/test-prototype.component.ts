import { Component, OnInit } from '@angular/core';
import { HogaramaBackendService } from 'src/app/services/HogaramaBackendService/hogarama-backend.service';

@Component({
  selector: 'app-test-prototype',
  templateUrl: './test-prototype.component.html',
  styleUrls: ['./test-prototype.component.scss']
})
export class TestPrototypeComponent implements OnInit {
  sensorArr: string[];
  error: string;

  constructor(private rs: HogaramaBackendService) { }

  ngOnInit() {
    const sub = this.rs.getAllSensors().subscribe((sensorData: string[]) => {
      this.sensorArr = sensorData;
      sub.unsubscribe();
    }, (error) => {
      console.error(error);
      this.error = error.statusText;
    });
  }

}
