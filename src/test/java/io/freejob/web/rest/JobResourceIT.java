package io.freejob.web.rest;

import io.freejob.FreejobApp;
import io.freejob.domain.Job;
import io.freejob.domain.Agencia;
import io.freejob.repository.JobRepository;
import io.freejob.service.JobService;
import io.freejob.web.rest.errors.ExceptionTranslator;
import io.freejob.service.dto.JobCriteria;
import io.freejob.service.JobQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.freejob.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.freejob.domain.enumeration.TipoPagamento;
/**
 * Integration tests for the {@link JobResource} REST controller.
 */
@SpringBootTest(classes = FreejobApp.class)
public class JobResourceIT {

    private static final String DEFAULT_NOME_JOB = "AAAAAAAAAA";
    private static final String UPDATED_NOME_JOB = "BBBBBBBBBB";

    private static final Long DEFAULT_VALOR_HORA = 1L;
    private static final Long UPDATED_VALOR_HORA = 2L;
    private static final Long SMALLER_VALOR_HORA = 1L - 1L;

    private static final Long DEFAULT_TEMPO_EVENTO = 1L;
    private static final Long UPDATED_TEMPO_EVENTO = 2L;
    private static final Long SMALLER_TEMPO_EVENTO = 1L - 1L;

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_PAGAMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_PAGAMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TipoPagamento DEFAULT_TIPO_PAGAMENTO = TipoPagamento.DINHEIRO;
    private static final TipoPagamento UPDATED_TIPO_PAGAMENTO = TipoPagamento.TRANSFERENCIA;

    private static final Boolean DEFAULT_STATUS_PAGAMENTO = false;
    private static final Boolean UPDATED_STATUS_PAGAMENTO = true;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobQueryService jobQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restJobMockMvc;

    private Job job;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobResource jobResource = new JobResource(jobService, jobQueryService);
        this.restJobMockMvc = MockMvcBuilders.standaloneSetup(jobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createEntity(EntityManager em) {
        Job job = new Job()
            .nomeJob(DEFAULT_NOME_JOB)
            .valorHora(DEFAULT_VALOR_HORA)
            .tempoEvento(DEFAULT_TEMPO_EVENTO)
            .observacao(DEFAULT_OBSERVACAO)
            .dataPagamento(DEFAULT_DATA_PAGAMENTO)
            .tipoPagamento(DEFAULT_TIPO_PAGAMENTO)
            .statusPagamento(DEFAULT_STATUS_PAGAMENTO);
        // Add required entity
        Agencia agencia;
        if (TestUtil.findAll(em, Agencia.class).isEmpty()) {
            agencia = AgenciaResourceIT.createEntity(em);
            em.persist(agencia);
            em.flush();
        } else {
            agencia = TestUtil.findAll(em, Agencia.class).get(0);
        }
        job.setAgencia(agencia);
        return job;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createUpdatedEntity(EntityManager em) {
        Job job = new Job()
            .nomeJob(UPDATED_NOME_JOB)
            .valorHora(UPDATED_VALOR_HORA)
            .tempoEvento(UPDATED_TEMPO_EVENTO)
            .observacao(UPDATED_OBSERVACAO)
            .dataPagamento(UPDATED_DATA_PAGAMENTO)
            .tipoPagamento(UPDATED_TIPO_PAGAMENTO)
            .statusPagamento(UPDATED_STATUS_PAGAMENTO);
        // Add required entity
        Agencia agencia;
        if (TestUtil.findAll(em, Agencia.class).isEmpty()) {
            agencia = AgenciaResourceIT.createUpdatedEntity(em);
            em.persist(agencia);
            em.flush();
        } else {
            agencia = TestUtil.findAll(em, Agencia.class).get(0);
        }
        job.setAgencia(agencia);
        return job;
    }

    @BeforeEach
    public void initTest() {
        job = createEntity(em);
    }

    @Test
    @Transactional
    public void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getNomeJob()).isEqualTo(DEFAULT_NOME_JOB);
        assertThat(testJob.getValorHora()).isEqualTo(DEFAULT_VALOR_HORA);
        assertThat(testJob.getTempoEvento()).isEqualTo(DEFAULT_TEMPO_EVENTO);
        assertThat(testJob.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testJob.getDataPagamento()).isEqualTo(DEFAULT_DATA_PAGAMENTO);
        assertThat(testJob.getTipoPagamento()).isEqualTo(DEFAULT_TIPO_PAGAMENTO);
        assertThat(testJob.isStatusPagamento()).isEqualTo(DEFAULT_STATUS_PAGAMENTO);
    }

