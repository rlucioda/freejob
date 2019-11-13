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
import { IJob, Job } from 'app/shared/model/job.model';
import { JobService } from './job.service';
import { IAgencia } from 'app/shared/model/agencia.model';
import { AgenciaService } from 'app/entities/agencia/agencia.service';

@Component({
  selector: 'jhi-job-update',
  templateUrl: './job-update.component.html'
})
export class JobUpdateComponent implements OnInit {
  isSaving: boolean;

  agencias: IAgencia[];

  editForm = this.fb.group({
    id: [],
    nomeJob: [null, [Validators.required, Validators.maxLength(100)]],
    valorHora: [],
    tempoEvento: [],
    observacao: [],
    dataPagamento: [],
    tipoPagamento: [],
    statusPagamento: [],
    agencia: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected jobService: JobService,
    protected agenciaService: AgenciaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ job }) => {
      this.updateForm(job);
    });
    this.agenciaService
      .query()
      .subscribe((res: HttpResponse<IAgencia[]>) => (this.agencias = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(job: IJob) {
    this.editForm.patchValue({
      id: job.id,
      nomeJob: job.nomeJob,
      valorHora: job.valorHora,
      tempoEvento: job.tempoEvento,
      observacao: job.observacao,
      dataPagamento: job.dataPagamento != null ? job.dataPagamento.format(DATE_TIME_FORMAT) : null,
      tipoPagamento: job.tipoPagamento,
      statusPagamento: job.statusPagamento,
      agencia: job.agencia
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const job = this.createFromForm();
    if (job.id !== undefined) {
      this.subscribeToSaveResponse(this.jobService.update(job));
    } else {
      this.subscribeToSaveResponse(this.jobService.create(job));
    }
  }

  private createFromForm(): IJob {
    return {
      ...new Job(),
      id: this.editForm.get(['id']).value,
      nomeJob: this.editForm.get(['nomeJob']).value,
      valorHora: this.editForm.get(['valorHora']).value,
      tempoEvento: this.editForm.get(['tempoEvento']).value,
      observacao: this.editForm.get(['observacao']).value,
      dataPagamento:
        this.editForm.get(['dataPagamento']).value != null
          ? moment(this.editForm.get(['dataPagamento']).value, DATE_TIME_FORMAT)
          : undefined,
      tipoPagamento: this.editForm.get(['tipoPagamento']).value,
      statusPagamento: this.editForm.get(['statusPagamento']).value,
      agencia: this.editForm.get(['agencia']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJob>>) {
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

  trackAgenciaById(index: number, item: IAgencia) {
    return item.id;
  }
}
