package io.freejob.web.rest;

import io.freejob.FreejobApp;
import io.freejob.domain.Evento;
import io.freejob.domain.Job;
import io.freejob.repository.EventoRepository;
import io.freejob.service.EventoService;
import io.freejob.web.rest.errors.ExceptionTranslator;
import io.freejob.service.dto.EventoCriteria;
import io.freejob.service.EventoQueryService;

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

import io.freejob.domain.enumeration.TipoEvento;
/**
 * Integration tests for the {@link EventoResource} REST controller.
 */
@SpringBootTest(classes = FreejobApp.class)
public class EventoResourceIT {

    private static final String DEFAULT_NOME_EVENTO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_EVENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICA_EVENTO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICA_EVENTO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_STATUS_JOB = false;
    private static final Boolean UPDATED_STATUS_JOB = true;

    private static final TipoEvento DEFAULT_TIPO_EVENTO = TipoEvento.PANFLETAGEM;
    private static final TipoEvento UPDATED_TIPO_EVENTO = TipoEvento.FESTA;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private EventoQueryService eventoQueryService;

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

    private MockMvc restEventoMockMvc;

    private Evento evento;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventoResource eventoResource = new EventoResource(eventoService, eventoQueryService);
        this.restEventoMockMvc = MockMvcBuilders.standaloneSetup(eventoResource)
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
    public static Evento createEntity(EntityManager em) {
        Evento evento = new Evento()
            .nomeEvento(DEFAULT_NOME_EVENTO)
            .descricaEvento(DEFAULT_DESCRICA_EVENTO)
            .dataInicio(DEFAULT_DATA_INICIO)
            .dataFim(DEFAULT_DATA_FIM)
            .statusJob(DEFAULT_STATUS_JOB)
            .tipoEvento(DEFAULT_TIPO_EVENTO);
        return evento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evento createUpdatedEntity(EntityManager em) {
        Evento evento = new Evento()
            .nomeEvento(UPDATED_NOME_EVENTO)
            .descricaEvento(UPDATED_DESCRICA_EVENTO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .statusJob(UPDATED_STATUS_JOB)
            .tipoEvento(UPDATED_TIPO_EVENTO);
        return evento;
    }

    @BeforeEach
    public void initTest() {
        evento = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvento() throws Exception {
        int databaseSizeBeforeCreate = eventoRepository.findAll().size();

        // Create the Evento
        restEventoMockMvc.perform(post("/api/eventos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isCreated());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeCreate + 1);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getNomeEvento()).isEqualTo(DEFAULT_NOME_EVENTO);
        assertThat(testEvento.getDescricaEvento()).isEqualTo(DEFAULT_DESCRICA_EVENTO);
        assertThat(testEvento.getDataInicio()).isEqualTo(DEFAULT_DATA_INICIO);
        assertThat(testEvento.getDataFim()).isEqualTo(DEFAULT_DATA_FIM);
        assertThat(testEvento.isStatusJob()).isEqualTo(DEFAULT_STATUS_JOB);
        assertThat(testEvento.getTipoEvento()).isEqualTo(DEFAULT_TIPO_EVENTO);
    }

    @Test
    @Transactional
    public void createEventoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventoRepository.findAll().size();

        // Create the Evento with an existing ID
        evento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventoMockMvc.perform(post("/api/eventos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomeEventoIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setNomeEvento(null);

        // Create the Evento, which fails.

        restEventoMockMvc.perform(post("/api/eventos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setDataInicio(null);

        // Create the Evento, which fails.

        restEventoMockMvc.perform(post("/api/eventos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataFimIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setDataFim(null);

        // Create the Evento, which fails.

        restEventoMockMvc.perform(post("/api/eventos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEventos() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList
        restEventoMockMvc.perform(get("/api/eventos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeEvento").value(hasItem(DEFAULT_NOME_EVENTO)))
            .andExpect(jsonPath("$.[*].descricaEvento").value(hasItem(DEFAULT_DESCRICA_EVENTO)))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())))
            .andExpect(jsonPath("$.[*].statusJob").value(hasItem(DEFAULT_STATUS_JOB.booleanValue())))
            .andExpect(jsonPath("$.[*].tipoEvento").value(hasItem(DEFAULT_TIPO_EVENTO.toString())));
    }
    
    @Test
    @Transactional
    public void getEvento() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get the evento
        restEventoMockMvc.perform(get("/api/eventos/{id}", evento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(evento.getId().intValue()))
            .andExpect(jsonPath("$.nomeEvento").value(DEFAULT_NOME_EVENTO))
            .andExpect(jsonPath("$.descricaEvento").value(DEFAULT_DESCRICA_EVENTO))
            .andExpect(jsonPath("$.dataInicio").value(DEFAULT_DATA_INICIO.toString()))
            .andExpect(jsonPath("$.dataFim").value(DEFAULT_DATA_FIM.toString()))
            .andExpect(jsonPath("$.statusJob").value(DEFAULT_STATUS_JOB.booleanValue()))
            .andExpect(jsonPath("$.tipoEvento").value(DEFAULT_TIPO_EVENTO.toString()));
    }


    @Test
    @Transactional
    public void getEventosByIdFiltering() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        Long id = evento.getId();

        defaultEventoShouldBeFound("id.equals=" + id);
        defaultEventoShouldNotBeFound("id.notEquals=" + id);

        defaultEventoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventoShouldNotBeFound("id.greaterThan=" + id);

        defaultEventoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEventosByNomeEventoIsEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where nomeEvento equals to DEFAULT_NOME_EVENTO
        defaultEventoShouldBeFound("nomeEvento.equals=" + DEFAULT_NOME_EVENTO);

        // Get all the eventoList where nomeEvento equals to UPDATED_NOME_EVENTO
        defaultEventoShouldNotBeFound("nomeEvento.equals=" + UPDATED_NOME_EVENTO);
    }

    @Test
    @Transactional
    public void getAllEventosByNomeEventoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where nomeEvento not equals to DEFAULT_NOME_EVENTO
        defaultEventoShouldNotBeFound("nomeEvento.notEquals=" + DEFAULT_NOME_EVENTO);

        // Get all the eventoList where nomeEvento not equals to UPDATED_NOME_EVENTO
        defaultEventoShouldBeFound("nomeEvento.notEquals=" + UPDATED_NOME_EVENTO);
    }

    @Test
    @Transactional
    public void getAllEventosByNomeEventoIsInShouldWork() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where nomeEvento in DEFAULT_NOME_EVENTO or UPDATED_NOME_EVENTO
        defaultEventoShouldBeFound("nomeEvento.in=" + DEFAULT_NOME_EVENTO + "," + UPDATED_NOME_EVENTO);

        // Get all the eventoList where nomeEvento equals to UPDATED_NOME_EVENTO
        defaultEventoShouldNotBeFound("nomeEvento.in=" + UPDATED_NOME_EVENTO);
    }

    @Test
    @Transactional
    public void getAllEventosByNomeEventoIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where nomeEvento is not null
        defaultEventoShouldBeFound("nomeEvento.specified=true");

        // Get all the eventoList where nomeEvento is null
        defaultEventoShouldNotBeFound("nomeEvento.specified=false");
    }
                @Test
    @Transactional
    public void getAllEventosByNomeEventoContainsSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where nomeEvento contains DEFAULT_NOME_EVENTO
        defaultEventoShouldBeFound("nomeEvento.contains=" + DEFAULT_NOME_EVENTO);

        // Get all the eventoList where nomeEvento contains UPDATED_NOME_EVENTO
        defaultEventoShouldNotBeFound("nomeEvento.contains=" + UPDATED_NOME_EVENTO);
    }

    @Test
    @Transactional
    public void getAllEventosByNomeEventoNotContainsSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where nomeEvento does not contain DEFAULT_NOME_EVENTO
        defaultEventoShouldNotBeFound("nomeEvento.doesNotContain=" + DEFAULT_NOME_EVENTO);

        // Get all the eventoList where nomeEvento does not contain UPDATED_NOME_EVENTO
        defaultEventoShouldBeFound("nomeEvento.doesNotContain=" + UPDATED_NOME_EVENTO);
    }


    @Test
    @Transactional
    public void getAllEventosByDescricaEventoIsEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where descricaEvento equals to DEFAULT_DESCRICA_EVENTO
        defaultEventoShouldBeFound("descricaEvento.equals=" + DEFAULT_DESCRICA_EVENTO);

        // Get all the eventoList where descricaEvento equals to UPDATED_DESCRICA_EVENTO
        defaultEventoShouldNotBeFound("descricaEvento.equals=" + UPDATED_DESCRICA_EVENTO);
    }

    @Test
    @Transactional
    public void getAllEventosByDescricaEventoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where descricaEvento not equals to DEFAULT_DESCRICA_EVENTO
        defaultEventoShouldNotBeFound("descricaEvento.notEquals=" + DEFAULT_DESCRICA_EVENTO);

        // Get all the eventoList where descricaEvento not equals to UPDATED_DESCRICA_EVENTO
        defaultEventoShouldBeFound("descricaEvento.notEquals=" + UPDATED_DESCRICA_EVENTO);
    }

    @Test
    @Transactional
    public void getAllEventosByDescricaEventoIsInShouldWork() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where descricaEvento in DEFAULT_DESCRICA_EVENTO or UPDATED_DESCRICA_EVENTO
        defaultEventoShouldBeFound("descricaEvento.in=" + DEFAULT_DESCRICA_EVENTO + "," + UPDATED_DESCRICA_EVENTO);

        // Get all the eventoList where descricaEvento equals to UPDATED_DESCRICA_EVENTO
        defaultEventoShouldNotBeFound("descricaEvento.in=" + UPDATED_DESCRICA_EVENTO);
    }

