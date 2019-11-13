package io.freejob.web.rest;

import io.freejob.FreejobApp;
import io.freejob.domain.Endereco;
import io.freejob.domain.Local;
import io.freejob.repository.EnderecoRepository;
import io.freejob.service.EnderecoService;
import io.freejob.web.rest.errors.ExceptionTranslator;
import io.freejob.service.dto.EnderecoCriteria;
import io.freejob.service.EnderecoQueryService;

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
 * Integration tests for the {@link EnderecoResource} REST controller.
 */
@SpringBootTest(classes = FreejobApp.class)
public class EnderecoResourceIT {

    private static final String DEFAULT_RUA = "AAAAAAAAAA";
    private static final String UPDATED_RUA = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLEMENTO = "AAAAAAAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private EnderecoQueryService enderecoQueryService;

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

    private MockMvc restEnderecoMockMvc;

    private Endereco endereco;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EnderecoResource enderecoResource = new EnderecoResource(enderecoService, enderecoQueryService);
        this.restEnderecoMockMvc = MockMvcBuilders.standaloneSetup(enderecoResource)
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
    public static Endereco createEntity(EntityManager em) {
        Endereco endereco = new Endereco()
            .rua(DEFAULT_RUA)
            .numero(DEFAULT_NUMERO)
            .complemento(DEFAULT_COMPLEMENTO)
            .bairro(DEFAULT_BAIRRO)
            .cep(DEFAULT_CEP)
            .cidade(DEFAULT_CIDADE)
            .estado(DEFAULT_ESTADO);
        return endereco;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createUpdatedEntity(EntityManager em) {
        Endereco endereco = new Endereco()
            .rua(UPDATED_RUA)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .cep(UPDATED_CEP)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO);
        return endereco;
    }

    @BeforeEach
    public void initTest() {
        endereco = createEntity(em);
    }

