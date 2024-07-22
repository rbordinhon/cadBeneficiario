package br.com.rbsoftware.cadBeneficiario.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

public class DocumentoDTO {

    @JsonProperty(required = true)
    public String tipoDocumento;

    @JsonProperty(required = true)
    public String descricao;

    @JsonProperty(required = true)
    @Schema(
            type = "string",
            format = "uuid",
            description = "Dados dos bytes do documento em base 64",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "Schema example"
    )
    public String dadosDocumento;



}
