import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AgenciaComponentsPage, AgenciaDeleteDialog, AgenciaUpdatePage } from './agencia.page-object';

const expect = chai.expect;

describe('Agencia e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let agenciaComponentsPage: AgenciaComponentsPage;
  let agenciaUpdatePage: AgenciaUpdatePage;
  let agenciaDeleteDialog: AgenciaDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Agencias', async () => {
    await navBarPage.goToEntity('agencia');
    agenciaComponentsPage = new AgenciaComponentsPage();
    await browser.wait(ec.visibilityOf(agenciaComponentsPage.title), 5000);
    expect(await agenciaComponentsPage.getTitle()).to.eq('freejobApp.agencia.home.title');
  });

  it('should load create Agencia page', async () => {
    await agenciaComponentsPage.clickOnCreateButton();
    agenciaUpdatePage = new AgenciaUpdatePage();
    expect(await agenciaUpdatePage.getPageTitle()).to.eq('freejobApp.agencia.home.createOrEditLabel');
    await agenciaUpdatePage.cancel();
  });

  it('should create and save Agencias', async () => {
    const nbButtonsBeforeCreate = await agenciaComponentsPage.countDeleteButtons();

    await agenciaComponentsPage.clickOnCreateButton();
    await promise.all([
      agenciaUpdatePage.setNomeAgenciaInput('nomeAgencia'),
      agenciaUpdatePage.setContatoAgenciaInput('contatoAgencia'),
      agenciaUpdatePage.setEmailInput('email')
    ]);
    expect(await agenciaUpdatePage.getNomeAgenciaInput()).to.eq('nomeAgencia', 'Expected NomeAgencia value to be equals to nomeAgencia');
    expect(await agenciaUpdatePage.getContatoAgenciaInput()).to.eq(
      'contatoAgencia',
      'Expected ContatoAgencia value to be equals to contatoAgencia'
    );
    expect(await agenciaUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    await agenciaUpdatePage.save();
    expect(await agenciaUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await agenciaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Agencia', async () => {
    const nbButtonsBeforeDelete = await agenciaComponentsPage.countDeleteButtons();
    await agenciaComponentsPage.clickOnLastDeleteButton();

    agenciaDeleteDialog = new AgenciaDeleteDialog();
    expect(await agenciaDeleteDialog.getDialogTitle()).to.eq('freejobApp.agencia.delete.question');
    await agenciaDeleteDialog.clickOnConfirmButton();

    expect(await agenciaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
