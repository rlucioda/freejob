package io.freejob.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Agencia.
 */
@Entity
@Table(name = "agencia")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Agencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nome_agencia", length = 50, nullable = false, unique = true)
    private String nomeAgencia;

    @Column(name = "contato_agencia")
    private String contatoAgencia;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "agencia")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Job> jobs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeAgencia() {
        return nomeAgencia;
    }

    public Agencia nomeAgencia(String nomeAgencia) {
        this.nomeAgencia = nomeAgencia;
        return this;
    }

    public void setNomeAgencia(String nomeAgencia) {
        this.nomeAgencia = nomeAgencia;
    }

    public String getContatoAgencia() {
        return contatoAgencia;
    }

    public Agencia contatoAgencia(String contatoAgencia) {
        this.contatoAgencia = contatoAgencia;
        return this;
    }

    public void setContatoAgencia(String contatoAgencia) {
        this.contatoAgencia = contatoAgencia;
    }

    public String getEmail() {
        return email;
    }

    public Agencia email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public Agencia jobs(Set<Job> jobs) {
        this.jobs = jobs;
        return this;
    }

    public Agencia addJob(Job job) {
        this.jobs.add(job);
        job.setAgencia(this);
        return this;
    }

    public Agencia removeJob(Job job) {
        this.jobs.remove(job);
        job.setAgencia(null);
        return this;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agencia)) {
            return false;
        }
        return id != null && id.equals(((Agencia) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Agencia{" +
            "id=" + getId() +
            ", nomeAgencia='" + getNomeAgencia() + "'" +
            ", contatoAgencia='" + getContatoAgencia() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
