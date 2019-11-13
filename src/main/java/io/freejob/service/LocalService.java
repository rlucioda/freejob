package io.freejob.service;

import io.freejob.domain.Local;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Local}.
 */
public interface LocalService {

    /**
     * Save a local.
     *
     * @param local the entity to save.
     * @return the persisted entity.
     */
    Local save(Local local);

    /**
     * Get all the locals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Local> findAll(Pageable pageable);


    /**
     * Get the "id" local.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Local> findOne(Long id);

    /**
     * Delete the "id" local.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