    @Test
    @Transactional
    public void getAllEventosByDescricaEventoIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where descricaEvento is not null
        defaultEventoShouldBeFound("descricaEvento.specified=true");

        // Get all the eventoList where descricaEvento is null
        defaultEventoShouldNotBeFound("descricaEvento.specified=false");
    }
                @Test
    @Transactional
    public void getAllEventosByDescricaEventoContainsSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where descricaEvento contains DEFAULT_DESCRICA_EVENTO
        defaultEventoShouldBeFound("descricaEvento.contains=" + DEFAULT_DESCRICA_EVENTO);

        // Get all the eventoList where descricaEvento contains UPDATED_DESCRICA_EVENTO
        defaultEventoShouldNotBeFound("descricaEvento.contains=" + UPDATED_DESCRICA_EVENTO);
    }

    @Test
    @Transactional
    public void getAllEventosByDescricaEventoNotContainsSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where descricaEvento does not contain DEFAULT_DESCRICA_EVENTO
        defaultEventoShouldNotBeFound("descricaEvento.doesNotContain=" + DEFAULT_DESCRICA_EVENTO);

        // Get all the eventoList where descricaEvento does not contain UPDATED_DESCRICA_EVENTO
        defaultEventoShouldBeFound("descricaEvento.doesNotContain=" + UPDATED_DESCRICA_EVENTO);
    }


    @Test
    @Transactional
    public void getAllEventosByDataInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where dataInicio equals to DEFAULT_DATA_INICIO
        defaultEventoShouldBeFound("dataInicio.equals=" + DEFAULT_DATA_INICIO);

        // Get all the eventoList where dataInicio equals to UPDATED_DATA_INICIO
        defaultEventoShouldNotBeFound("dataInicio.equals=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllEventosByDataInicioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where dataInicio not equals to DEFAULT_DATA_INICIO
        defaultEventoShouldNotBeFound("dataInicio.notEquals=" + DEFAULT_DATA_INICIO);

        // Get all the eventoList where dataInicio not equals to UPDATED_DATA_INICIO
        defaultEventoShouldBeFound("dataInicio.notEquals=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllEventosByDataInicioIsInShouldWork() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where dataInicio in DEFAULT_DATA_INICIO or UPDATED_DATA_INICIO
        defaultEventoShouldBeFound("dataInicio.in=" + DEFAULT_DATA_INICIO + "," + UPDATED_DATA_INICIO);

        // Get all the eventoList where dataInicio equals to UPDATED_DATA_INICIO
        defaultEventoShouldNotBeFound("dataInicio.in=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    public void getAllEventosByDataInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where dataInicio is not null
        defaultEventoShouldBeFound("dataInicio.specified=true");

        // Get all the eventoList where dataInicio is null
        defaultEventoShouldNotBeFound("dataInicio.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventosByDataFimIsEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where dataFim equals to DEFAULT_DATA_FIM
        defaultEventoShouldBeFound("dataFim.equals=" + DEFAULT_DATA_FIM);

        // Get all the eventoList where dataFim equals to UPDATED_DATA_FIM
        defaultEventoShouldNotBeFound("dataFim.equals=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void getAllEventosByDataFimIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where dataFim not equals to DEFAULT_DATA_FIM
        defaultEventoShouldNotBeFound("dataFim.notEquals=" + DEFAULT_DATA_FIM);

        // Get all the eventoList where dataFim not equals to UPDATED_DATA_FIM
        defaultEventoShouldBeFound("dataFim.notEquals=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void getAllEventosByDataFimIsInShouldWork() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where dataFim in DEFAULT_DATA_FIM or UPDATED_DATA_FIM
        defaultEventoShouldBeFound("dataFim.in=" + DEFAULT_DATA_FIM + "," + UPDATED_DATA_FIM);

        // Get all the eventoList where dataFim equals to UPDATED_DATA_FIM
        defaultEventoShouldNotBeFound("dataFim.in=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    public void getAllEventosByDataFimIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where dataFim is not null
        defaultEventoShouldBeFound("dataFim.specified=true");

        // Get all the eventoList where dataFim is null
        defaultEventoShouldNotBeFound("dataFim.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventosByStatusJobIsEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where statusJob equals to DEFAULT_STATUS_JOB
        defaultEventoShouldBeFound("statusJob.equals=" + DEFAULT_STATUS_JOB);

        // Get all the eventoList where statusJob equals to UPDATED_STATUS_JOB
        defaultEventoShouldNotBeFound("statusJob.equals=" + UPDATED_STATUS_JOB);
    }

    @Test
    @Transactional
    public void getAllEventosByStatusJobIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where statusJob not equals to DEFAULT_STATUS_JOB
        defaultEventoShouldNotBeFound("statusJob.notEquals=" + DEFAULT_STATUS_JOB);

        // Get all the eventoList where statusJob not equals to UPDATED_STATUS_JOB
        defaultEventoShouldBeFound("statusJob.notEquals=" + UPDATED_STATUS_JOB);
    }

    @Test
    @Transactional
    public void getAllEventosByStatusJobIsInShouldWork() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where statusJob in DEFAULT_STATUS_JOB or UPDATED_STATUS_JOB
        defaultEventoShouldBeFound("statusJob.in=" + DEFAULT_STATUS_JOB + "," + UPDATED_STATUS_JOB);

        // Get all the eventoList where statusJob equals to UPDATED_STATUS_JOB
        defaultEventoShouldNotBeFound("statusJob.in=" + UPDATED_STATUS_JOB);
    }

    @Test
    @Transactional
    public void getAllEventosByStatusJobIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where statusJob is not null
        defaultEventoShouldBeFound("statusJob.specified=true");

        // Get all the eventoList where statusJob is null
        defaultEventoShouldNotBeFound("statusJob.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventosByTipoEventoIsEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where tipoEvento equals to DEFAULT_TIPO_EVENTO
        defaultEventoShouldBeFound("tipoEvento.equals=" + DEFAULT_TIPO_EVENTO);

        // Get all the eventoList where tipoEvento equals to UPDATED_TIPO_EVENTO
        defaultEventoShouldNotBeFound("tipoEvento.equals=" + UPDATED_TIPO_EVENTO);
    }

    @Test
    @Transactional
    public void getAllEventosByTipoEventoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where tipoEvento not equals to DEFAULT_TIPO_EVENTO
        defaultEventoShouldNotBeFound("tipoEvento.notEquals=" + DEFAULT_TIPO_EVENTO);

        // Get all the eventoList where tipoEvento not equals to UPDATED_TIPO_EVENTO
        defaultEventoShouldBeFound("tipoEvento.notEquals=" + UPDATED_TIPO_EVENTO);
    }

    @Test
    @Transactional
    public void getAllEventosByTipoEventoIsInShouldWork() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where tipoEvento in DEFAULT_TIPO_EVENTO or UPDATED_TIPO_EVENTO
        defaultEventoShouldBeFound("tipoEvento.in=" + DEFAULT_TIPO_EVENTO + "," + UPDATED_TIPO_EVENTO);

        // Get all the eventoList where tipoEvento equals to UPDATED_TIPO_EVENTO
        defaultEventoShouldNotBeFound("tipoEvento.in=" + UPDATED_TIPO_EVENTO);
    }

    @Test
    @Transactional
    public void getAllEventosByTipoEventoIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList where tipoEvento is not null
        defaultEventoShouldBeFound("tipoEvento.specified=true");

        // Get all the eventoList where tipoEvento is null
        defaultEventoShouldNotBeFound("tipoEvento.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventosByJobIsEqualToSomething() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);
        Job job = JobResourceIT.createEntity(em);
        em.persist(job);
        em.flush();
        evento.setJob(job);
        eventoRepository.saveAndFlush(evento);
        Long jobId = job.getId();

        // Get all the eventoList where job equals to jobId
        defaultEventoShouldBeFound("jobId.equals=" + jobId);

        // Get all the eventoList where job equals to jobId + 1
        defaultEventoShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventoShouldBeFound(String filter) throws Exception {
        restEventoMockMvc.perform(get("/api/eventos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeEvento").value(hasItem(DEFAULT_NOME_EVENTO)))
            .andExpect(jsonPath("$.[*].descricaEvento").value(hasItem(DEFAULT_DESCRICA_EVENTO)))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())))
            .andExpect(jsonPath("$.[*].statusJob").value(hasItem(DEFAULT_STATUS_JOB.booleanValue())))
            .andExpect(jsonPath("$.[*].tipoEvento").value(hasItem(DEFAULT_TIPO_EVENTO.toString())));

        // Check, that the count call also returns 1
        restEventoMockMvc.perform(get("/api/eventos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventoShouldNotBeFound(String filter) throws Exception {
        restEventoMockMvc.perform(get("/api/eventos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventoMockMvc.perform(get("/api/eventos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEvento() throws Exception {
        // Get the evento
        restEventoMockMvc.perform(get("/api/eventos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvento() throws Exception {
        // Initialize the database
        eventoService.save(evento);

        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();

        // Update the evento
        Evento updatedEvento = eventoRepository.findById(evento.getId()).get();
        // Disconnect from session so that the updates on updatedEvento are not directly saved in db
        em.detach(updatedEvento);
        updatedEvento
            .nomeEvento(UPDATED_NOME_EVENTO)
            .descricaEvento(UPDATED_DESCRICA_EVENTO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .statusJob(UPDATED_STATUS_JOB)
            .tipoEvento(UPDATED_TIPO_EVENTO);

        restEventoMockMvc.perform(put("/api/eventos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvento)))
            .andExpect(status().isOk());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getNomeEvento()).isEqualTo(UPDATED_NOME_EVENTO);
        assertThat(testEvento.getDescricaEvento()).isEqualTo(UPDATED_DESCRICA_EVENTO);
        assertThat(testEvento.getDataInicio()).isEqualTo(UPDATED_DATA_INICIO);
        assertThat(testEvento.getDataFim()).isEqualTo(UPDATED_DATA_FIM);
        assertThat(testEvento.isStatusJob()).isEqualTo(UPDATED_STATUS_JOB);
        assertThat(testEvento.getTipoEvento()).isEqualTo(UPDATED_TIPO_EVENTO);
    }

    @Test
    @Transactional
    public void updateNonExistingEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();

        // Create the Evento

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventoMockMvc.perform(put("/api/eventos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEvento() throws Exception {
        // Initialize the database
        eventoService.save(evento);

        int databaseSizeBeforeDelete = eventoRepository.findAll().size();

        // Delete the evento
        restEventoMockMvc.perform(delete("/api/eventos/{id}", evento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
