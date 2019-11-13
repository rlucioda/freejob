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

import io.freejob.domain.Local;
import io.freejob.domain.*; // for static metamodels
import io.freejob.repository.LocalRepository;
import io.freejob.service.dto.LocalCriteria;

/**
 * Service for executing complex queries for {@link Local} entities in the database.
 * The main input is a {@link LocalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Local} or a {@link Page} of {@link Local} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocalQueryService extends QueryService<Local> {

    private final Logger log = LoggerFactory.getLogger(LocalQueryService.class);

    private final LocalRepository localRepository;

    public LocalQueryService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    /**
     * Return a {@link List} of {@link Local} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Local> findByCriteria(LocalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Local> specification = createSpecification(criteria);
        return localRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Local} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Local> findByCriteria(LocalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Local> specification = createSpecification(criteria);
        return localRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LocalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Local> specification = createSpecification(criteria);
        return localRepository.count(specification);
    }

    /**
     * Function to convert {@link LocalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Local> createSpecification(LocalCriteria criteria) {
        Specification<Local> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Local_.id));
            }
            if (criteria.getNomeLocal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomeLocal(), Local_.nomeLocal));
            }
            if (criteria.getTipoLocal() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoLocal(), Local_.tipoLocal));
            }
            if (criteria.getEventoId() != null) {
                specification = specification.and(buildSpecification(criteria.getEventoId(),
                    root -> root.join(Local_.evento, JoinType.LEFT).get(Evento_.id)));
            }
        }
        return specification;
    }
}
