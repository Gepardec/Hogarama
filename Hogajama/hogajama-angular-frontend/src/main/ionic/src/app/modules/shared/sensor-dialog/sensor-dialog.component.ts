import {Component, Inject, OnInit} from '@angular/core';
import {Sensor} from "../../../shared/models/Sensor";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {HogaramaBackendService} from "../../../services/HogaramaBackendService/hogarama-backend.service";

@Component({
  selector: 'app-sensor-dialog',
  templateUrl: './sensor-dialog.component.html',
  styleUrls: ['./sensor-dialog.component.scss'],
})
export class SensorDialogComponent implements OnInit {

  public sensor: Sensor;
  private isEditAction = false;

  constructor(
      public dialogRef: MatDialogRef<SensorDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: Sensor,
      private backend: HogaramaBackendService
  ) {
    if(data != null) {
      this.isEditAction = true;
      this.sensor = data;
    } else {
      this.sensor = {};
    }
  }

  ngOnInit() {}

  async save() {
    let actionResult;
    if(this.isEditAction) {
      actionResult = await this.backend.sensors.patch(this.sensor.id, this.sensor);
    } else {
      actionResult = await this.backend.sensors.put(this.sensor);
    }
    this.dialogRef.close(actionResult);
  }
}
