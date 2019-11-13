import { element, by, ElementFinder } from 'protractor';

export class JobComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-job div table .btn-danger'));
  title = element.all(by.css('jhi-job div h2#page-heading span')).first();

  async clickOnCreateButton() {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton() {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class JobUpdatePage {
  pageTitle = element(by.id('jhi-job-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  nomeJobInput = element(by.id('field_nomeJob'));
  valorHoraInput = element(by.id('field_valorHora'));
  tempoEventoInput = element(by.id('field_tempoEvento'));
  observacaoInput = element(by.id('field_observacao'));
  dataPagamentoInput = element(by.id('field_dataPagamento'));
  tipoPagamentoSelect = element(by.id('field_tipoPagamento'));
  statusPagamentoInput = element(by.id('field_statusPagamento'));
  agenciaSelect = element(by.id('field_agencia'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNomeJobInput(nomeJob) {
    await this.nomeJobInput.sendKeys(nomeJob);
  }

  async getNomeJobInput() {
    return await this.nomeJobInput.getAttribute('value');
  }

  async setValorHoraInput(valorHora) {
    await this.valorHoraInput.sendKeys(valorHora);
  }

  async getValorHoraInput() {
    return await this.valorHoraInput.getAttribute('value');
  }

  async setTempoEventoInput(tempoEvento) {
    await this.tempoEventoInput.sendKeys(tempoEvento);
  }

  async getTempoEventoInput() {
    return await this.tempoEventoInput.getAttribute('value');
  }

  async setObservacaoInput(observacao) {
    await this.observacaoInput.sendKeys(observacao);
  }

  async getObservacaoInput() {
    return await this.observacaoInput.getAttribute('value');
  }

  async setDataPagamentoInput(dataPagamento) {
    await this.dataPagamentoInput.sendKeys(dataPagamento);
  }

  async getDataPagamentoInput() {
    return await this.dataPagamentoInput.getAttribute('value');
  }

  async setTipoPagamentoSelect(tipoPagamento) {
    await this.tipoPagamentoSelect.sendKeys(tipoPagamento);
  }

  async getTipoPagamentoSelect() {
    return await this.tipoPagamentoSelect.element(by.css('option:checked')).getText();
  }

  async tipoPagamentoSelectLastOption() {
    await this.tipoPagamentoSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  getStatusPagamentoInput() {
    return this.statusPagamentoInput;
  }

  async agenciaSelectLastOption() {
    await this.agenciaSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async agenciaSelectOption(option) {
    await this.agenciaSelect.sendKeys(option);
  }

  getAgenciaSelect(): ElementFinder {
    return this.agenciaSelect;
  }

  async getAgenciaSelectedOption() {
    return await this.agenciaSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class JobDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-job-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-job'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
