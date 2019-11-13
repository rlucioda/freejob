package io.freejob.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.freejob.domain.enumeration.TipoLocal;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.freejob.domain.Local} entity. This class is used
 * in {@link io.freejob.web.rest.LocalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocalCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TipoLocal
     */
    public static class TipoLocalFilter extends Filter<TipoLocal> {

        public TipoLocalFilter() {
        }

        public TipoLocalFilter(TipoLocalFilter filter) {
            super(filter);
        }

        @Override
        public TipoLocalFilter copy() {
            return new TipoLocalFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomeLocal;

    private TipoLocalFilter tipoLocal;

    private LongFilter eventoId;

    public LocalCriteria(){
    }

    public LocalCriteria(LocalCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.nomeLocal = other.nomeLocal == null ? null : other.nomeLocal.copy();
        this.tipoLocal = other.tipoLocal == null ? null : other.tipoLocal.copy();
        this.eventoId = other.eventoId == null ? null : other.eventoId.copy();
    }

    @Override
    public LocalCriteria copy() {
        return new LocalCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(StringFilter nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public TipoLocalFilter getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocal(TipoLocalFilter tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public LongFilter getEventoId() {
        return eventoId;
    }

    public void setEventoId(LongFilter eventoId) {
        this.eventoId = eventoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LocalCriteria that = (LocalCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nomeLocal, that.nomeLocal) &&
            Objects.equals(tipoLocal, that.tipoLocal) &&
            Objects.equals(eventoId, that.eventoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nomeLocal,
        tipoLocal,
        eventoId
        );
    }

    @Override
    public String toString() {
        return "LocalCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nomeLocal != null ? "nomeLocal=" + nomeLocal + ", " : "") +
                (tipoLocal != null ? "tipoLocal=" + tipoLocal + ", " : "") +
                (eventoId != null ? "eventoId=" + eventoId + ", " : "") +
            "}";
    }

}
