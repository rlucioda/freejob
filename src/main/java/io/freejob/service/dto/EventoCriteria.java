package io.freejob.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.freejob.domain.enumeration.TipoEvento;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link io.freejob.domain.Evento} entity. This class is used
 * in {@link io.freejob.web.rest.EventoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /eventos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventoCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TipoEvento
     */
    public static class TipoEventoFilter extends Filter<TipoEvento> {

        public TipoEventoFilter() {
        }

        public TipoEventoFilter(TipoEventoFilter filter) {
            super(filter);
        }

        @Override
        public TipoEventoFilter copy() {
            return new TipoEventoFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomeEvento;

    private StringFilter descricaEvento;

    private InstantFilter dataInicio;

    private InstantFilter dataFim;

    private BooleanFilter statusJob;

    private TipoEventoFilter tipoEvento;

    private LongFilter jobId;

    public EventoCriteria(){
    }

    public EventoCriteria(EventoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.nomeEvento = other.nomeEvento == null ? null : other.nomeEvento.copy();
        this.descricaEvento = other.descricaEvento == null ? null : other.descricaEvento.copy();
        this.dataInicio = other.dataInicio == null ? null : other.dataInicio.copy();
        this.dataFim = other.dataFim == null ? null : other.dataFim.copy();
        this.statusJob = other.statusJob == null ? null : other.statusJob.copy();
        this.tipoEvento = other.tipoEvento == null ? null : other.tipoEvento.copy();
        this.jobId = other.jobId == null ? null : other.jobId.copy();
    }

    @Override
    public EventoCriteria copy() {
        return new EventoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(StringFilter nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public StringFilter getDescricaEvento() {
        return descricaEvento;
    }

    public void setDescricaEvento(StringFilter descricaEvento) {
        this.descricaEvento = descricaEvento;
    }

    public InstantFilter getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(InstantFilter dataInicio) {
        this.dataInicio = dataInicio;
    }

    public InstantFilter getDataFim() {
        return dataFim;
    }

    public void setDataFim(InstantFilter dataFim) {
        this.dataFim = dataFim;
    }

    public BooleanFilter getStatusJob() {
        return statusJob;
    }

    public void setStatusJob(BooleanFilter statusJob) {
        this.statusJob = statusJob;
    }

    public TipoEventoFilter getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEventoFilter tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public LongFilter getJobId() {
        return jobId;
    }

    public void setJobId(LongFilter jobId) {
        this.jobId = jobId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventoCriteria that = (EventoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nomeEvento, that.nomeEvento) &&
            Objects.equals(descricaEvento, that.descricaEvento) &&
            Objects.equals(dataInicio, that.dataInicio) &&
            Objects.equals(dataFim, that.dataFim) &&
            Objects.equals(statusJob, that.statusJob) &&
            Objects.equals(tipoEvento, that.tipoEvento) &&
            Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nomeEvento,
        descricaEvento,
        dataInicio,
        dataFim,
        statusJob,
        tipoEvento,
        jobId
        );
    }

    @Override
    public String toString() {
        return "EventoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nomeEvento != null ? "nomeEvento=" + nomeEvento + ", " : "") +
                (descricaEvento != null ? "descricaEvento=" + descricaEvento + ", " : "") +
                (dataInicio != null ? "dataInicio=" + dataInicio + ", " : "") +
                (dataFim != null ? "dataFim=" + dataFim + ", " : "") +
                (statusJob != null ? "statusJob=" + statusJob + ", " : "") +
                (tipoEvento != null ? "tipoEvento=" + tipoEvento + ", " : "") +
                (jobId != null ? "jobId=" + jobId + ", " : "") +
            "}";
    }

}
