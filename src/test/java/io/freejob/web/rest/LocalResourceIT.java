package io.freejob.web.rest;

import io.freejob.FreejobApp;
import io.freejob.domain.Local;
import io.freejob.domain.Evento;
import io.freejob.repository.LocalRepository;
import io.freejob.service.LocalService;
import io.freejob.web.rest.errors.ExceptionTranslator;
import io.freejob.service.dto.LocalCriteria;
import io.freejob.service.LocalQueryService;

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

import io.freejob.domain.enumeration.TipoLocal;
/**
 * Integration tests for the {@link LocalResource} REST controller.
 */
@SpringBootTest(classes = FreejobApp.class)
public class LocalResourceIT {

    private static final String DEFAULT_NOME_LOCAL = "AAAAAAAAAA";
    private static final String UPDATED_NOME_LOCAL = "BBBBBBBBBB";

    private static final TipoLocal DEFAULT_TIPO_LOCAL = TipoLocal.BAR;
    private static final TipoLocal UPDATED_TIPO_LOCAL = TipoLocal.RESTAURANTE;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private LocalService localService;

    @Autowired
    private LocalQueryService localQueryService;

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

    private MockMvc restLocalMockMvc;

    private Local local;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LocalResource localResource = new LocalResource(localService, localQueryService);
        this.restLocalMockMvc = MockMvcBuilders.standaloneSetup(localResource)
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
    public static Local createEntity(EntityManager em) {
        Local local = new Local()
            .nomeLocal(DEFAULT_NOME_LOCAL)
            .tipoLocal(DEFAULT_TIPO_LOCAL);
        return local;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Local createUpdatedEntity(EntityManager em) {
        Local local = new Local()
            .nomeLocal(UPDATED_NOME_LOCAL)
            .tipoLocal(UPDATED_TIPO_LOCAL);
        return local;
    }

    @BeforeEach
    public void initTest() {
        local = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocal() throws Exception {
        int databaseSizeBeforeCreate = localRepository.findAll().size();

        // Create the Local
        restLocalMockMvc.perform(post("/api/locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(local)))
            .andExpect(status().isCreated());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeCreate + 1);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getNomeLocal()).isEqualTo(DEFAULT_NOME_LOCAL);
        assertThat(testLocal.getTipoLocal()).isEqualTo(DEFAULT_TIPO_LOCAL);
    }

    @Test
    @Transactional
    public void createLocalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = localRepository.findAll().size();

        // Create the Local with an existing ID
        local.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalMockMvc.perform(post("/api/locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(local)))
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomeLocalIsRequired() throws Exception {
        int databaseSizeBeforeTest = localRepository.findAll().size();
        // set the field null
        local.setNomeLocal(null);

        // Create the Local, which fails.

        restLocalMockMvc.perform(post("/api/locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(local)))
            .andExpect(status().isBadRequest());

        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocals() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList
        restLocalMockMvc.perform(get("/api/locals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(local.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeLocal").value(hasItem(DEFAULT_NOME_LOCAL)))
            .andExpect(jsonPath("$.[*].tipoLocal").value(hasItem(DEFAULT_TIPO_LOCAL.toString())));
    }
    
    @Test
    @Transactional
    public void getLocal() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get the local
        restLocalMockMvc.perform(get("/api/locals/{id}", local.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(local.getId().intValue()))
            .andExpect(jsonPath("$.nomeLocal").value(DEFAULT_NOME_LOCAL))
            .andExpect(jsonPath("$.tipoLocal").value(DEFAULT_TIPO_LOCAL.toString()));
    }


    @Test
    @Transactional
    public void getLocalsByIdFiltering() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        Long id = local.getId();

        defaultLocalShouldBeFound("id.equals=" + id);
        defaultLocalShouldNotBeFound("id.notEquals=" + id);

        defaultLocalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocalShouldNotBeFound("id.greaterThan=" + id);

        defaultLocalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocalShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLocalsByNomeLocalIsEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where nomeLocal equals to DEFAULT_NOME_LOCAL
        defaultLocalShouldBeFound("nomeLocal.equals=" + DEFAULT_NOME_LOCAL);

        // Get all the localList where nomeLocal equals to UPDATED_NOME_LOCAL
        defaultLocalShouldNotBeFound("nomeLocal.equals=" + UPDATED_NOME_LOCAL);
    }

    @Test
    @Transactional
    public void getAllLocalsByNomeLocalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where nomeLocal not equals to DEFAULT_NOME_LOCAL
        defaultLocalShouldNotBeFound("nomeLocal.notEquals=" + DEFAULT_NOME_LOCAL);

        // Get all the localList where nomeLocal not equals to UPDATED_NOME_LOCAL
        defaultLocalShouldBeFound("nomeLocal.notEquals=" + UPDATED_NOME_LOCAL);
    }

    @Test
    @Transactional
    public void getAllLocalsByNomeLocalIsInShouldWork() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where nomeLocal in DEFAULT_NOME_LOCAL or UPDATED_NOME_LOCAL
        defaultLocalShouldBeFound("nomeLocal.in=" + DEFAULT_NOME_LOCAL + "," + UPDATED_NOME_LOCAL);

        // Get all the localList where nomeLocal equals to UPDATED_NOME_LOCAL
        defaultLocalShouldNotBeFound("nomeLocal.in=" + UPDATED_NOME_LOCAL);
    }

    @Test
    @Transactional
    public void getAllLocalsByNomeLocalIsNullOrNotNull() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where nomeLocal is not null
        defaultLocalShouldBeFound("nomeLocal.specified=true");

        // Get all the localList where nomeLocal is null
        defaultLocalShouldNotBeFound("nomeLocal.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocalsByNomeLocalContainsSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where nomeLocal contains DEFAULT_NOME_LOCAL
        defaultLocalShouldBeFound("nomeLocal.contains=" + DEFAULT_NOME_LOCAL);

        // Get all the localList where nomeLocal contains UPDATED_NOME_LOCAL
        defaultLocalShouldNotBeFound("nomeLocal.contains=" + UPDATED_NOME_LOCAL);
    }

    @Test
    @Transactional
    public void getAllLocalsByNomeLocalNotContainsSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where nomeLocal does not contain DEFAULT_NOME_LOCAL
        defaultLocalShouldNotBeFound("nomeLocal.doesNotContain=" + DEFAULT_NOME_LOCAL);

        // Get all the localList where nomeLocal does not contain UPDATED_NOME_LOCAL
        defaultLocalShouldBeFound("nomeLocal.doesNotContain=" + UPDATED_NOME_LOCAL);
    }


    @Test
    @Transactional
    public void getAllLocalsByTipoLocalIsEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where tipoLocal equals to DEFAULT_TIPO_LOCAL
        defaultLocalShouldBeFound("tipoLocal.equals=" + DEFAULT_TIPO_LOCAL);

        // Get all the localList where tipoLocal equals to UPDATED_TIPO_LOCAL
        defaultLocalShouldNotBeFound("tipoLocal.equals=" + UPDATED_TIPO_LOCAL);
    }

    @Test
    @Transactional
    public void getAllLocalsByTipoLocalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where tipoLocal not equals to DEFAULT_TIPO_LOCAL
        defaultLocalShouldNotBeFound("tipoLocal.notEquals=" + DEFAULT_TIPO_LOCAL);

        // Get all the localList where tipoLocal not equals to UPDATED_TIPO_LOCAL
        defaultLocalShouldBeFound("tipoLocal.notEquals=" + UPDATED_TIPO_LOCAL);
    }

