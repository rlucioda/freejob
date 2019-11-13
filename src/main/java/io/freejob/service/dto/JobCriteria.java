package io.freejob.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.freejob.domain.enumeration.TipoPagamento;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link io.freejob.domain.Job} entity. This class is used
 * in {@link io.freejob.web.rest.JobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TipoPagamento
     */
    public static class TipoPagamentoFilter extends Filter<TipoPagamento> {

        public TipoPagamentoFilter() {
        }

        public TipoPagamentoFilter(TipoPagamentoFilter filter) {
            super(filter);
        }

        @Override
        public TipoPagamentoFilter copy() {
            return new TipoPagamentoFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomeJob;

    private LongFilter valorHora;

    private LongFilter tempoEvento;

    private StringFilter observacao;

    private InstantFilter dataPagamento;

    private TipoPagamentoFilter tipoPagamento;

    private BooleanFilter statusPagamento;

    private LongFilter agenciaId;

    public JobCriteria(){
    }

    public JobCriteria(JobCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.nomeJob = other.nomeJob == null ? null : other.nomeJob.copy();
        this.valorHora = other.valorHora == null ? null : other.valorHora.copy();
        this.tempoEvento = other.tempoEvento == null ? null : other.tempoEvento.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.dataPagamento = other.dataPagamento == null ? null : other.dataPagamento.copy();
        this.tipoPagamento = other.tipoPagamento == null ? null : other.tipoPagamento.copy();
        this.statusPagamento = other.statusPagamento == null ? null : other.statusPagamento.copy();
        this.agenciaId = other.agenciaId == null ? null : other.agenciaId.copy();
    }

    @Override
    public JobCriteria copy() {
        return new JobCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNomeJob() {
        return nomeJob;
    }

    public void setNomeJob(StringFilter nomeJob) {
        this.nomeJob = nomeJob;
    }

    public LongFilter getValorHora() {
        return valorHora;
    }

    public void setValorHora(LongFilter valorHora) {
        this.valorHora = valorHora;
    }

    public LongFilter getTempoEvento() {
        return tempoEvento;
    }

    public void setTempoEvento(LongFilter tempoEvento) {
        this.tempoEvento = tempoEvento;
    }

    public StringFilter getObservacao() {
        return observacao;
    }

    public void setObservacao(StringFilter observacao) {
        this.observacao = observacao;
    }

    public InstantFilter getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(InstantFilter dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public TipoPagamentoFilter getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamentoFilter tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public BooleanFilter getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(BooleanFilter statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public LongFilter getAgenciaId() {
        return agenciaId;
    }

    public void setAgenciaId(LongFilter agenciaId) {
        this.agenciaId = agenciaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JobCriteria that = (JobCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nomeJob, that.nomeJob) &&
            Objects.equals(valorHora, that.valorHora) &&
            Objects.equals(tempoEvento, that.tempoEvento) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(dataPagamento, that.dataPagamento) &&
            Objects.equals(tipoPagamento, that.tipoPagamento) &&
            Objects.equals(statusPagamento, that.statusPagamento) &&
            Objects.equals(agenciaId, that.agenciaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nomeJob,
        valorHora,
        tempoEvento,
        observacao,
        dataPagamento,
        tipoPagamento,
        statusPagamento,
        agenciaId
        );
    }

    @Override
    public String toString() {
        return "JobCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nomeJob != null ? "nomeJob=" + nomeJob + ", " : "") +
                (valorHora != null ? "valorHora=" + valorHora + ", " : "") +
                (tempoEvento != null ? "tempoEvento=" + tempoEvento + ", " : "") +
                (observacao != null ? "observacao=" + observacao + ", " : "") +
                (dataPagamento != null ? "dataPagamento=" + dataPagamento + ", " : "") +
                (tipoPagamento != null ? "tipoPagamento=" + tipoPagamento + ", " : "") +
                (statusPagamento != null ? "statusPagamento=" + statusPagamento + ", " : "") +
                (agenciaId != null ? "agenciaId=" + agenciaId + ", " : "") +
            "}";
    }

}
