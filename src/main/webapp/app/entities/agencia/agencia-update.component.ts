import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IAgencia, Agencia } from 'app/shared/model/agencia.model';
import { AgenciaService } from './agencia.service';

@Component({
  selector: 'jhi-agencia-update',
  templateUrl: './agencia-update.component.html'
})
export class AgenciaUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    nomeAgencia: [null, [Validators.required, Validators.maxLength(50)]],
    contatoAgencia: [],
    email: []
  });

  constructor(protected agenciaService: AgenciaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ agencia }) => {
      this.updateForm(agencia);
    });
  }

  updateForm(agencia: IAgencia) {
    this.editForm.patchValue({
      id: agencia.id,
      nomeAgencia: agencia.nomeAgencia,
      contatoAgencia: agencia.contatoAgencia,
      email: agencia.email
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const agencia = this.createFromForm();
    if (agencia.id !== undefined) {
      this.subscribeToSaveResponse(this.agenciaService.update(agencia));
    } else {
      this.subscribeToSaveResponse(this.agenciaService.create(agencia));
    }
  }

  private createFromForm(): IAgencia {
    return {
      ...new Agencia(),
      id: this.editForm.get(['id']).value,
      nomeAgencia: this.editForm.get(['nomeAgencia']).value,
      contatoAgencia: this.editForm.get(['contatoAgencia']).value,
      email: this.editForm.get(['email']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgencia>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
