package io.freejob.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.freejob.domain.Agencia} entity. This class is used
 * in {@link io.freejob.web.rest.AgenciaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agencias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AgenciaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomeAgencia;

    private StringFilter contatoAgencia;

    private StringFilter email;

    private LongFilter jobId;

    public AgenciaCriteria(){
    }

    public AgenciaCriteria(AgenciaCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.nomeAgencia = other.nomeAgencia == null ? null : other.nomeAgencia.copy();
        this.contatoAgencia = other.contatoAgencia == null ? null : other.contatoAgencia.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.jobId = other.jobId == null ? null : other.jobId.copy();
    }

    @Override
    public AgenciaCriteria copy() {
        return new AgenciaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNomeAgencia() {
        return nomeAgencia;
    }

    public void setNomeAgencia(StringFilter nomeAgencia) {
        this.nomeAgencia = nomeAgencia;
    }

    public StringFilter getContatoAgencia() {
        return contatoAgencia;
    }

    public void setContatoAgencia(StringFilter contatoAgencia) {
        this.contatoAgencia = contatoAgencia;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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
        final AgenciaCriteria that = (AgenciaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nomeAgencia, that.nomeAgencia) &&
            Objects.equals(contatoAgencia, that.contatoAgencia) &&
            Objects.equals(email, that.email) &&
            Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nomeAgencia,
        contatoAgencia,
        email,
        jobId
        );
    }

    @Override
    public String toString() {
        return "AgenciaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nomeAgencia != null ? "nomeAgencia=" + nomeAgencia + ", " : "") +
                (contatoAgencia != null ? "contatoAgencia=" + contatoAgencia + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (jobId != null ? "jobId=" + jobId + ", " : "") +
            "}";
    }

}
