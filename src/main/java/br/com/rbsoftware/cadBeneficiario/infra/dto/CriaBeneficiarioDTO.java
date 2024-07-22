package br.com.rbsoftware.cadBeneficiario.infra.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class CriaBeneficiarioDTO {

  public CriaBeneficiarioDTO(){

  }

    @JsonProperty(required = true)
    public String nome;

    @JsonProperty(required = true)
    public String telefone;

    @JsonProperty(required = true)
    public String dataNascimento;

    @JsonProperty(required = true)
    public List<DocumentoDTO> documentos;


}
