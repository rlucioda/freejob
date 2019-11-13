package io.freejob.service.impl;

import io.freejob.service.EventoService;
import io.freejob.domain.Evento;
import io.freejob.repository.EventoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Evento}.
 */
@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    private final Logger log = LoggerFactory.getLogger(EventoServiceImpl.class);

    private final EventoRepository eventoRepository;

    public EventoServiceImpl(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    /**
     * Save a evento.
     *
     * @param evento the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Evento save(Evento evento) {
        log.debug("Request to save Evento : {}", evento);
        return eventoRepository.save(evento);
    }

    /**
     * Get all the eventos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Evento> findAll(Pageable pageable) {
        log.debug("Request to get all Eventos");
        return eventoRepository.findAll(pageable);
    }


    /**
     * Get one evento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Evento> findOne(Long id) {
        log.debug("Request to get Evento : {}", id);
        return eventoRepository.findById(id);
    }

    /**
     * Delete the evento by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Evento : {}", id);
        eventoRepository.deleteById(id);
    }
}
