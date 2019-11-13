package io.freejob.service;

import io.freejob.domain.Agencia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Agencia}.
 */
public interface AgenciaService {

    /**
     * Save a agencia.
     *
     * @param agencia the entity to save.
     * @return the persisted entity.
     */
    Agencia save(Agencia agencia);

    /**
     * Get all the agencias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Agencia> findAll(Pageable pageable);


    /**
     * Get the "id" agencia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Agencia> findOne(Long id);

    /**
     * Delete the "id" agencia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
