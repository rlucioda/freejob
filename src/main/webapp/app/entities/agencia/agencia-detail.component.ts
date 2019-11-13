import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAgencia } from 'app/shared/model/agencia.model';

@Component({
  selector: 'jhi-agencia-detail',
  templateUrl: './agencia-detail.component.html'
})
export class AgenciaDetailComponent implements OnInit {
  agencia: IAgencia;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ agencia }) => {
      this.agencia = agencia;
    });
  }

  previousState() {
    window.history.back();
  }
}
