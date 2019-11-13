import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IEvento, Evento } from 'app/shared/model/evento.model';
import { EventoService } from './evento.service';
import { IJob } from 'app/shared/model/job.model';
import { JobService } from 'app/entities/job/job.service';

@Component({
  selector: 'jhi-evento-update',
  templateUrl: './evento-update.component.html'
})
export class EventoUpdateComponent implements OnInit {
  isSaving: boolean;

  jobs: IJob[];

  editForm = this.fb.group({
    id: [],
    nomeEvento: [null, [Validators.required, Validators.maxLength(50)]],
    descricaEvento: [],
    dataInicio: [null, [Validators.required]],
    dataFim: [null, [Validators.required]],
    statusJob: [],
    tipoEvento: [],
    job: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected eventoService: EventoService,
    protected jobService: JobService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ evento }) => {
      this.updateForm(evento);
    });
    this.jobService.query({ 'eventoId.specified': 'false' }).subscribe(
      (res: HttpResponse<IJob[]>) => {
        if (!this.editForm.get('job').value || !this.editForm.get('job').value.id) {
          this.jobs = res.body;
        } else {
          this.jobService
            .find(this.editForm.get('job').value.id)
            .subscribe(
              (subRes: HttpResponse<IJob>) => (this.jobs = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(evento: IEvento) {
    this.editForm.patchValue({
      id: evento.id,
      nomeEvento: evento.nomeEvento,
      descricaEvento: evento.descricaEvento,
      dataInicio: evento.dataInicio != null ? evento.dataInicio.format(DATE_TIME_FORMAT) : null,
      dataFim: evento.dataFim != null ? evento.dataFim.format(DATE_TIME_FORMAT) : null,
      statusJob: evento.statusJob,
      tipoEvento: evento.tipoEvento,
      job: evento.job
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const evento = this.createFromForm();
    if (evento.id !== undefined) {
      this.subscribeToSaveResponse(this.eventoService.update(evento));
    } else {
      this.subscribeToSaveResponse(this.eventoService.create(evento));
    }
  }

  private createFromForm(): IEvento {
    return {
      ...new Evento(),
      id: this.editForm.get(['id']).value,
      nomeEvento: this.editForm.get(['nomeEvento']).value,
      descricaEvento: this.editForm.get(['descricaEvento']).value,
      dataInicio:
        this.editForm.get(['dataInicio']).value != null ? moment(this.editForm.get(['dataInicio']).value, DATE_TIME_FORMAT) : undefined,
      dataFim: this.editForm.get(['dataFim']).value != null ? moment(this.editForm.get(['dataFim']).value, DATE_TIME_FORMAT) : undefined,
      statusJob: this.editForm.get(['statusJob']).value,
      tipoEvento: this.editForm.get(['tipoEvento']).value,
      job: this.editForm.get(['job']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvento>>) {
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

  trackJobById(index: number, item: IJob) {
    return item.id;
  }
}