    @Test
    @Transactional
    public void createEndereco() throws Exception {
        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();

        // Create the Endereco
        restEnderecoMockMvc.perform(post("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isCreated());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate + 1);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getRua()).isEqualTo(DEFAULT_RUA);
        assertThat(testEndereco.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEndereco.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testEndereco.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testEndereco.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testEndereco.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testEndereco.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    public void createEnderecoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();

        // Create the Endereco with an existing ID
        endereco.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnderecoMockMvc.perform(post("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRuaIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setRua(null);

        // Create the Endereco, which fails.

        restEnderecoMockMvc.perform(post("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isBadRequest());

        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnderecos() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList
        restEnderecoMockMvc.perform(get("/api/enderecos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].rua").value(hasItem(DEFAULT_RUA)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }
    
    @Test
    @Transactional
    public void getEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get the endereco
        restEnderecoMockMvc.perform(get("/api/enderecos/{id}", endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(endereco.getId().intValue()))
            .andExpect(jsonPath("$.rua").value(DEFAULT_RUA))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }


    @Test
    @Transactional
    public void getEnderecosByIdFiltering() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        Long id = endereco.getId();

        defaultEnderecoShouldBeFound("id.equals=" + id);
        defaultEnderecoShouldNotBeFound("id.notEquals=" + id);

        defaultEnderecoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEnderecoShouldNotBeFound("id.greaterThan=" + id);

        defaultEnderecoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEnderecoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEnderecosByRuaIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where rua equals to DEFAULT_RUA
        defaultEnderecoShouldBeFound("rua.equals=" + DEFAULT_RUA);

        // Get all the enderecoList where rua equals to UPDATED_RUA
        defaultEnderecoShouldNotBeFound("rua.equals=" + UPDATED_RUA);
    }

    @Test
    @Transactional
    public void getAllEnderecosByRuaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where rua not equals to DEFAULT_RUA
        defaultEnderecoShouldNotBeFound("rua.notEquals=" + DEFAULT_RUA);

        // Get all the enderecoList where rua not equals to UPDATED_RUA
        defaultEnderecoShouldBeFound("rua.notEquals=" + UPDATED_RUA);
    }

    @Test
    @Transactional
    public void getAllEnderecosByRuaIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where rua in DEFAULT_RUA or UPDATED_RUA
        defaultEnderecoShouldBeFound("rua.in=" + DEFAULT_RUA + "," + UPDATED_RUA);

        // Get all the enderecoList where rua equals to UPDATED_RUA
        defaultEnderecoShouldNotBeFound("rua.in=" + UPDATED_RUA);
    }

    @Test
    @Transactional
    public void getAllEnderecosByRuaIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where rua is not null
        defaultEnderecoShouldBeFound("rua.specified=true");

        // Get all the enderecoList where rua is null
        defaultEnderecoShouldNotBeFound("rua.specified=false");
    }
                @Test
    @Transactional
    public void getAllEnderecosByRuaContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where rua contains DEFAULT_RUA
        defaultEnderecoShouldBeFound("rua.contains=" + DEFAULT_RUA);

        // Get all the enderecoList where rua contains UPDATED_RUA
        defaultEnderecoShouldNotBeFound("rua.contains=" + UPDATED_RUA);
    }

    @Test
    @Transactional
    public void getAllEnderecosByRuaNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where rua does not contain DEFAULT_RUA
        defaultEnderecoShouldNotBeFound("rua.doesNotContain=" + DEFAULT_RUA);

        // Get all the enderecoList where rua does not contain UPDATED_RUA
        defaultEnderecoShouldBeFound("rua.doesNotContain=" + UPDATED_RUA);
    }


    @Test
    @Transactional
    public void getAllEnderecosByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero equals to DEFAULT_NUMERO
        defaultEnderecoShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the enderecoList where numero equals to UPDATED_NUMERO
        defaultEnderecoShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByNumeroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero not equals to DEFAULT_NUMERO
        defaultEnderecoShouldNotBeFound("numero.notEquals=" + DEFAULT_NUMERO);

        // Get all the enderecoList where numero not equals to UPDATED_NUMERO
        defaultEnderecoShouldBeFound("numero.notEquals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultEnderecoShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the enderecoList where numero equals to UPDATED_NUMERO
        defaultEnderecoShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero is not null
        defaultEnderecoShouldBeFound("numero.specified=true");

        // Get all the enderecoList where numero is null
        defaultEnderecoShouldNotBeFound("numero.specified=false");
    }
                @Test
    @Transactional
    public void getAllEnderecosByNumeroContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero contains DEFAULT_NUMERO
        defaultEnderecoShouldBeFound("numero.contains=" + DEFAULT_NUMERO);

        // Get all the enderecoList where numero contains UPDATED_NUMERO
        defaultEnderecoShouldNotBeFound("numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero does not contain DEFAULT_NUMERO
        defaultEnderecoShouldNotBeFound("numero.doesNotContain=" + DEFAULT_NUMERO);

        // Get all the enderecoList where numero does not contain UPDATED_NUMERO
        defaultEnderecoShouldBeFound("numero.doesNotContain=" + UPDATED_NUMERO);
    }


    @Test
    @Transactional
    public void getAllEnderecosByComplementoIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento equals to DEFAULT_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.equals=" + DEFAULT_COMPLEMENTO);

        // Get all the enderecoList where complemento equals to UPDATED_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.equals=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByComplementoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento not equals to DEFAULT_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.notEquals=" + DEFAULT_COMPLEMENTO);

        // Get all the enderecoList where complemento not equals to UPDATED_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.notEquals=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByComplementoIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento in DEFAULT_COMPLEMENTO or UPDATED_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.in=" + DEFAULT_COMPLEMENTO + "," + UPDATED_COMPLEMENTO);

        // Get all the enderecoList where complemento equals to UPDATED_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.in=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByComplementoIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento is not null
        defaultEnderecoShouldBeFound("complemento.specified=true");

        // Get all the enderecoList where complemento is null
        defaultEnderecoShouldNotBeFound("complemento.specified=false");
    }
                @Test
    @Transactional
    public void getAllEnderecosByComplementoContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento contains DEFAULT_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.contains=" + DEFAULT_COMPLEMENTO);

        // Get all the enderecoList where complemento contains UPDATED_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.contains=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByComplementoNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento does not contain DEFAULT_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.doesNotContain=" + DEFAULT_COMPLEMENTO);

        // Get all the enderecoList where complemento does not contain UPDATED_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.doesNotContain=" + UPDATED_COMPLEMENTO);
    }


    @Test
    @Transactional
    public void getAllEnderecosByBairroIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro equals to DEFAULT_BAIRRO
        defaultEnderecoShouldBeFound("bairro.equals=" + DEFAULT_BAIRRO);

        // Get all the enderecoList where bairro equals to UPDATED_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.equals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByBairroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro not equals to DEFAULT_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.notEquals=" + DEFAULT_BAIRRO);

        // Get all the enderecoList where bairro not equals to UPDATED_BAIRRO
        defaultEnderecoShouldBeFound("bairro.notEquals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByBairroIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro in DEFAULT_BAIRRO or UPDATED_BAIRRO
        defaultEnderecoShouldBeFound("bairro.in=" + DEFAULT_BAIRRO + "," + UPDATED_BAIRRO);

        // Get all the enderecoList where bairro equals to UPDATED_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.in=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByBairroIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro is not null
        defaultEnderecoShouldBeFound("bairro.specified=true");

        // Get all the enderecoList where bairro is null
        defaultEnderecoShouldNotBeFound("bairro.specified=false");
    }
                @Test
    @Transactional
    public void getAllEnderecosByBairroContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro contains DEFAULT_BAIRRO
        defaultEnderecoShouldBeFound("bairro.contains=" + DEFAULT_BAIRRO);

        // Get all the enderecoList where bairro contains UPDATED_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.contains=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByBairroNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro does not contain DEFAULT_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.doesNotContain=" + DEFAULT_BAIRRO);

        // Get all the enderecoList where bairro does not contain UPDATED_BAIRRO
        defaultEnderecoShouldBeFound("bairro.doesNotContain=" + UPDATED_BAIRRO);
    }


