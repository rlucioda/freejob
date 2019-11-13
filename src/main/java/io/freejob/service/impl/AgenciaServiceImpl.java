package io.freejob.service.impl;

import io.freejob.service.AgenciaService;
import io.freejob.domain.Agencia;
import io.freejob.repository.AgenciaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Agencia}.
 */
@Service
@Transactional
public class AgenciaServiceImpl implements AgenciaService {

    private final Logger log = LoggerFactory.getLogger(AgenciaServiceImpl.class);

    private final AgenciaRepository agenciaRepository;

    public AgenciaServiceImpl(AgenciaRepository agenciaRepository) {
        this.agenciaRepository = agenciaRepository;
    }

    /**
     * Save a agencia.
     *
     * @param agencia the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Agencia save(Agencia agencia) {
        log.debug("Request to save Agencia : {}", agencia);
        return agenciaRepository.save(agencia);
    }

    /**
     * Get all the agencias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Agencia> findAll(Pageable pageable) {
        log.debug("Request to get all Agencias");
        return agenciaRepository.findAll(pageable);
    }


    /**
     * Get one agencia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Agencia> findOne(Long id) {
        log.debug("Request to get Agencia : {}", id);
        return agenciaRepository.findById(id);
    }

    /**
     * Delete the agencia by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Agencia : {}", id);
        agenciaRepository.deleteById(id);
    }
}
