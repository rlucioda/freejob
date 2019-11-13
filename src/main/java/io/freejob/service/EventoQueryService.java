package io.freejob.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.freejob.domain.Evento;
import io.freejob.domain.*; // for static metamodels
import io.freejob.repository.EventoRepository;
import io.freejob.service.dto.EventoCriteria;

/**
 * Service for executing complex queries for {@link Evento} entities in the database.
 * The main input is a {@link EventoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Evento} or a {@link Page} of {@link Evento} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventoQueryService extends QueryService<Evento> {

    private final Logger log = LoggerFactory.getLogger(EventoQueryService.class);

    private final EventoRepository eventoRepository;

    public EventoQueryService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    /**
     * Return a {@link List} of {@link Evento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Evento> findByCriteria(EventoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Evento> specification = createSpecification(criteria);
        return eventoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Evento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Evento> findByCriteria(EventoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Evento> specification = createSpecification(criteria);
        return eventoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Evento> specification = createSpecification(criteria);
        return eventoRepository.count(specification);
    }

    /**
     * Function to convert {@link EventoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Evento> createSpecification(EventoCriteria criteria) {
        Specification<Evento> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Evento_.id));
            }
            if (criteria.getNomeEvento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomeEvento(), Evento_.nomeEvento));
            }
            if (criteria.getDescricaEvento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricaEvento(), Evento_.descricaEvento));
            }
            if (criteria.getDataInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataInicio(), Evento_.dataInicio));
            }
            if (criteria.getDataFim() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataFim(), Evento_.dataFim));
            }
            if (criteria.getStatusJob() != null) {
                specification = specification.and(buildSpecification(criteria.getStatusJob(), Evento_.statusJob));
            }
            if (criteria.getTipoEvento() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoEvento(), Evento_.tipoEvento));
            }
            if (criteria.getJobId() != null) {
                specification = specification.and(buildSpecification(criteria.getJobId(),
                    root -> root.join(Evento_.job, JoinType.LEFT).get(Job_.id)));
            }
        }
        return specification;
    }
}