    @Test
    @Transactional
    public void getAllEnderecosByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep equals to DEFAULT_CEP
        defaultEnderecoShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the enderecoList where cep equals to UPDATED_CEP
        defaultEnderecoShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllEnderecosByCepIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep not equals to DEFAULT_CEP
        defaultEnderecoShouldNotBeFound("cep.notEquals=" + DEFAULT_CEP);

        // Get all the enderecoList where cep not equals to UPDATED_CEP
        defaultEnderecoShouldBeFound("cep.notEquals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllEnderecosByCepIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultEnderecoShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the enderecoList where cep equals to UPDATED_CEP
        defaultEnderecoShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllEnderecosByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep is not null
        defaultEnderecoShouldBeFound("cep.specified=true");

        // Get all the enderecoList where cep is null
        defaultEnderecoShouldNotBeFound("cep.specified=false");
    }
                @Test
    @Transactional
    public void getAllEnderecosByCepContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep contains DEFAULT_CEP
        defaultEnderecoShouldBeFound("cep.contains=" + DEFAULT_CEP);

        // Get all the enderecoList where cep contains UPDATED_CEP
        defaultEnderecoShouldNotBeFound("cep.contains=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllEnderecosByCepNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep does not contain DEFAULT_CEP
        defaultEnderecoShouldNotBeFound("cep.doesNotContain=" + DEFAULT_CEP);

        // Get all the enderecoList where cep does not contain UPDATED_CEP
        defaultEnderecoShouldBeFound("cep.doesNotContain=" + UPDATED_CEP);
    }


    @Test
    @Transactional
    public void getAllEnderecosByCidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cidade equals to DEFAULT_CIDADE
        defaultEnderecoShouldBeFound("cidade.equals=" + DEFAULT_CIDADE);

