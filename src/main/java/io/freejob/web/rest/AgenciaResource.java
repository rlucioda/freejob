package io.freejob.web.rest;

import io.freejob.domain.Agencia;
import io.freejob.service.AgenciaService;
import io.freejob.web.rest.errors.BadRequestAlertException;
import io.freejob.service.dto.AgenciaCriteria;
import io.freejob.service.AgenciaQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.freejob.domain.Agencia}.
 */
@RestController
@RequestMapping("/api")
public class AgenciaResource {

    private final Logger log = LoggerFactory.getLogger(AgenciaResource.class);

    private static final String ENTITY_NAME = "agencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgenciaService agenciaService;

    private final AgenciaQueryService agenciaQueryService;

    public AgenciaResource(AgenciaService agenciaService, AgenciaQueryService agenciaQueryService) {
        this.agenciaService = agenciaService;
        this.agenciaQueryService = agenciaQueryService;
    }

    /**
     * {@code POST  /agencias} : Create a new agencia.
     *
     * @param agencia the agencia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agencia, or with status {@code 400 (Bad Request)} if the agencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agencias")
    public ResponseEntity<Agencia> createAgencia(@Valid @RequestBody Agencia agencia) throws URISyntaxException {
        log.debug("REST request to save Agencia : {}", agencia);
        if (agencia.getId() != null) {
            throw new BadRequestAlertException("A new agencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Agencia result = agenciaService.save(agencia);
        return ResponseEntity.created(new URI("/api/agencias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agencias} : Updates an existing agencia.
     *
     * @param agencia the agencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencia,
     * or with status {@code 400 (Bad Request)} if the agencia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agencias")
    public ResponseEntity<Agencia> updateAgencia(@Valid @RequestBody Agencia agencia) throws URISyntaxException {
        log.debug("REST request to update Agencia : {}", agencia);
        if (agencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Agencia result = agenciaService.save(agencia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agencia.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /agencias} : get all the agencias.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agencias in body.
     */
    @GetMapping("/agencias")
    public ResponseEntity<List<Agencia>> getAllAgencias(AgenciaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Agencias by criteria: {}", criteria);
        Page<Agencia> page = agenciaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /agencias/count} : count all the agencias.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/agencias/count")
    public ResponseEntity<Long> countAgencias(AgenciaCriteria criteria) {
        log.debug("REST request to count Agencias by criteria: {}", criteria);
        return ResponseEntity.ok().body(agenciaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agencias/:id} : get the "id" agencia.
     *
     * @param id the id of the agencia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agencia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agencias/{id}")
    public ResponseEntity<Agencia> getAgencia(@PathVariable Long id) {
        log.debug("REST request to get Agencia : {}", id);
        Optional<Agencia> agencia = agenciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agencia);
    }

    /**
     * {@code DELETE  /agencias/:id} : delete the "id" agencia.
     *
     * @param id the id of the agencia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agencias/{id}")
    public ResponseEntity<Void> deleteAgencia(@PathVariable Long id) {
        log.debug("REST request to delete Agencia : {}", id);
        agenciaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