    @Test
    @Transactional
    public void createJobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job with an existing ID
        job.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomeJobIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setNomeJob(null);

        // Create the Job, which fails.

        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeJob").value(hasItem(DEFAULT_NOME_JOB)))
            .andExpect(jsonPath("$.[*].valorHora").value(hasItem(DEFAULT_VALOR_HORA.intValue())))
            .andExpect(jsonPath("$.[*].tempoEvento").value(hasItem(DEFAULT_TEMPO_EVENTO.intValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].dataPagamento").value(hasItem(DEFAULT_DATA_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].tipoPagamento").value(hasItem(DEFAULT_TIPO_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].statusPagamento").value(hasItem(DEFAULT_STATUS_PAGAMENTO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.nomeJob").value(DEFAULT_NOME_JOB))
            .andExpect(jsonPath("$.valorHora").value(DEFAULT_VALOR_HORA.intValue()))
            .andExpect(jsonPath("$.tempoEvento").value(DEFAULT_TEMPO_EVENTO.intValue()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.dataPagamento").value(DEFAULT_DATA_PAGAMENTO.toString()))
            .andExpect(jsonPath("$.tipoPagamento").value(DEFAULT_TIPO_PAGAMENTO.toString()))
            .andExpect(jsonPath("$.statusPagamento").value(DEFAULT_STATUS_PAGAMENTO.booleanValue()));
    }


    @Test
    @Transactional
    public void getJobsByIdFiltering() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        Long id = job.getId();

        defaultJobShouldBeFound("id.equals=" + id);
        defaultJobShouldNotBeFound("id.notEquals=" + id);

        defaultJobShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJobShouldNotBeFound("id.greaterThan=" + id);

        defaultJobShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJobShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllJobsByNomeJobIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where nomeJob equals to DEFAULT_NOME_JOB
        defaultJobShouldBeFound("nomeJob.equals=" + DEFAULT_NOME_JOB);

        // Get all the jobList where nomeJob equals to UPDATED_NOME_JOB
        defaultJobShouldNotBeFound("nomeJob.equals=" + UPDATED_NOME_JOB);
    }

    @Test
    @Transactional
    public void getAllJobsByNomeJobIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where nomeJob not equals to DEFAULT_NOME_JOB
        defaultJobShouldNotBeFound("nomeJob.notEquals=" + DEFAULT_NOME_JOB);