        // Get all the enderecoList where cidade equals to UPDATED_CIDADE
        defaultEnderecoShouldNotBeFound("cidade.equals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllEnderecosByCidadeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cidade not equals to DEFAULT_CIDADE
        defaultEnderecoShouldNotBeFound("cidade.notEquals=" + DEFAULT_CIDADE);

        // Get all the enderecoList where cidade not equals to UPDATED_CIDADE
        defaultEnderecoShouldBeFound("cidade.notEquals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllEnderecosByCidadeIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cidade in DEFAULT_CIDADE or UPDATED_CIDADE
        defaultEnderecoShouldBeFound("cidade.in=" + DEFAULT_CIDADE + "," + UPDATED_CIDADE);

        // Get all the enderecoList where cidade equals to UPDATED_CIDADE
        defaultEnderecoShouldNotBeFound("cidade.in=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllEnderecosByCidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cidade is not null
        defaultEnderecoShouldBeFound("cidade.specified=true");

        // Get all the enderecoList where cidade is null
        defaultEnderecoShouldNotBeFound("cidade.specified=false");
    }
                @Test
    @Transactional
    public void getAllEnderecosByCidadeContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cidade contains DEFAULT_CIDADE
        defaultEnderecoShouldBeFound("cidade.contains=" + DEFAULT_CIDADE);

        // Get all the enderecoList where cidade contains UPDATED_CIDADE
        defaultEnderecoShouldNotBeFound("cidade.contains=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllEnderecosByCidadeNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cidade does not contain DEFAULT_CIDADE
        defaultEnderecoShouldNotBeFound("cidade.doesNotContain=" + DEFAULT_CIDADE);

        // Get all the enderecoList where cidade does not contain UPDATED_CIDADE
        defaultEnderecoShouldBeFound("cidade.doesNotContain=" + UPDATED_CIDADE);
    }


    @Test
    @Transactional
    public void getAllEnderecosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where estado equals to DEFAULT_ESTADO
        defaultEnderecoShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the enderecoList where estado equals to UPDATED_ESTADO
        defaultEnderecoShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where estado not equals to DEFAULT_ESTADO
        defaultEnderecoShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the enderecoList where estado not equals to UPDATED_ESTADO
        defaultEnderecoShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultEnderecoShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the enderecoList where estado equals to UPDATED_ESTADO
        defaultEnderecoShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where estado is not null
        defaultEnderecoShouldBeFound("estado.specified=true");

        // Get all the enderecoList where estado is null
        defaultEnderecoShouldNotBeFound("estado.specified=false");
    }
                @Test
    @Transactional
    public void getAllEnderecosByEstadoContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where estado contains DEFAULT_ESTADO
        defaultEnderecoShouldBeFound("estado.contains=" + DEFAULT_ESTADO);

        // Get all the enderecoList where estado contains UPDATED_ESTADO
        defaultEnderecoShouldNotBeFound("estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllEnderecosByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where estado does not contain DEFAULT_ESTADO
        defaultEnderecoShouldNotBeFound("estado.doesNotContain=" + DEFAULT_ESTADO);

        // Get all the enderecoList where estado does not contain UPDATED_ESTADO
        defaultEnderecoShouldBeFound("estado.doesNotContain=" + UPDATED_ESTADO);
    }


    @Test
    @Transactional
    public void getAllEnderecosByLocalIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);
        Local local = LocalResourceIT.createEntity(em);
        em.persist(local);
        em.flush();
        endereco.setLocal(local);
        enderecoRepository.saveAndFlush(endereco);
        Long localId = local.getId();

        // Get all the enderecoList where local equals to localId
        defaultEnderecoShouldBeFound("localId.equals=" + localId);

        // Get all the enderecoList where local equals to localId + 1
        defaultEnderecoShouldNotBeFound("localId.equals=" + (localId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEnderecoShouldBeFound(String filter) throws Exception {
        restEnderecoMockMvc.perform(get("/api/enderecos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].rua").value(hasItem(DEFAULT_RUA)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));

        // Check, that the count call also returns 1
        restEnderecoMockMvc.perform(get("/api/enderecos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEnderecoShouldNotBeFound(String filter) throws Exception {
        restEnderecoMockMvc.perform(get("/api/enderecos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEnderecoMockMvc.perform(get("/api/enderecos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEndereco() throws Exception {
        // Get the endereco
        restEnderecoMockMvc.perform(get("/api/enderecos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEndereco() throws Exception {
        // Initialize the database
        enderecoService.save(endereco);

        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Update the endereco
        Endereco updatedEndereco = enderecoRepository.findById(endereco.getId()).get();
        // Disconnect from session so that the updates on updatedEndereco are not directly saved in db
        em.detach(updatedEndereco);
        updatedEndereco
            .rua(UPDATED_RUA)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .cep(UPDATED_CEP)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO);

        restEnderecoMockMvc.perform(put("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEndereco)))
            .andExpect(status().isOk());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getRua()).isEqualTo(UPDATED_RUA);
        assertThat(testEndereco.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEndereco.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEndereco.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEndereco.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testEndereco.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void updateNonExistingEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Create the Endereco

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnderecoMockMvc.perform(put("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEndereco() throws Exception {
        // Initialize the database
        enderecoService.save(endereco);

        int databaseSizeBeforeDelete = enderecoRepository.findAll().size();

        // Delete the endereco
        restEnderecoMockMvc.perform(delete("/api/enderecos/{id}", endereco.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
