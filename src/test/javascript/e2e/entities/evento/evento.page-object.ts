import { element, by, ElementFinder } from 'protractor';

export class EventoComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-evento div table .btn-danger'));
  title = element.all(by.css('jhi-evento div h2#page-heading span')).first();

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

export class EventoUpdatePage {
  pageTitle = element(by.id('jhi-evento-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  nomeEventoInput = element(by.id('field_nomeEvento'));
  descricaEventoInput = element(by.id('field_descricaEvento'));
  dataInicioInput = element(by.id('field_dataInicio'));
  dataFimInput = element(by.id('field_dataFim'));
  statusJobInput = element(by.id('field_statusJob'));
  tipoEventoSelect = element(by.id('field_tipoEvento'));
  jobSelect = element(by.id('field_job'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNomeEventoInput(nomeEvento) {
    await this.nomeEventoInput.sendKeys(nomeEvento);
  }

  async getNomeEventoInput() {
    return await this.nomeEventoInput.getAttribute('value');
  }

  async setDescricaEventoInput(descricaEvento) {
    await this.descricaEventoInput.sendKeys(descricaEvento);
  }

  async getDescricaEventoInput() {
    return await this.descricaEventoInput.getAttribute('value');
  }

  async setDataInicioInput(dataInicio) {
    await this.dataInicioInput.sendKeys(dataInicio);
  }

  async getDataInicioInput() {
    return await this.dataInicioInput.getAttribute('value');
  }

  async setDataFimInput(dataFim) {
    await this.dataFimInput.sendKeys(dataFim);
  }

  async getDataFimInput() {
    return await this.dataFimInput.getAttribute('value');
  }

  getStatusJobInput() {
    return this.statusJobInput;
  }
  async setTipoEventoSelect(tipoEvento) {
    await this.tipoEventoSelect.sendKeys(tipoEvento);
  }

  async getTipoEventoSelect() {
    return await this.tipoEventoSelect.element(by.css('option:checked')).getText();
  }

  async tipoEventoSelectLastOption() {
    await this.tipoEventoSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async jobSelectLastOption() {
    await this.jobSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async jobSelectOption(option) {
    await this.jobSelect.sendKeys(option);
  }

  getJobSelect(): ElementFinder {
    return this.jobSelect;
  }

  async getJobSelectedOption() {
    return await this.jobSelect.element(by.css('option:checked')).getText();
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

export class EventoDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-evento-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-evento'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
