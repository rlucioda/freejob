package io.freejob.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import io.freejob.domain.enumeration.TipoPagamento;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nome_job", length = 100, nullable = false, unique = true)
    private String nomeJob;

    @Column(name = "valor_hora")
    private Long valorHora;

    @Column(name = "tempo_evento")
    private Long tempoEvento;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "data_pagamento")
    private Instant dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagamento")
    private TipoPagamento tipoPagamento;

    @Column(name = "status_pagamento")
    private Boolean statusPagamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JsonIgnoreProperties("jobs")
    private Agencia agencia;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeJob() {
        return nomeJob;
    }

    public Job nomeJob(String nomeJob) {
        this.nomeJob = nomeJob;
        return this;
    }

    public void setNomeJob(String nomeJob) {
        this.nomeJob = nomeJob;
    }

    public Long getValorHora() {
        return valorHora;
    }

    public Job valorHora(Long valorHora) {
        this.valorHora = valorHora;
        return this;
    }

    public void setValorHora(Long valorHora) {
        this.valorHora = valorHora;
    }

    public Long getTempoEvento() {
        return tempoEvento;
    }

    public Job tempoEvento(Long tempoEvento) {
        this.tempoEvento = tempoEvento;
        return this;
    }

    public void setTempoEvento(Long tempoEvento) {
        this.tempoEvento = tempoEvento;
    }

    public String getObservacao() {
        return observacao;
    }

    public Job observacao(String observacao) {
        this.observacao = observacao;
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Instant getDataPagamento() {
        return dataPagamento;
    }

    public Job dataPagamento(Instant dataPagamento) {
        this.dataPagamento = dataPagamento;
        return this;
    }

    public void setDataPagamento(Instant dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public Job tipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
        return this;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Boolean isStatusPagamento() {
        return statusPagamento;
    }

    public Job statusPagamento(Boolean statusPagamento) {
        this.statusPagamento = statusPagamento;
        return this;
    }

    public void setStatusPagamento(Boolean statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public Job agencia(Agencia agencia) {
        this.agencia = agencia;
        return this;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }
        return id != null && id.equals(((Job) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", nomeJob='" + getNomeJob() + "'" +
            ", valorHora=" + getValorHora() +
            ", tempoEvento=" + getTempoEvento() +
            ", observacao='" + getObservacao() + "'" +
            ", dataPagamento='" + getDataPagamento() + "'" +
            ", tipoPagamento='" + getTipoPagamento() + "'" +
            ", statusPagamento='" + isStatusPagamento() + "'" +
            "}";
    }
}