        // Get all the jobList where nomeJob not equals to UPDATED_NOME_JOB
        defaultJobShouldBeFound("nomeJob.notEquals=" + UPDATED_NOME_JOB);
    }

    @Test
    @Transactional
    public void getAllJobsByNomeJobIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where nomeJob in DEFAULT_NOME_JOB or UPDATED_NOME_JOB
        defaultJobShouldBeFound("nomeJob.in=" + DEFAULT_NOME_JOB + "," + UPDATED_NOME_JOB);

        // Get all the jobList where nomeJob equals to UPDATED_NOME_JOB
        defaultJobShouldNotBeFound("nomeJob.in=" + UPDATED_NOME_JOB);
    }

    @Test
    @Transactional
    public void getAllJobsByNomeJobIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where nomeJob is not null
        defaultJobShouldBeFound("nomeJob.specified=true");

        // Get all the jobList where nomeJob is null
        defaultJobShouldNotBeFound("nomeJob.specified=false");
    }
                @Test
    @Transactional
    public void getAllJobsByNomeJobContainsSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where nomeJob contains DEFAULT_NOME_JOB
        defaultJobShouldBeFound("nomeJob.contains=" + DEFAULT_NOME_JOB);

        // Get all the jobList where nomeJob contains UPDATED_NOME_JOB
        defaultJobShouldNotBeFound("nomeJob.contains=" + UPDATED_NOME_JOB);
    }

    @Test
    @Transactional
    public void getAllJobsByNomeJobNotContainsSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where nomeJob does not contain DEFAULT_NOME_JOB
        defaultJobShouldNotBeFound("nomeJob.doesNotContain=" + DEFAULT_NOME_JOB);

        // Get all the jobList where nomeJob does not contain UPDATED_NOME_JOB
        defaultJobShouldBeFound("nomeJob.doesNotContain=" + UPDATED_NOME_JOB);
    }


    @Test
    @Transactional
    public void getAllJobsByValorHoraIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where valorHora equals to DEFAULT_VALOR_HORA
        defaultJobShouldBeFound("valorHora.equals=" + DEFAULT_VALOR_HORA);

        // Get all the jobList where valorHora equals to UPDATED_VALOR_HORA
        defaultJobShouldNotBeFound("valorHora.equals=" + UPDATED_VALOR_HORA);
    }

    @Test
    @Transactional
    public void getAllJobsByValorHoraIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where valorHora not equals to DEFAULT_VALOR_HORA
        defaultJobShouldNotBeFound("valorHora.notEquals=" + DEFAULT_VALOR_HORA);

        // Get all the jobList where valorHora not equals to UPDATED_VALOR_HORA
        defaultJobShouldBeFound("valorHora.notEquals=" + UPDATED_VALOR_HORA);
    }

    @Test
    @Transactional
    public void getAllJobsByValorHoraIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where valorHora in DEFAULT_VALOR_HORA or UPDATED_VALOR_HORA
        defaultJobShouldBeFound("valorHora.in=" + DEFAULT_VALOR_HORA + "," + UPDATED_VALOR_HORA);

        // Get all the jobList where valorHora equals to UPDATED_VALOR_HORA
        defaultJobShouldNotBeFound("valorHora.in=" + UPDATED_VALOR_HORA);
    }

    @Test
    @Transactional
    public void getAllJobsByValorHoraIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where valorHora is not null
        defaultJobShouldBeFound("valorHora.specified=true");

        // Get all the jobList where valorHora is null
        defaultJobShouldNotBeFound("valorHora.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByValorHoraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where valorHora is greater than or equal to DEFAULT_VALOR_HORA
        defaultJobShouldBeFound("valorHora.greaterThanOrEqual=" + DEFAULT_VALOR_HORA);

        // Get all the jobList where valorHora is greater than or equal to UPDATED_VALOR_HORA
        defaultJobShouldNotBeFound("valorHora.greaterThanOrEqual=" + UPDATED_VALOR_HORA);
    }

    @Test
    @Transactional
    public void getAllJobsByValorHoraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where valorHora is less than or equal to DEFAULT_VALOR_HORA
        defaultJobShouldBeFound("valorHora.lessThanOrEqual=" + DEFAULT_VALOR_HORA);

        // Get all the jobList where valorHora is less than or equal to SMALLER_VALOR_HORA
        defaultJobShouldNotBeFound("valorHora.lessThanOrEqual=" + SMALLER_VALOR_HORA);
    }

    @Test
    @Transactional
    public void getAllJobsByValorHoraIsLessThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where valorHora is less than DEFAULT_VALOR_HORA
        defaultJobShouldNotBeFound("valorHora.lessThan=" + DEFAULT_VALOR_HORA);

        // Get all the jobList where valorHora is less than UPDATED_VALOR_HORA
        defaultJobShouldBeFound("valorHora.lessThan=" + UPDATED_VALOR_HORA);
    }

    @Test
    @Transactional
    public void getAllJobsByValorHoraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where valorHora is greater than DEFAULT_VALOR_HORA
        defaultJobShouldNotBeFound("valorHora.greaterThan=" + DEFAULT_VALOR_HORA);

        // Get all the jobList where valorHora is greater than SMALLER_VALOR_HORA
        defaultJobShouldBeFound("valorHora.greaterThan=" + SMALLER_VALOR_HORA);
    }


    @Test
    @Transactional
    public void getAllJobsByTempoEventoIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tempoEvento equals to DEFAULT_TEMPO_EVENTO
        defaultJobShouldBeFound("tempoEvento.equals=" + DEFAULT_TEMPO_EVENTO);

        // Get all the jobList where tempoEvento equals to UPDATED_TEMPO_EVENTO
        defaultJobShouldNotBeFound("tempoEvento.equals=" + UPDATED_TEMPO_EVENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByTempoEventoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tempoEvento not equals to DEFAULT_TEMPO_EVENTO
        defaultJobShouldNotBeFound("tempoEvento.notEquals=" + DEFAULT_TEMPO_EVENTO);

        // Get all the jobList where tempoEvento not equals to UPDATED_TEMPO_EVENTO
        defaultJobShouldBeFound("tempoEvento.notEquals=" + UPDATED_TEMPO_EVENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByTempoEventoIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tempoEvento in DEFAULT_TEMPO_EVENTO or UPDATED_TEMPO_EVENTO
        defaultJobShouldBeFound("tempoEvento.in=" + DEFAULT_TEMPO_EVENTO + "," + UPDATED_TEMPO_EVENTO);

        // Get all the jobList where tempoEvento equals to UPDATED_TEMPO_EVENTO
        defaultJobShouldNotBeFound("tempoEvento.in=" + UPDATED_TEMPO_EVENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByTempoEventoIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tempoEvento is not null
        defaultJobShouldBeFound("tempoEvento.specified=true");

        // Get all the jobList where tempoEvento is null
        defaultJobShouldNotBeFound("tempoEvento.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByTempoEventoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tempoEvento is greater than or equal to DEFAULT_TEMPO_EVENTO
        defaultJobShouldBeFound("tempoEvento.greaterThanOrEqual=" + DEFAULT_TEMPO_EVENTO);

        // Get all the jobList where tempoEvento is greater than or equal to UPDATED_TEMPO_EVENTO
        defaultJobShouldNotBeFound("tempoEvento.greaterThanOrEqual=" + UPDATED_TEMPO_EVENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByTempoEventoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tempoEvento is less than or equal to DEFAULT_TEMPO_EVENTO
        defaultJobShouldBeFound("tempoEvento.lessThanOrEqual=" + DEFAULT_TEMPO_EVENTO);

        // Get all the jobList where tempoEvento is less than or equal to SMALLER_TEMPO_EVENTO
        defaultJobShouldNotBeFound("tempoEvento.lessThanOrEqual=" + SMALLER_TEMPO_EVENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByTempoEventoIsLessThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tempoEvento is less than DEFAULT_TEMPO_EVENTO
        defaultJobShouldNotBeFound("tempoEvento.lessThan=" + DEFAULT_TEMPO_EVENTO);

        // Get all the jobList where tempoEvento is less than UPDATED_TEMPO_EVENTO
        defaultJobShouldBeFound("tempoEvento.lessThan=" + UPDATED_TEMPO_EVENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByTempoEventoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tempoEvento is greater than DEFAULT_TEMPO_EVENTO
        defaultJobShouldNotBeFound("tempoEvento.greaterThan=" + DEFAULT_TEMPO_EVENTO);

        // Get all the jobList where tempoEvento is greater than SMALLER_TEMPO_EVENTO
        defaultJobShouldBeFound("tempoEvento.greaterThan=" + SMALLER_TEMPO_EVENTO);
    }


    @Test
    @Transactional
    public void getAllJobsByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where observacao equals to DEFAULT_OBSERVACAO
        defaultJobShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the jobList where observacao equals to UPDATED_OBSERVACAO
        defaultJobShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllJobsByObservacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where observacao not equals to DEFAULT_OBSERVACAO
        defaultJobShouldNotBeFound("observacao.notEquals=" + DEFAULT_OBSERVACAO);

        // Get all the jobList where observacao not equals to UPDATED_OBSERVACAO
        defaultJobShouldBeFound("observacao.notEquals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllJobsByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultJobShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the jobList where observacao equals to UPDATED_OBSERVACAO
        defaultJobShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllJobsByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where observacao is not null
        defaultJobShouldBeFound("observacao.specified=true");

        // Get all the jobList where observacao is null
        defaultJobShouldNotBeFound("observacao.specified=false");
    }
                @Test
    @Transactional
    public void getAllJobsByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where observacao contains DEFAULT_OBSERVACAO
        defaultJobShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the jobList where observacao contains UPDATED_OBSERVACAO
        defaultJobShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllJobsByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where observacao does not contain DEFAULT_OBSERVACAO
        defaultJobShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the jobList where observacao does not contain UPDATED_OBSERVACAO
        defaultJobShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }


    @Test
    @Transactional
    public void getAllJobsByDataPagamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where dataPagamento equals to DEFAULT_DATA_PAGAMENTO
        defaultJobShouldBeFound("dataPagamento.equals=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the jobList where dataPagamento equals to UPDATED_DATA_PAGAMENTO
        defaultJobShouldNotBeFound("dataPagamento.equals=" + UPDATED_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByDataPagamentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where dataPagamento not equals to DEFAULT_DATA_PAGAMENTO
        defaultJobShouldNotBeFound("dataPagamento.notEquals=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the jobList where dataPagamento not equals to UPDATED_DATA_PAGAMENTO
        defaultJobShouldBeFound("dataPagamento.notEquals=" + UPDATED_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByDataPagamentoIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where dataPagamento in DEFAULT_DATA_PAGAMENTO or UPDATED_DATA_PAGAMENTO
        defaultJobShouldBeFound("dataPagamento.in=" + DEFAULT_DATA_PAGAMENTO + "," + UPDATED_DATA_PAGAMENTO);

        // Get all the jobList where dataPagamento equals to UPDATED_DATA_PAGAMENTO
        defaultJobShouldNotBeFound("dataPagamento.in=" + UPDATED_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByDataPagamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where dataPagamento is not null
        defaultJobShouldBeFound("dataPagamento.specified=true");

        // Get all the jobList where dataPagamento is null
        defaultJobShouldNotBeFound("dataPagamento.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByTipoPagamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tipoPagamento equals to DEFAULT_TIPO_PAGAMENTO
        defaultJobShouldBeFound("tipoPagamento.equals=" + DEFAULT_TIPO_PAGAMENTO);

        // Get all the jobList where tipoPagamento equals to UPDATED_TIPO_PAGAMENTO
        defaultJobShouldNotBeFound("tipoPagamento.equals=" + UPDATED_TIPO_PAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByTipoPagamentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tipoPagamento not equals to DEFAULT_TIPO_PAGAMENTO
        defaultJobShouldNotBeFound("tipoPagamento.notEquals=" + DEFAULT_TIPO_PAGAMENTO);

        // Get all the jobList where tipoPagamento not equals to UPDATED_TIPO_PAGAMENTO
        defaultJobShouldBeFound("tipoPagamento.notEquals=" + UPDATED_TIPO_PAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByTipoPagamentoIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tipoPagamento in DEFAULT_TIPO_PAGAMENTO or UPDATED_TIPO_PAGAMENTO
        defaultJobShouldBeFound("tipoPagamento.in=" + DEFAULT_TIPO_PAGAMENTO + "," + UPDATED_TIPO_PAGAMENTO);

        // Get all the jobList where tipoPagamento equals to UPDATED_TIPO_PAGAMENTO
        defaultJobShouldNotBeFound("tipoPagamento.in=" + UPDATED_TIPO_PAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByTipoPagamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tipoPagamento is not null
        defaultJobShouldBeFound("tipoPagamento.specified=true");

        // Get all the jobList where tipoPagamento is null
        defaultJobShouldNotBeFound("tipoPagamento.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByStatusPagamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where statusPagamento equals to DEFAULT_STATUS_PAGAMENTO
        defaultJobShouldBeFound("statusPagamento.equals=" + DEFAULT_STATUS_PAGAMENTO);

        // Get all the jobList where statusPagamento equals to UPDATED_STATUS_PAGAMENTO
        defaultJobShouldNotBeFound("statusPagamento.equals=" + UPDATED_STATUS_PAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByStatusPagamentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where statusPagamento not equals to DEFAULT_STATUS_PAGAMENTO
        defaultJobShouldNotBeFound("statusPagamento.notEquals=" + DEFAULT_STATUS_PAGAMENTO);

        // Get all the jobList where statusPagamento not equals to UPDATED_STATUS_PAGAMENTO
        defaultJobShouldBeFound("statusPagamento.notEquals=" + UPDATED_STATUS_PAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByStatusPagamentoIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where statusPagamento in DEFAULT_STATUS_PAGAMENTO or UPDATED_STATUS_PAGAMENTO
        defaultJobShouldBeFound("statusPagamento.in=" + DEFAULT_STATUS_PAGAMENTO + "," + UPDATED_STATUS_PAGAMENTO);

        // Get all the jobList where statusPagamento equals to UPDATED_STATUS_PAGAMENTO
        defaultJobShouldNotBeFound("statusPagamento.in=" + UPDATED_STATUS_PAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllJobsByStatusPagamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where statusPagamento is not null
        defaultJobShouldBeFound("statusPagamento.specified=true");

        // Get all the jobList where statusPagamento is null
        defaultJobShouldNotBeFound("statusPagamento.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByAgenciaIsEqualToSomething() throws Exception {
        // Get already existing entity
        Agencia agencia = job.getAgencia();
        jobRepository.saveAndFlush(job);
        Long agenciaId = agencia.getId();

        // Get all the jobList where agencia equals to agenciaId
        defaultJobShouldBeFound("agenciaId.equals=" + agenciaId);

        // Get all the jobList where agencia equals to agenciaId + 1
        defaultJobShouldNotBeFound("agenciaId.equals=" + (agenciaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobShouldBeFound(String filter) throws Exception {
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeJob").value(hasItem(DEFAULT_NOME_JOB)))
            .andExpect(jsonPath("$.[*].valorHora").value(hasItem(DEFAULT_VALOR_HORA.intValue())))
            .andExpect(jsonPath("$.[*].tempoEvento").value(hasItem(DEFAULT_TEMPO_EVENTO.intValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].dataPagamento").value(hasItem(DEFAULT_DATA_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].tipoPagamento").value(hasItem(DEFAULT_TIPO_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].statusPagamento").value(hasItem(DEFAULT_STATUS_PAGAMENTO.booleanValue())));

        // Check, that the count call also returns 1
        restJobMockMvc.perform(get("/api/jobs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobShouldNotBeFound(String filter) throws Exception {
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobMockMvc.perform(get("/api/jobs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJob() throws Exception {
        // Initialize the database
        jobService.save(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        Job updatedJob = jobRepository.findById(job.getId()).get();
        // Disconnect from session so that the updates on updatedJob are not directly saved in db
        em.detach(updatedJob);
        updatedJob
            .nomeJob(UPDATED_NOME_JOB)
            .valorHora(UPDATED_VALOR_HORA)
            .tempoEvento(UPDATED_TEMPO_EVENTO)
            .observacao(UPDATED_OBSERVACAO)
            .dataPagamento(UPDATED_DATA_PAGAMENTO)
            .tipoPagamento(UPDATED_TIPO_PAGAMENTO)
            .statusPagamento(UPDATED_STATUS_PAGAMENTO);

        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJob)))
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getNomeJob()).isEqualTo(UPDATED_NOME_JOB);
        assertThat(testJob.getValorHora()).isEqualTo(UPDATED_VALOR_HORA);
        assertThat(testJob.getTempoEvento()).isEqualTo(UPDATED_TEMPO_EVENTO);
        assertThat(testJob.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testJob.getDataPagamento()).isEqualTo(UPDATED_DATA_PAGAMENTO);
        assertThat(testJob.getTipoPagamento()).isEqualTo(UPDATED_TIPO_PAGAMENTO);
        assertThat(testJob.isStatusPagamento()).isEqualTo(UPDATED_STATUS_PAGAMENTO);
    }

    @Test
    @Transactional
    public void updateNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Create the Job

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJob() throws Exception {
        // Initialize the database
        jobService.save(job);

        int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Delete the job
        restJobMockMvc.perform(delete("/api/jobs/{id}", job.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
