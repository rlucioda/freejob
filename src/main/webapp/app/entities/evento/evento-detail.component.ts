import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEvento } from 'app/shared/model/evento.model';

@Component({
  selector: 'jhi-evento-detail',
  templateUrl: './evento-detail.component.html'
})
export class EventoDetailComponent implements OnInit {
  evento: IEvento;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ evento }) => {
      this.evento = evento;
    });
  }

  previousState() {
    window.history.back();
  }
}
