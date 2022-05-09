import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HogaramaBackendService} from '../../../services/HogaramaBackendService/hogarama-backend.service';
import {Actor} from '../../../shared/models/Actor';

@Component({
  selector: 'app-actor-dialog',
  templateUrl: './actor-dialog.component.html',
  styleUrls: ['./actor-dialog.component.scss'],
})
export class ActorDialogComponent implements OnInit {

  public actor: Actor;
  public isEditAction = false;

  constructor(
    public dialogRef: MatDialogRef<ActorDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Actor,
    private backend: HogaramaBackendService
  ) {
    if (data != null) {
      this.isEditAction = true;
      this.actor = data;
    } else {
      this.actor = {};
    }
  }

  ngOnInit() {}

  async save() {
    let actionResult;
    if (this.isEditAction) {
      actionResult = await this.backend.actors.patch(this.actor.id, this.actor);
    } else {
      actionResult = await this.backend.actors.put(this.actor);
    }
    this.dialogRef.close(actionResult);
  }
}
