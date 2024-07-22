package br.com.rbsoftware.cadBeneficiario.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Table(name = "documento")
@Entity
public class Documento {

    @Id
    @Column(name = "id_documento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="id_beneficiario", nullable=false)
    private Beneficiario beneficiario;

    @Column(name = "ds_tipoDocumento")
    private String tipoDocumento;

    @Column(name = "ds_documento")
    private String descricao;

    @Column(name = "bl_dados_documento")
    @Lob
    private byte[] dadosDocumento;

    @Column(name = "dt_atualizacao")
    private LocalDateTime dataInclusao;
    @Column(name = "dt_inclusao")
    private LocalDateTime dataAtualizacao;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Beneficiario getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getDadosDocumento() {
        return dadosDocumento;
    }

    public void setDadosDocumento(byte[] dadosDocumento) {
        this.dadosDocumento = dadosDocumento;
    }

    public LocalDateTime getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(LocalDateTime dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Documento documento = (Documento) o;
        return id == documento.id && Objects.equals(beneficiario, documento.beneficiario) && Objects.equals(tipoDocumento, documento.tipoDocumento) && Objects.equals(descricao, documento.descricao) && Objects.deepEquals(dadosDocumento, documento.dadosDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, beneficiario, tipoDocumento, descricao, Arrays.hashCode(dadosDocumento));
    }
}
