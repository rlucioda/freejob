import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EnderecoComponentsPage, EnderecoDeleteDialog, EnderecoUpdatePage } from './endereco.page-object';

const expect = chai.expect;

describe('Endereco e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let enderecoComponentsPage: EnderecoComponentsPage;
  let enderecoUpdatePage: EnderecoUpdatePage;
  let enderecoDeleteDialog: EnderecoDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Enderecos', async () => {
    await navBarPage.goToEntity('endereco');
    enderecoComponentsPage = new EnderecoComponentsPage();
    await browser.wait(ec.visibilityOf(enderecoComponentsPage.title), 5000);
    expect(await enderecoComponentsPage.getTitle()).to.eq('freejobApp.endereco.home.title');
  });

  it('should load create Endereco page', async () => {
    await enderecoComponentsPage.clickOnCreateButton();
    enderecoUpdatePage = new EnderecoUpdatePage();
    expect(await enderecoUpdatePage.getPageTitle()).to.eq('freejobApp.endereco.home.createOrEditLabel');
    await enderecoUpdatePage.cancel();
  });

  it('should create and save Enderecos', async () => {
    const nbButtonsBeforeCreate = await enderecoComponentsPage.countDeleteButtons();

    await enderecoComponentsPage.clickOnCreateButton();
    await promise.all([
      enderecoUpdatePage.setRuaInput('rua'),
      enderecoUpdatePage.setNumeroInput('numero'),
      enderecoUpdatePage.setComplementoInput('complemento'),
      enderecoUpdatePage.setBairroInput('bairro'),
      enderecoUpdatePage.setCepInput('cep'),
      enderecoUpdatePage.setCidadeInput('cidade'),
      enderecoUpdatePage.setEstadoInput('estado'),
      enderecoUpdatePage.localSelectLastOption()
    ]);
    expect(await enderecoUpdatePage.getRuaInput()).to.eq('rua', 'Expected Rua value to be equals to rua');
    expect(await enderecoUpdatePage.getNumeroInput()).to.eq('numero', 'Expected Numero value to be equals to numero');
    expect(await enderecoUpdatePage.getComplementoInput()).to.eq('complemento', 'Expected Complemento value to be equals to complemento');
    expect(await enderecoUpdatePage.getBairroInput()).to.eq('bairro', 'Expected Bairro value to be equals to bairro');
    expect(await enderecoUpdatePage.getCepInput()).to.eq('cep', 'Expected Cep value to be equals to cep');
    expect(await enderecoUpdatePage.getCidadeInput()).to.eq('cidade', 'Expected Cidade value to be equals to cidade');
    expect(await enderecoUpdatePage.getEstadoInput()).to.eq('estado', 'Expected Estado value to be equals to estado');
    await enderecoUpdatePage.save();
    expect(await enderecoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await enderecoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Endereco', async () => {
    const nbButtonsBeforeDelete = await enderecoComponentsPage.countDeleteButtons();
    await enderecoComponentsPage.clickOnLastDeleteButton();

    enderecoDeleteDialog = new EnderecoDeleteDialog();
    expect(await enderecoDeleteDialog.getDialogTitle()).to.eq('freejobApp.endereco.delete.question');
    await enderecoDeleteDialog.clickOnConfirmButton();

    expect(await enderecoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
