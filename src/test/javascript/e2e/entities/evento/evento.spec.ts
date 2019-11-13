import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EventoComponentsPage, EventoDeleteDialog, EventoUpdatePage } from './evento.page-object';

const expect = chai.expect;

describe('Evento e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let eventoComponentsPage: EventoComponentsPage;
  let eventoUpdatePage: EventoUpdatePage;
  let eventoDeleteDialog: EventoDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Eventos', async () => {
    await navBarPage.goToEntity('evento');
    eventoComponentsPage = new EventoComponentsPage();
    await browser.wait(ec.visibilityOf(eventoComponentsPage.title), 5000);
    expect(await eventoComponentsPage.getTitle()).to.eq('freejobApp.evento.home.title');
  });

  it('should load create Evento page', async () => {
    await eventoComponentsPage.clickOnCreateButton();
    eventoUpdatePage = new EventoUpdatePage();
    expect(await eventoUpdatePage.getPageTitle()).to.eq('freejobApp.evento.home.createOrEditLabel');
    await eventoUpdatePage.cancel();
  });

  it('should create and save Eventos', async () => {
    const nbButtonsBeforeCreate = await eventoComponentsPage.countDeleteButtons();

    await eventoComponentsPage.clickOnCreateButton();
    await promise.all([
      eventoUpdatePage.setNomeEventoInput('nomeEvento'),
      eventoUpdatePage.setDescricaEventoInput('descricaEvento'),
      eventoUpdatePage.setDataInicioInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      eventoUpdatePage.setDataFimInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      eventoUpdatePage.tipoEventoSelectLastOption(),
      eventoUpdatePage.jobSelectLastOption()
    ]);
    expect(await eventoUpdatePage.getNomeEventoInput()).to.eq('nomeEvento', 'Expected NomeEvento value to be equals to nomeEvento');
    expect(await eventoUpdatePage.getDescricaEventoInput()).to.eq(
      'descricaEvento',
      'Expected DescricaEvento value to be equals to descricaEvento'
    );
    expect(await eventoUpdatePage.getDataInicioInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dataInicio value to be equals to 2000-12-31'
    );
    expect(await eventoUpdatePage.getDataFimInput()).to.contain('2001-01-01T02:30', 'Expected dataFim value to be equals to 2000-12-31');
    const selectedStatusJob = eventoUpdatePage.getStatusJobInput();
    if (await selectedStatusJob.isSelected()) {
      await eventoUpdatePage.getStatusJobInput().click();
      expect(await eventoUpdatePage.getStatusJobInput().isSelected(), 'Expected statusJob not to be selected').to.be.false;
    } else {
      await eventoUpdatePage.getStatusJobInput().click();
      expect(await eventoUpdatePage.getStatusJobInput().isSelected(), 'Expected statusJob to be selected').to.be.true;
    }
    await eventoUpdatePage.save();
    expect(await eventoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await eventoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Evento', async () => {
    const nbButtonsBeforeDelete = await eventoComponentsPage.countDeleteButtons();
    await eventoComponentsPage.clickOnLastDeleteButton();

    eventoDeleteDialog = new EventoDeleteDialog();
    expect(await eventoDeleteDialog.getDialogTitle()).to.eq('freejobApp.evento.delete.question');
    await eventoDeleteDialog.clickOnConfirmButton();

    expect(await eventoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
