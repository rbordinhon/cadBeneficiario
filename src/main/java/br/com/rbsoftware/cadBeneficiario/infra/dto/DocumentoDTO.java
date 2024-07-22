package br.com.rbsoftware.cadBeneficiario.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentoDTO {

    @JsonProperty(required = true)
    public String tipoDocumento;

    @JsonProperty(required = true)
    public String descricao;

    @JsonProperty(required = true)
    public String dadosDocumento;



}
