package io.freejob.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import io.freejob.domain.enumeration.TipoEvento;

/**
 * A Evento.
 */
@Entity
@Table(name = "evento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nome_evento", length = 50, nullable = false, unique = true)
    private String nomeEvento;

    @Column(name = "descrica_evento")
    private String descricaEvento;

    @NotNull
    @Column(name = "data_inicio", nullable = false)
    private Instant dataInicio;

    @NotNull
    @Column(name = "data_fim", nullable = false)
    private Instant dataFim;

    @Column(name = "status_job")
    private Boolean statusJob;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evento")
    private TipoEvento tipoEvento;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(unique = true)
    private Job job;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public Evento nomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
        return this;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getDescricaEvento() {
        return descricaEvento;
    }

    public Evento descricaEvento(String descricaEvento) {
        this.descricaEvento = descricaEvento;
        return this;
    }

    public void setDescricaEvento(String descricaEvento) {
        this.descricaEvento = descricaEvento;
    }

    public Instant getDataInicio() {
        return dataInicio;
    }

    public Evento dataInicio(Instant dataInicio) {
        this.dataInicio = dataInicio;
        return this;
    }

    public void setDataInicio(Instant dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Instant getDataFim() {
        return dataFim;
    }

    public Evento dataFim(Instant dataFim) {
        this.dataFim = dataFim;
        return this;
    }

    public void setDataFim(Instant dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean isStatusJob() {
        return statusJob;
    }

    public Evento statusJob(Boolean statusJob) {
        this.statusJob = statusJob;
        return this;
    }

    public void setStatusJob(Boolean statusJob) {
        this.statusJob = statusJob;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public Evento tipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
        return this;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public Job getJob() {
        return job;
    }

    public Evento job(Job job) {
        this.job = job;
        return this;
    }

    public void setJob(Job job) {
        this.job = job;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evento)) {
            return false;
        }
        return id != null && id.equals(((Evento) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Evento{" +
            "id=" + getId() +
            ", nomeEvento='" + getNomeEvento() + "'" +
            ", descricaEvento='" + getDescricaEvento() + "'" +
            ", dataInicio='" + getDataInicio() + "'" +
            ", dataFim='" + getDataFim() + "'" +
            ", statusJob='" + isStatusJob() + "'" +
            ", tipoEvento='" + getTipoEvento() + "'" +
            "}";
    }
}
