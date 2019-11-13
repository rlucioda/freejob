import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILocal } from 'app/shared/model/local.model';

@Component({
  selector: 'jhi-local-detail',
  templateUrl: './local-detail.component.html'
})
export class LocalDetailComponent implements OnInit {
  local: ILocal;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ local }) => {
      this.local = local;
    });
  }

  previousState() {
    window.history.back();
  }
}
