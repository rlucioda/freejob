package io.freejob.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import io.freejob.domain.enumeration.TipoLocal;

/**
 * A Local.
 */
@Entity
@Table(name = "local")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Local implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nome_local", length = 50, nullable = false, unique = true)
    private String nomeLocal;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_local")
    private TipoLocal tipoLocal;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(unique = true)
    private Evento evento;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public Local nomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
        return this;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public TipoLocal getTipoLocal() {
        return tipoLocal;
    }

    public Local tipoLocal(TipoLocal tipoLocal) {
        this.tipoLocal = tipoLocal;
        return this;
    }

    public void setTipoLocal(TipoLocal tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public Evento getEvento() {
        return evento;
    }

    public Local evento(Evento evento) {
        this.evento = evento;
        return this;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Local)) {
            return false;
        }
        return id != null && id.equals(((Local) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Local{" +
            "id=" + getId() +
            ", nomeLocal='" + getNomeLocal() + "'" +
            ", tipoLocal='" + getTipoLocal() + "'" +
            "}";
    }
}