    @Test
    @Transactional
    public void getAllLocalsByTipoLocalIsInShouldWork() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where tipoLocal in DEFAULT_TIPO_LOCAL or UPDATED_TIPO_LOCAL
        defaultLocalShouldBeFound("tipoLocal.in=" + DEFAULT_TIPO_LOCAL + "," + UPDATED_TIPO_LOCAL);

        // Get all the localList where tipoLocal equals to UPDATED_TIPO_LOCAL
        defaultLocalShouldNotBeFound("tipoLocal.in=" + UPDATED_TIPO_LOCAL);
    }

    @Test
    @Transactional
    public void getAllLocalsByTipoLocalIsNullOrNotNull() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList where tipoLocal is not null
        defaultLocalShouldBeFound("tipoLocal.specified=true");

        // Get all the localList where tipoLocal is null
        defaultLocalShouldNotBeFound("tipoLocal.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocalsByEventoIsEqualToSomething() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);
        Evento evento = EventoResourceIT.createEntity(em);
        em.persist(evento);
        em.flush();
        local.setEvento(evento);
        localRepository.saveAndFlush(local);
        Long eventoId = evento.getId();

        // Get all the localList where evento equals to eventoId
        defaultLocalShouldBeFound("eventoId.equals=" + eventoId);

        // Get all the localList where evento equals to eventoId + 1
        defaultLocalShouldNotBeFound("eventoId.equals=" + (eventoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocalShouldBeFound(String filter) throws Exception {
        restLocalMockMvc.perform(get("/api/locals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(local.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeLocal").value(hasItem(DEFAULT_NOME_LOCAL)))
            .andExpect(jsonPath("$.[*].tipoLocal").value(hasItem(DEFAULT_TIPO_LOCAL.toString())));

        // Check, that the count call also returns 1
        restLocalMockMvc.perform(get("/api/locals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocalShouldNotBeFound(String filter) throws Exception {
        restLocalMockMvc.perform(get("/api/locals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocalMockMvc.perform(get("/api/locals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLocal() throws Exception {
        // Get the local
        restLocalMockMvc.perform(get("/api/locals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocal() throws Exception {
        // Initialize the database
        localService.save(local);

        int databaseSizeBeforeUpdate = localRepository.findAll().size();

        // Update the local
        Local updatedLocal = localRepository.findById(local.getId()).get();
        // Disconnect from session so that the updates on updatedLocal are not directly saved in db
        em.detach(updatedLocal);
        updatedLocal
            .nomeLocal(UPDATED_NOME_LOCAL)
            .tipoLocal(UPDATED_TIPO_LOCAL);

        restLocalMockMvc.perform(put("/api/locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLocal)))
            .andExpect(status().isOk());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getNomeLocal()).isEqualTo(UPDATED_NOME_LOCAL);
        assertThat(testLocal.getTipoLocal()).isEqualTo(UPDATED_TIPO_LOCAL);
    }

    @Test
    @Transactional
    public void updateNonExistingLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();

        // Create the Local

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalMockMvc.perform(put("/api/locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(local)))
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLocal() throws Exception {
        // Initialize the database
        localService.save(local);

        int databaseSizeBeforeDelete = localRepository.findAll().size();

        // Delete the local
        restLocalMockMvc.perform(delete("/api/locals/{id}", local.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
