import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { LocalComponentsPage, LocalDeleteDialog, LocalUpdatePage } from './local.page-object';

const expect = chai.expect;

describe('Local e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let localComponentsPage: LocalComponentsPage;
  let localUpdatePage: LocalUpdatePage;
  let localDeleteDialog: LocalDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Locals', async () => {
    await navBarPage.goToEntity('local');
    localComponentsPage = new LocalComponentsPage();
    await browser.wait(ec.visibilityOf(localComponentsPage.title), 5000);
    expect(await localComponentsPage.getTitle()).to.eq('freejobApp.local.home.title');
  });

  it('should load create Local page', async () => {
    await localComponentsPage.clickOnCreateButton();
    localUpdatePage = new LocalUpdatePage();
    expect(await localUpdatePage.getPageTitle()).to.eq('freejobApp.local.home.createOrEditLabel');
    await localUpdatePage.cancel();
  });

  it('should create and save Locals', async () => {
    const nbButtonsBeforeCreate = await localComponentsPage.countDeleteButtons();

    await localComponentsPage.clickOnCreateButton();
    await promise.all([
      localUpdatePage.setNomeLocalInput('nomeLocal'),
      localUpdatePage.tipoLocalSelectLastOption(),
      localUpdatePage.eventoSelectLastOption()
    ]);
    expect(await localUpdatePage.getNomeLocalInput()).to.eq('nomeLocal', 'Expected NomeLocal value to be equals to nomeLocal');
    await localUpdatePage.save();
    expect(await localUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await localComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Local', async () => {
    const nbButtonsBeforeDelete = await localComponentsPage.countDeleteButtons();
    await localComponentsPage.clickOnLastDeleteButton();

    localDeleteDialog = new LocalDeleteDialog();
    expect(await localDeleteDialog.getDialogTitle()).to.eq('freejobApp.local.delete.question');
    await localDeleteDialog.clickOnConfirmButton();

    expect(await localComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
