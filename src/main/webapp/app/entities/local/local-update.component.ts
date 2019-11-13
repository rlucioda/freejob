import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ILocal, Local } from 'app/shared/model/local.model';
import { LocalService } from './local.service';
import { IEvento } from 'app/shared/model/evento.model';
import { EventoService } from 'app/entities/evento/evento.service';

@Component({
  selector: 'jhi-local-update',
  templateUrl: './local-update.component.html'
})
export class LocalUpdateComponent implements OnInit {
  isSaving: boolean;

  eventos: IEvento[];

  editForm = this.fb.group({
    id: [],
    nomeLocal: [null, [Validators.required, Validators.maxLength(50)]],
    tipoLocal: [],
    evento: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected localService: LocalService,
    protected eventoService: EventoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ local }) => {
      this.updateForm(local);
    });
    this.eventoService.query({ 'localId.specified': 'false' }).subscribe(
      (res: HttpResponse<IEvento[]>) => {
        if (!this.editForm.get('evento').value || !this.editForm.get('evento').value.id) {
          this.eventos = res.body;
        } else {
          this.eventoService
            .find(this.editForm.get('evento').value.id)
            .subscribe(
              (subRes: HttpResponse<IEvento>) => (this.eventos = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(local: ILocal) {
    this.editForm.patchValue({
      id: local.id,
      nomeLocal: local.nomeLocal,
      tipoLocal: local.tipoLocal,
      evento: local.evento
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const local = this.createFromForm();
    if (local.id !== undefined) {
      this.subscribeToSaveResponse(this.localService.update(local));
    } else {
      this.subscribeToSaveResponse(this.localService.create(local));
    }
  }

  private createFromForm(): ILocal {
    return {
      ...new Local(),
      id: this.editForm.get(['id']).value,
      nomeLocal: this.editForm.get(['nomeLocal']).value,
      tipoLocal: this.editForm.get(['tipoLocal']).value,
      evento: this.editForm.get(['evento']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocal>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackEventoById(index: number, item: IEvento) {
    return item.id;
  }
}
