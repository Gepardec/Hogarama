import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {HogaramaBackendService} from '../../../services/HogaramaBackendService/hogarama-backend.service';
import {Unit} from '../../../shared/models/Unit';

@Component({
  selector: 'app-unit-dialog',
  templateUrl: './unit-dialog.component.html',
  styleUrls: ['./unit-dialog.component.scss'],
})
export class UnitDialogComponent implements OnInit {

  public unit: Unit;
  public isEditAction = false;

  constructor(
      public dialogRef: MatDialogRef<UnitDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: Unit,
      private backend: HogaramaBackendService
  ) {
    if (data != null) {
      this.isEditAction = true;
      this.unit = data;
    } else {
      this.unit = {};
    }
  }

  ngOnInit() {}

  async save() {
    let actionResult;
    if (this.isEditAction) {
      actionResult = await this.backend.units.patch(this.unit.id, this.unit);
    } else {
      actionResult = await this.backend.units.put(this.unit);
    }
    this.dialogRef.close(actionResult);
  }
}
