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
 * Criteria class for the {@link io.freejob.domain.Endereco} entity. This class is used
 * in {@link io.freejob.web.rest.EnderecoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /enderecos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EnderecoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter rua;

    private StringFilter numero;

    private StringFilter complemento;

    private StringFilter bairro;

    private StringFilter cep;

    private StringFilter cidade;

    private StringFilter estado;

    private LongFilter localId;

    public EnderecoCriteria(){
    }

    public EnderecoCriteria(EnderecoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.rua = other.rua == null ? null : other.rua.copy();
        this.numero = other.numero == null ? null : other.numero.copy();
        this.complemento = other.complemento == null ? null : other.complemento.copy();
        this.bairro = other.bairro == null ? null : other.bairro.copy();
        this.cep = other.cep == null ? null : other.cep.copy();
        this.cidade = other.cidade == null ? null : other.cidade.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.localId = other.localId == null ? null : other.localId.copy();
    }

    @Override
    public EnderecoCriteria copy() {
        return new EnderecoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRua() {
        return rua;
    }

    public void setRua(StringFilter rua) {
        this.rua = rua;
    }

    public StringFilter getNumero() {
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public StringFilter getComplemento() {
        return complemento;
    }

    public void setComplemento(StringFilter complemento) {
        this.complemento = complemento;
    }

    public StringFilter getBairro() {
        return bairro;
    }

    public void setBairro(StringFilter bairro) {
        this.bairro = bairro;
    }

    public StringFilter getCep() {
        return cep;
    }

    public void setCep(StringFilter cep) {
        this.cep = cep;
    }

    public StringFilter getCidade() {
        return cidade;
    }

    public void setCidade(StringFilter cidade) {
        this.cidade = cidade;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public LongFilter getLocalId() {
        return localId;
    }

    public void setLocalId(LongFilter localId) {
        this.localId = localId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EnderecoCriteria that = (EnderecoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(rua, that.rua) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(complemento, that.complemento) &&
            Objects.equals(bairro, that.bairro) &&
            Objects.equals(cep, that.cep) &&
            Objects.equals(cidade, that.cidade) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(localId, that.localId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        rua,
        numero,
        complemento,
        bairro,
        cep,
        cidade,
        estado,
        localId
        );
    }

    @Override
    public String toString() {
        return "EnderecoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (rua != null ? "rua=" + rua + ", " : "") +
                (numero != null ? "numero=" + numero + ", " : "") +
                (complemento != null ? "complemento=" + complemento + ", " : "") +
                (bairro != null ? "bairro=" + bairro + ", " : "") +
                (cep != null ? "cep=" + cep + ", " : "") +
                (cidade != null ? "cidade=" + cidade + ", " : "") +
                (estado != null ? "estado=" + estado + ", " : "") +
                (localId != null ? "localId=" + localId + ", " : "") +
            "}";
    }

}
