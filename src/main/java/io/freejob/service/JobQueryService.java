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

import io.freejob.domain.Job;
import io.freejob.domain.*; // for static metamodels
import io.freejob.repository.JobRepository;
import io.freejob.service.dto.JobCriteria;

/**
 * Service for executing complex queries for {@link Job} entities in the database.
 * The main input is a {@link JobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Job} or a {@link Page} of {@link Job} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobQueryService extends QueryService<Job> {

    private final Logger log = LoggerFactory.getLogger(JobQueryService.class);

    private final JobRepository jobRepository;

    public JobQueryService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Return a {@link List} of {@link Job} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Job> findByCriteria(JobCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Job} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Job> findByCriteria(JobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.count(specification);
    }

    /**
     * Function to convert {@link JobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Job> createSpecification(JobCriteria criteria) {
        Specification<Job> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Job_.id));
            }
            if (criteria.getNomeJob() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomeJob(), Job_.nomeJob));
            }
            if (criteria.getValorHora() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorHora(), Job_.valorHora));
            }
            if (criteria.getTempoEvento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTempoEvento(), Job_.tempoEvento));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), Job_.observacao));
            }
            if (criteria.getDataPagamento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataPagamento(), Job_.dataPagamento));
            }
            if (criteria.getTipoPagamento() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoPagamento(), Job_.tipoPagamento));
            }
            if (criteria.getStatusPagamento() != null) {
                specification = specification.and(buildSpecification(criteria.getStatusPagamento(), Job_.statusPagamento));
            }
            if (criteria.getAgenciaId() != null) {
                specification = specification.and(buildSpecification(criteria.getAgenciaId(),
                    root -> root.join(Job_.agencia, JoinType.LEFT).get(Agencia_.id)));
            }
        }
        return specification;
    }
}
