package io.freejob.web.rest;

import io.freejob.domain.Local;
import io.freejob.service.LocalService;
import io.freejob.web.rest.errors.BadRequestAlertException;
import io.freejob.service.dto.LocalCriteria;
import io.freejob.service.LocalQueryService;

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
 * REST controller for managing {@link io.freejob.domain.Local}.
 */
@RestController
@RequestMapping("/api")
public class LocalResource {

    private final Logger log = LoggerFactory.getLogger(LocalResource.class);

    private static final String ENTITY_NAME = "local";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocalService localService;

    private final LocalQueryService localQueryService;

    public LocalResource(LocalService localService, LocalQueryService localQueryService) {
        this.localService = localService;
        this.localQueryService = localQueryService;
    }

    /**
     * {@code POST  /locals} : Create a new local.
     *
     * @param local the local to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new local, or with status {@code 400 (Bad Request)} if the local has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locals")
    public ResponseEntity<Local> createLocal(@Valid @RequestBody Local local) throws URISyntaxException {
        log.debug("REST request to save Local : {}", local);
        if (local.getId() != null) {
            throw new BadRequestAlertException("A new local cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Local result = localService.save(local);
        return ResponseEntity.created(new URI("/api/locals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /locals} : Updates an existing local.
     *
     * @param local the local to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated local,
     * or with status {@code 400 (Bad Request)} if the local is not valid,
     * or with status {@code 500 (Internal Server Error)} if the local couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locals")
    public ResponseEntity<Local> updateLocal(@Valid @RequestBody Local local) throws URISyntaxException {
        log.debug("REST request to update Local : {}", local);
        if (local.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Local result = localService.save(local);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, local.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /locals} : get all the locals.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locals in body.
     */
    @GetMapping("/locals")
    public ResponseEntity<List<Local>> getAllLocals(LocalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Locals by criteria: {}", criteria);
        Page<Local> page = localQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /locals/count} : count all the locals.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/locals/count")
    public ResponseEntity<Long> countLocals(LocalCriteria criteria) {
        log.debug("REST request to count Locals by criteria: {}", criteria);
        return ResponseEntity.ok().body(localQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /locals/:id} : get the "id" local.
     *
     * @param id the id of the local to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the local, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/locals/{id}")
    public ResponseEntity<Local> getLocal(@PathVariable Long id) {
        log.debug("REST request to get Local : {}", id);
        Optional<Local> local = localService.findOne(id);
        return ResponseUtil.wrapOrNotFound(local);
    }

    /**
     * {@code DELETE  /locals/:id} : delete the "id" local.
     *
     * @param id the id of the local to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locals/{id}")
    public ResponseEntity<Void> deleteLocal(@PathVariable Long id) {
        log.debug("REST request to delete Local : {}", id);
        localService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
