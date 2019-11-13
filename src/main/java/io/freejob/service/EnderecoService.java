package io.freejob.service;

import io.freejob.domain.Endereco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Endereco}.
 */
public interface EnderecoService {

    /**
     * Save a endereco.
     *
     * @param endereco the entity to save.
     * @return the persisted entity.
     */
    Endereco save(Endereco endereco);

    /**
     * Get all the enderecos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Endereco> findAll(Pageable pageable);


    /**
     * Get the "id" endereco.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Endereco> findOne(Long id);

    /**
     * Delete the "id" endereco.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
