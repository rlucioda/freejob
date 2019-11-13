import { element, by, ElementFinder } from 'protractor';

export class EnderecoComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-endereco div table .btn-danger'));
  title = element.all(by.css('jhi-endereco div h2#page-heading span')).first();

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

export class EnderecoUpdatePage {
  pageTitle = element(by.id('jhi-endereco-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  ruaInput = element(by.id('field_rua'));
  numeroInput = element(by.id('field_numero'));
  complementoInput = element(by.id('field_complemento'));
  bairroInput = element(by.id('field_bairro'));
  cepInput = element(by.id('field_cep'));
  cidadeInput = element(by.id('field_cidade'));
  estadoInput = element(by.id('field_estado'));
  localSelect = element(by.id('field_local'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setRuaInput(rua) {
    await this.ruaInput.sendKeys(rua);
  }

  async getRuaInput() {
    return await this.ruaInput.getAttribute('value');
  }

  async setNumeroInput(numero) {
    await this.numeroInput.sendKeys(numero);
  }

  async getNumeroInput() {
    return await this.numeroInput.getAttribute('value');
  }

  async setComplementoInput(complemento) {
    await this.complementoInput.sendKeys(complemento);
  }

  async getComplementoInput() {
    return await this.complementoInput.getAttribute('value');
  }

  async setBairroInput(bairro) {
    await this.bairroInput.sendKeys(bairro);
  }

  async getBairroInput() {
    return await this.bairroInput.getAttribute('value');
  }

  async setCepInput(cep) {
    await this.cepInput.sendKeys(cep);
  }

  async getCepInput() {
    return await this.cepInput.getAttribute('value');
  }

  async setCidadeInput(cidade) {
    await this.cidadeInput.sendKeys(cidade);
  }

  async getCidadeInput() {
    return await this.cidadeInput.getAttribute('value');
  }

  async setEstadoInput(estado) {
    await this.estadoInput.sendKeys(estado);
  }

  async getEstadoInput() {
    return await this.estadoInput.getAttribute('value');
  }

  async localSelectLastOption() {
    await this.localSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async localSelectOption(option) {
    await this.localSelect.sendKeys(option);
  }

  getLocalSelect(): ElementFinder {
    return this.localSelect;
  }

  async getLocalSelectedOption() {
    return await this.localSelect.element(by.css('option:checked')).getText();
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

export class EnderecoDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-endereco-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-endereco'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
