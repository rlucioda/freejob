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

import io.freejob.domain.Agencia;
import io.freejob.domain.*; // for static metamodels
import io.freejob.repository.AgenciaRepository;
import io.freejob.service.dto.AgenciaCriteria;

/**
 * Service for executing complex queries for {@link Agencia} entities in the database.
 * The main input is a {@link AgenciaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Agencia} or a {@link Page} of {@link Agencia} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgenciaQueryService extends QueryService<Agencia> {

    private final Logger log = LoggerFactory.getLogger(AgenciaQueryService.class);

    private final AgenciaRepository agenciaRepository;

    public AgenciaQueryService(AgenciaRepository agenciaRepository) {
        this.agenciaRepository = agenciaRepository;
    }

    /**
     * Return a {@link List} of {@link Agencia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Agencia> findByCriteria(AgenciaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Agencia> specification = createSpecification(criteria);
        return agenciaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Agencia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Agencia> findByCriteria(AgenciaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Agencia> specification = createSpecification(criteria);
        return agenciaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgenciaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Agencia> specification = createSpecification(criteria);
        return agenciaRepository.count(specification);
    }

    /**
     * Function to convert {@link AgenciaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Agencia> createSpecification(AgenciaCriteria criteria) {
        Specification<Agencia> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Agencia_.id));
            }
            if (criteria.getNomeAgencia() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomeAgencia(), Agencia_.nomeAgencia));
            }
            if (criteria.getContatoAgencia() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContatoAgencia(), Agencia_.contatoAgencia));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Agencia_.email));
            }
            if (criteria.getJobId() != null) {
                specification = specification.and(buildSpecification(criteria.getJobId(),
                    root -> root.join(Agencia_.jobs, JoinType.LEFT).get(Job_.id)));
            }
        }
        return specification;
    }
}
