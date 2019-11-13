package io.freejob.web.rest;

import io.freejob.FreejobApp;
import io.freejob.domain.Agencia;
import io.freejob.domain.Job;
import io.freejob.repository.AgenciaRepository;
import io.freejob.service.AgenciaService;
import io.freejob.web.rest.errors.ExceptionTranslator;
import io.freejob.service.dto.AgenciaCriteria;
import io.freejob.service.AgenciaQueryService;

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
import java.util.List;

import static io.freejob.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AgenciaResource} REST controller.
 */
@SpringBootTest(classes = FreejobApp.class)
public class AgenciaResourceIT {

    private static final String DEFAULT_NOME_AGENCIA = "AAAAAAAAAA";
    private static final String UPDATED_NOME_AGENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_CONTATO_AGENCIA = "AAAAAAAAAA";
    private static final String UPDATED_CONTATO_AGENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private AgenciaService agenciaService;

    @Autowired
    private AgenciaQueryService agenciaQueryService;

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

    private MockMvc restAgenciaMockMvc;

    private Agencia agencia;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgenciaResource agenciaResource = new AgenciaResource(agenciaService, agenciaQueryService);
        this.restAgenciaMockMvc = MockMvcBuilders.standaloneSetup(agenciaResource)
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
    public static Agencia createEntity(EntityManager em) {
        Agencia agencia = new Agencia()
            .nomeAgencia(DEFAULT_NOME_AGENCIA)
            .contatoAgencia(DEFAULT_CONTATO_AGENCIA)
            .email(DEFAULT_EMAIL);
        return agencia;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agencia createUpdatedEntity(EntityManager em) {
        Agencia agencia = new Agencia()
            .nomeAgencia(UPDATED_NOME_AGENCIA)
            .contatoAgencia(UPDATED_CONTATO_AGENCIA)
            .email(UPDATED_EMAIL);
        return agencia;
    }

    @BeforeEach
    public void initTest() {
        agencia = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgencia() throws Exception {
        int databaseSizeBeforeCreate = agenciaRepository.findAll().size();

        // Create the Agencia
        restAgenciaMockMvc.perform(post("/api/agencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agencia)))
            .andExpect(status().isCreated());

        // Validate the Agencia in the database
        List<Agencia> agenciaList = agenciaRepository.findAll();
        assertThat(agenciaList).hasSize(databaseSizeBeforeCreate + 1);
        Agencia testAgencia = agenciaList.get(agenciaList.size() - 1);
        assertThat(testAgencia.getNomeAgencia()).isEqualTo(DEFAULT_NOME_AGENCIA);
        assertThat(testAgencia.getContatoAgencia()).isEqualTo(DEFAULT_CONTATO_AGENCIA);
        assertThat(testAgencia.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createAgenciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agenciaRepository.findAll().size();

        // Create the Agencia with an existing ID
        agencia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgenciaMockMvc.perform(post("/api/agencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agencia)))
            .andExpect(status().isBadRequest());

        // Validate the Agencia in the database
        List<Agencia> agenciaList = agenciaRepository.findAll();
        assertThat(agenciaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomeAgenciaIsRequired() throws Exception {
        int databaseSizeBeforeTest = agenciaRepository.findAll().size();
        // set the field null
        agencia.setNomeAgencia(null);

        // Create the Agencia, which fails.

        restAgenciaMockMvc.perform(post("/api/agencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agencia)))
            .andExpect(status().isBadRequest());

        List<Agencia> agenciaList = agenciaRepository.findAll();
        assertThat(agenciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAgencias() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList
        restAgenciaMockMvc.perform(get("/api/agencias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeAgencia").value(hasItem(DEFAULT_NOME_AGENCIA)))
            .andExpect(jsonPath("$.[*].contatoAgencia").value(hasItem(DEFAULT_CONTATO_AGENCIA)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }
    
    @Test
    @Transactional
    public void getAgencia() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get the agencia
        restAgenciaMockMvc.perform(get("/api/agencias/{id}", agencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agencia.getId().intValue()))
            .andExpect(jsonPath("$.nomeAgencia").value(DEFAULT_NOME_AGENCIA))
            .andExpect(jsonPath("$.contatoAgencia").value(DEFAULT_CONTATO_AGENCIA))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }


    @Test
    @Transactional
    public void getAgenciasByIdFiltering() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        Long id = agencia.getId();

        defaultAgenciaShouldBeFound("id.equals=" + id);
        defaultAgenciaShouldNotBeFound("id.notEquals=" + id);

        defaultAgenciaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAgenciaShouldNotBeFound("id.greaterThan=" + id);

        defaultAgenciaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAgenciaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAgenciasByNomeAgenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where nomeAgencia equals to DEFAULT_NOME_AGENCIA
        defaultAgenciaShouldBeFound("nomeAgencia.equals=" + DEFAULT_NOME_AGENCIA);

        // Get all the agenciaList where nomeAgencia equals to UPDATED_NOME_AGENCIA
        defaultAgenciaShouldNotBeFound("nomeAgencia.equals=" + UPDATED_NOME_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciasByNomeAgenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where nomeAgencia not equals to DEFAULT_NOME_AGENCIA
        defaultAgenciaShouldNotBeFound("nomeAgencia.notEquals=" + DEFAULT_NOME_AGENCIA);

        // Get all the agenciaList where nomeAgencia not equals to UPDATED_NOME_AGENCIA
        defaultAgenciaShouldBeFound("nomeAgencia.notEquals=" + UPDATED_NOME_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciasByNomeAgenciaIsInShouldWork() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where nomeAgencia in DEFAULT_NOME_AGENCIA or UPDATED_NOME_AGENCIA
        defaultAgenciaShouldBeFound("nomeAgencia.in=" + DEFAULT_NOME_AGENCIA + "," + UPDATED_NOME_AGENCIA);

        // Get all the agenciaList where nomeAgencia equals to UPDATED_NOME_AGENCIA
        defaultAgenciaShouldNotBeFound("nomeAgencia.in=" + UPDATED_NOME_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciasByNomeAgenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where nomeAgencia is not null
        defaultAgenciaShouldBeFound("nomeAgencia.specified=true");

        // Get all the agenciaList where nomeAgencia is null
        defaultAgenciaShouldNotBeFound("nomeAgencia.specified=false");
    }
                @Test
    @Transactional
    public void getAllAgenciasByNomeAgenciaContainsSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where nomeAgencia contains DEFAULT_NOME_AGENCIA
        defaultAgenciaShouldBeFound("nomeAgencia.contains=" + DEFAULT_NOME_AGENCIA);

        // Get all the agenciaList where nomeAgencia contains UPDATED_NOME_AGENCIA
        defaultAgenciaShouldNotBeFound("nomeAgencia.contains=" + UPDATED_NOME_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciasByNomeAgenciaNotContainsSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where nomeAgencia does not contain DEFAULT_NOME_AGENCIA
        defaultAgenciaShouldNotBeFound("nomeAgencia.doesNotContain=" + DEFAULT_NOME_AGENCIA);

        // Get all the agenciaList where nomeAgencia does not contain UPDATED_NOME_AGENCIA
        defaultAgenciaShouldBeFound("nomeAgencia.doesNotContain=" + UPDATED_NOME_AGENCIA);
    }


    @Test
    @Transactional
    public void getAllAgenciasByContatoAgenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where contatoAgencia equals to DEFAULT_CONTATO_AGENCIA
        defaultAgenciaShouldBeFound("contatoAgencia.equals=" + DEFAULT_CONTATO_AGENCIA);

        // Get all the agenciaList where contatoAgencia equals to UPDATED_CONTATO_AGENCIA
        defaultAgenciaShouldNotBeFound("contatoAgencia.equals=" + UPDATED_CONTATO_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciasByContatoAgenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where contatoAgencia not equals to DEFAULT_CONTATO_AGENCIA
        defaultAgenciaShouldNotBeFound("contatoAgencia.notEquals=" + DEFAULT_CONTATO_AGENCIA);

        // Get all the agenciaList where contatoAgencia not equals to UPDATED_CONTATO_AGENCIA
        defaultAgenciaShouldBeFound("contatoAgencia.notEquals=" + UPDATED_CONTATO_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciasByContatoAgenciaIsInShouldWork() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where contatoAgencia in DEFAULT_CONTATO_AGENCIA or UPDATED_CONTATO_AGENCIA
        defaultAgenciaShouldBeFound("contatoAgencia.in=" + DEFAULT_CONTATO_AGENCIA + "," + UPDATED_CONTATO_AGENCIA);

        // Get all the agenciaList where contatoAgencia equals to UPDATED_CONTATO_AGENCIA
        defaultAgenciaShouldNotBeFound("contatoAgencia.in=" + UPDATED_CONTATO_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciasByContatoAgenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where contatoAgencia is not null
        defaultAgenciaShouldBeFound("contatoAgencia.specified=true");

        // Get all the agenciaList where contatoAgencia is null
        defaultAgenciaShouldNotBeFound("contatoAgencia.specified=false");
    }
                @Test
    @Transactional
    public void getAllAgenciasByContatoAgenciaContainsSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where contatoAgencia contains DEFAULT_CONTATO_AGENCIA
        defaultAgenciaShouldBeFound("contatoAgencia.contains=" + DEFAULT_CONTATO_AGENCIA);

        // Get all the agenciaList where contatoAgencia contains UPDATED_CONTATO_AGENCIA
        defaultAgenciaShouldNotBeFound("contatoAgencia.contains=" + UPDATED_CONTATO_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciasByContatoAgenciaNotContainsSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where contatoAgencia does not contain DEFAULT_CONTATO_AGENCIA
        defaultAgenciaShouldNotBeFound("contatoAgencia.doesNotContain=" + DEFAULT_CONTATO_AGENCIA);

        // Get all the agenciaList where contatoAgencia does not contain UPDATED_CONTATO_AGENCIA
        defaultAgenciaShouldBeFound("contatoAgencia.doesNotContain=" + UPDATED_CONTATO_AGENCIA);
    }


    @Test
    @Transactional
    public void getAllAgenciasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where email equals to DEFAULT_EMAIL
        defaultAgenciaShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the agenciaList where email equals to UPDATED_EMAIL
        defaultAgenciaShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAgenciasByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where email not equals to DEFAULT_EMAIL
        defaultAgenciaShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the agenciaList where email not equals to UPDATED_EMAIL
        defaultAgenciaShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAgenciasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultAgenciaShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the agenciaList where email equals to UPDATED_EMAIL
        defaultAgenciaShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAgenciasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where email is not null
        defaultAgenciaShouldBeFound("email.specified=true");

        // Get all the agenciaList where email is null
        defaultAgenciaShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllAgenciasByEmailContainsSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where email contains DEFAULT_EMAIL
        defaultAgenciaShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the agenciaList where email contains UPDATED_EMAIL
        defaultAgenciaShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAgenciasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);

        // Get all the agenciaList where email does not contain DEFAULT_EMAIL
        defaultAgenciaShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the agenciaList where email does not contain UPDATED_EMAIL
        defaultAgenciaShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllAgenciasByJobIsEqualToSomething() throws Exception {
        // Initialize the database
        agenciaRepository.saveAndFlush(agencia);
        Job job = JobResourceIT.createEntity(em);
        em.persist(job);
        em.flush();
        agencia.addJob(job);
        agenciaRepository.saveAndFlush(agencia);
        Long jobId = job.getId();

        // Get all the agenciaList where job equals to jobId
        defaultAgenciaShouldBeFound("jobId.equals=" + jobId);

        // Get all the agenciaList where job equals to jobId + 1
        defaultAgenciaShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgenciaShouldBeFound(String filter) throws Exception {
        restAgenciaMockMvc.perform(get("/api/agencias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeAgencia").value(hasItem(DEFAULT_NOME_AGENCIA)))
            .andExpect(jsonPath("$.[*].contatoAgencia").value(hasItem(DEFAULT_CONTATO_AGENCIA)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restAgenciaMockMvc.perform(get("/api/agencias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgenciaShouldNotBeFound(String filter) throws Exception {
        restAgenciaMockMvc.perform(get("/api/agencias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgenciaMockMvc.perform(get("/api/agencias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAgencia() throws Exception {
        // Get the agencia
        restAgenciaMockMvc.perform(get("/api/agencias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgencia() throws Exception {
        // Initialize the database
        agenciaService.save(agencia);

        int databaseSizeBeforeUpdate = agenciaRepository.findAll().size();

        // Update the agencia
        Agencia updatedAgencia = agenciaRepository.findById(agencia.getId()).get();
        // Disconnect from session so that the updates on updatedAgencia are not directly saved in db
        em.detach(updatedAgencia);
        updatedAgencia
            .nomeAgencia(UPDATED_NOME_AGENCIA)
            .contatoAgencia(UPDATED_CONTATO_AGENCIA)
            .email(UPDATED_EMAIL);

        restAgenciaMockMvc.perform(put("/api/agencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAgencia)))
            .andExpect(status().isOk());

        // Validate the Agencia in the database
        List<Agencia> agenciaList = agenciaRepository.findAll();
        assertThat(agenciaList).hasSize(databaseSizeBeforeUpdate);
        Agencia testAgencia = agenciaList.get(agenciaList.size() - 1);
        assertThat(testAgencia.getNomeAgencia()).isEqualTo(UPDATED_NOME_AGENCIA);
        assertThat(testAgencia.getContatoAgencia()).isEqualTo(UPDATED_CONTATO_AGENCIA);
        assertThat(testAgencia.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingAgencia() throws Exception {
        int databaseSizeBeforeUpdate = agenciaRepository.findAll().size();

        // Create the Agencia

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgenciaMockMvc.perform(put("/api/agencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agencia)))
            .andExpect(status().isBadRequest());

        // Validate the Agencia in the database
        List<Agencia> agenciaList = agenciaRepository.findAll();
        assertThat(agenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAgencia() throws Exception {
        // Initialize the database
        agenciaService.save(agencia);

        int databaseSizeBeforeDelete = agenciaRepository.findAll().size();

        // Delete the agencia
        restAgenciaMockMvc.perform(delete("/api/agencias/{id}", agencia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Agencia> agenciaList = agenciaRepository.findAll();
        assertThat(agenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
