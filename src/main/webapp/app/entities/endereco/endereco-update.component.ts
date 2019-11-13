import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IEndereco, Endereco } from 'app/shared/model/endereco.model';
import { EnderecoService } from './endereco.service';
import { ILocal } from 'app/shared/model/local.model';
import { LocalService } from 'app/entities/local/local.service';

@Component({
  selector: 'jhi-endereco-update',
  templateUrl: './endereco-update.component.html'
})
export class EnderecoUpdateComponent implements OnInit {
  isSaving: boolean;

  locals: ILocal[];

  editForm = this.fb.group({
    id: [],
    rua: [null, [Validators.required]],
    numero: [],
    complemento: [],
    bairro: [],
    cep: [],
    cidade: [],
    estado: [],
    local: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected enderecoService: EnderecoService,
    protected localService: LocalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ endereco }) => {
      this.updateForm(endereco);
    });
    this.localService.query({ 'enderecoId.specified': 'false' }).subscribe(
      (res: HttpResponse<ILocal[]>) => {
        if (!this.editForm.get('local').value || !this.editForm.get('local').value.id) {
          this.locals = res.body;
        } else {
          this.localService
            .find(this.editForm.get('local').value.id)
            .subscribe(
              (subRes: HttpResponse<ILocal>) => (this.locals = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(endereco: IEndereco) {
    this.editForm.patchValue({
      id: endereco.id,
      rua: endereco.rua,
      numero: endereco.numero,
      complemento: endereco.complemento,
      bairro: endereco.bairro,
      cep: endereco.cep,
      cidade: endereco.cidade,
      estado: endereco.estado,
      local: endereco.local
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const endereco = this.createFromForm();
    if (endereco.id !== undefined) {
      this.subscribeToSaveResponse(this.enderecoService.update(endereco));
    } else {
      this.subscribeToSaveResponse(this.enderecoService.create(endereco));
    }
  }

  private createFromForm(): IEndereco {
    return {
      ...new Endereco(),
      id: this.editForm.get(['id']).value,
      rua: this.editForm.get(['rua']).value,
      numero: this.editForm.get(['numero']).value,
      complemento: this.editForm.get(['complemento']).value,
      bairro: this.editForm.get(['bairro']).value,
      cep: this.editForm.get(['cep']).value,
      cidade: this.editForm.get(['cidade']).value,
      estado: this.editForm.get(['estado']).value,
      local: this.editForm.get(['local']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEndereco>>) {
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

  trackLocalById(index: number, item: ILocal) {
    return item.id;
  }
}
