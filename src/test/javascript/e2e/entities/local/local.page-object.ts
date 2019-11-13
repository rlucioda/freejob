import { element, by, ElementFinder } from 'protractor';

export class LocalComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-local div table .btn-danger'));
  title = element.all(by.css('jhi-local div h2#page-heading span')).first();

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

export class LocalUpdatePage {
  pageTitle = element(by.id('jhi-local-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  nomeLocalInput = element(by.id('field_nomeLocal'));
  tipoLocalSelect = element(by.id('field_tipoLocal'));
  eventoSelect = element(by.id('field_evento'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNomeLocalInput(nomeLocal) {
    await this.nomeLocalInput.sendKeys(nomeLocal);
  }

  async getNomeLocalInput() {
    return await this.nomeLocalInput.getAttribute('value');
  }

  async setTipoLocalSelect(tipoLocal) {
    await this.tipoLocalSelect.sendKeys(tipoLocal);
  }

  async getTipoLocalSelect() {
    return await this.tipoLocalSelect.element(by.css('option:checked')).getText();
  }

  async tipoLocalSelectLastOption() {
    await this.tipoLocalSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async eventoSelectLastOption() {
    await this.eventoSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async eventoSelectOption(option) {
    await this.eventoSelect.sendKeys(option);
  }

  getEventoSelect(): ElementFinder {
    return this.eventoSelect;
  }

  async getEventoSelectedOption() {
    return await this.eventoSelect.element(by.css('option:checked')).getText();
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

export class LocalDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-local-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-local'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
