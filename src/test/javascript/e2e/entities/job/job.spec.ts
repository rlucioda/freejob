import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  JobComponentsPage,
  /* JobDeleteDialog,
   */ JobUpdatePage
} from './job.page-object';

const expect = chai.expect;

describe('Job e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let jobComponentsPage: JobComponentsPage;
  let jobUpdatePage: JobUpdatePage;
  /* let jobDeleteDialog: JobDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Jobs', async () => {
    await navBarPage.goToEntity('job');
    jobComponentsPage = new JobComponentsPage();
    await browser.wait(ec.visibilityOf(jobComponentsPage.title), 5000);
    expect(await jobComponentsPage.getTitle()).to.eq('freejobApp.job.home.title');
  });

  it('should load create Job page', async () => {
    await jobComponentsPage.clickOnCreateButton();
    jobUpdatePage = new JobUpdatePage();
    expect(await jobUpdatePage.getPageTitle()).to.eq('freejobApp.job.home.createOrEditLabel');
    await jobUpdatePage.cancel();
  });

  /*  it('should create and save Jobs', async () => {
        const nbButtonsBeforeCreate = await jobComponentsPage.countDeleteButtons();

        await jobComponentsPage.clickOnCreateButton();
        await promise.all([
            jobUpdatePage.setNomeJobInput('nomeJob'),
            jobUpdatePage.setValorHoraInput('5'),
            jobUpdatePage.setTempoEventoInput('5'),
            jobUpdatePage.setObservacaoInput('observacao'),
            jobUpdatePage.setDataPagamentoInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            jobUpdatePage.tipoPagamentoSelectLastOption(),
            jobUpdatePage.agenciaSelectLastOption(),
        ]);
        expect(await jobUpdatePage.getNomeJobInput()).to.eq('nomeJob', 'Expected NomeJob value to be equals to nomeJob');
        expect(await jobUpdatePage.getValorHoraInput()).to.eq('5', 'Expected valorHora value to be equals to 5');
        expect(await jobUpdatePage.getTempoEventoInput()).to.eq('5', 'Expected tempoEvento value to be equals to 5');
        expect(await jobUpdatePage.getObservacaoInput()).to.eq('observacao', 'Expected Observacao value to be equals to observacao');
        expect(await jobUpdatePage.getDataPagamentoInput()).to.contain('2001-01-01T02:30', 'Expected dataPagamento value to be equals to 2000-12-31');
        const selectedStatusPagamento = jobUpdatePage.getStatusPagamentoInput();
        if (await selectedStatusPagamento.isSelected()) {
            await jobUpdatePage.getStatusPagamentoInput().click();
            expect(await jobUpdatePage.getStatusPagamentoInput().isSelected(), 'Expected statusPagamento not to be selected').to.be.false;
        } else {
            await jobUpdatePage.getStatusPagamentoInput().click();
            expect(await jobUpdatePage.getStatusPagamentoInput().isSelected(), 'Expected statusPagamento to be selected').to.be.true;
        }
        await jobUpdatePage.save();
        expect(await jobUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await jobComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /*  it('should delete last Job', async () => {
        const nbButtonsBeforeDelete = await jobComponentsPage.countDeleteButtons();
        await jobComponentsPage.clickOnLastDeleteButton();

        jobDeleteDialog = new JobDeleteDialog();
        expect(await jobDeleteDialog.getDialogTitle())
            .to.eq('freejobApp.job.delete.question');
        await jobDeleteDialog.clickOnConfirmButton();

        expect(await jobComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
