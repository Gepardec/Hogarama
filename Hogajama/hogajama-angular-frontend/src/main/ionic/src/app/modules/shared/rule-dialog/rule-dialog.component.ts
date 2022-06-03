import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HogaramaBackendService} from '../../../services/HogaramaBackendService/hogarama-backend.service';
import {Rule} from '../../../shared/models/Rule';

@Component({
  selector: 'app-rule-dialog',
  templateUrl: './rule-dialog.component.html',
  styleUrls: ['./rule-dialog.component.scss'],
})
export class RuleDialogComponent implements OnInit {

  public rule: Rule;
  public isEditAction = false;

  constructor(
    public dialogRef: MatDialogRef<RuleDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Rule,
    private backend: HogaramaBackendService
  ) {
    if (data != null) {
      this.isEditAction = true;
      this.rule = data;
    } else {
      this.rule = {};
    }
  }

  ngOnInit() {}

  async save() {
    let actionResult;
    if (this.isEditAction) {
      actionResult = await this.backend.rules.patch(this.rule.id, this.rule);
    } else {
      actionResult = await this.backend.rules.put(this.rule);
    }
    this.dialogRef.close(actionResult);
  }
}
