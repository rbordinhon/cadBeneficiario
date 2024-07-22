package br.com.rbsoftware.cadBeneficiario.domain;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Beneficiario")
public class Beneficiario {


    @Id
    @Column(name = "id_beneficiario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ds_nome")
    private String nome;

    @Column(name = "ds_descricao")
    private String telefone;

    @Column(name = "dt_nascimento")
    private LocalDate dataNascimento;

    @Column (name = "dt_inclusao")
    private LocalDateTime dataInclusao;

    @Column (name = "dt_atualizacao")
    private LocalDateTime dataAtualizacao;


    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(joinColumns = @JoinColumn(name = "id_beneficiario",referencedColumnName = "id_beneficiario") )
    private List<Documento> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public void setItems(List<Documento> items) {
        this.items = items;
    }

    public List<Documento> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beneficiario that = (Beneficiario) o;
        return id == that.id && Objects.equals(nome, that.nome) && Objects.equals(telefone, that.telefone) && Objects.equals(dataNascimento, that.dataNascimento) && Objects.equals(dataInclusao, that.dataInclusao) && Objects.equals(dataAtualizacao, that.dataAtualizacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, telefone, dataNascimento, dataInclusao, dataAtualizacao);
    }
}
