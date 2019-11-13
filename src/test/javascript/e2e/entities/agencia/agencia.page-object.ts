import { element, by, ElementFinder } from 'protractor';

export class AgenciaComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-agencia div table .btn-danger'));
  title = element.all(by.css('jhi-agencia div h2#page-heading span')).first();

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

export class AgenciaUpdatePage {
  pageTitle = element(by.id('jhi-agencia-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  nomeAgenciaInput = element(by.id('field_nomeAgencia'));
  contatoAgenciaInput = element(by.id('field_contatoAgencia'));
  emailInput = element(by.id('field_email'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNomeAgenciaInput(nomeAgencia) {
    await this.nomeAgenciaInput.sendKeys(nomeAgencia);
  }

  async getNomeAgenciaInput() {
    return await this.nomeAgenciaInput.getAttribute('value');
  }

  async setContatoAgenciaInput(contatoAgencia) {
    await this.contatoAgenciaInput.sendKeys(contatoAgencia);
  }

  async getContatoAgenciaInput() {
    return await this.contatoAgenciaInput.getAttribute('value');
  }

  async setEmailInput(email) {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput() {
    return await this.emailInput.getAttribute('value');
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

export class AgenciaDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-agencia-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-agencia'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
