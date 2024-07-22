package br.com.rbsoftware.cadBeneficiario.controller;


import br.com.rbsoftware.cadBeneficiario.infra.controller.ErrorVo;
import br.com.rbsoftware.cadBeneficiario.infra.dto.BuscaBeneficiarioDTO;
import br.com.rbsoftware.cadBeneficiario.infra.dto.BuscaDocumentoDTO;
import br.com.rbsoftware.cadBeneficiario.infra.dto.CriaBeneficiarioDTO;
import br.com.rbsoftware.cadBeneficiario.service.BeneficiarioService;
import br.com.rbsoftware.cadBeneficiario.service.exception.BeneficiarioException;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@RestController
@RequestMapping(value = "v1")
public class BeneficiarioController {


    @Autowired
    private BeneficiarioService service;

    @GetMapping("/beneficiarios/{id}")
    @Secured("USER")
    @Operation(description = "Busca o beneficiarios utilizando o Id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",description = "Retorna o beneficiario",content = @Content(mediaType = "application/json")),@ApiResponse(responseCode = "500", description = "Erro na busca do beneficiario",content = @Content(schema = @Schema(implementation = ErrorVo.class),mediaType = "application/json"))})
    public BuscaBeneficiarioDTO buscaPorId(@PathVariable(value = "id",required = false) Integer idBeneficiario) throws BeneficiarioException {

        return service.buscaBeneficiarioPorId(idBeneficiario);
    }

    @GetMapping("/beneficiarios")
    @Secured("USER")
    @Operation(description = "Busca todos os beneficiarios")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",description = "Retorna os beneficiarios",content = @Content(mediaType = "application/json")),@ApiResponse(responseCode = "500", description = "Erro na busca dos beneficiarios",content = @Content(schema = @Schema(implementation = ErrorVo.class),mediaType = "application/json"))})
    public List<BuscaBeneficiarioDTO> buscaBeneficiarios() throws BeneficiarioException {
        return service.buscaBeneficiarios();
    }

    @GetMapping("/beneficiarios/{id}/documentos")
    @Secured("USER")
    @Operation(description = "Busca os documentos do beneficiario")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",description = "Retorna os documentos",content = @Content(mediaType = "application/json"))
            ,@ApiResponse(responseCode = "500", description = "Erro na busca dos documentos",content = @Content(schema = @Schema(implementation = ErrorVo.class),mediaType = "application/json"))})
    public List<BuscaDocumentoDTO> buscaDocumentos(@PathVariable(value = "id",required = true) Integer idBeneficiario) throws BeneficiarioException {
        return service.buscaDocumentosByIdBeneficiario(idBeneficiario);
    }

    @PostMapping("/beneficiarios")
    @Secured("USER")
    @Operation(description = "Cria um novo beneficiario com os documentos")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",description = "Retorna o beneficiario criado com o id",content = @Content(mediaType = "application/json"))
            ,@ApiResponse(responseCode = "500", description = "Erro na criação do beneficiario",content = @Content(schema = @Schema(implementation = ErrorVo.class),mediaType = "application/json"))})
    public BuscaBeneficiarioDTO criarBeneficiario(@RequestBody(required = true) CriaBeneficiarioDTO beneficiario) throws Exception {
        validar(beneficiario);
        return service.criarBeneficiario(beneficiario);
    }

    @PutMapping("/beneficiarios/{id}")
    @Secured("USER")
    @Operation(description = "Atualiza o beneficiário passando o id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Sucesso",content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro na atualização do beneficiario", content = @Content(schema = @Schema(implementation = ErrorVo.class),mediaType = "application/json"))
    })
    public BuscaBeneficiarioDTO atualizaBeneficiario(@PathVariable(value = "id",required = true) Integer idBeneficiario, @RequestBody(required = true) CriaBeneficiarioDTO beneficiario) throws Exception {
        validar(beneficiario);
        return service.atualizaBeneficiario(idBeneficiario,beneficiario);
    }

    @DeleteMapping("/beneficiarios/{id}")
    @Secured("USER")
    @Operation(description = "Remove o beneficiário pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Sucesso",content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro na remocao do beneficiario",content = @Content(schema = @Schema(implementation = ErrorVo.class),mediaType = "application/json"))})
    public void removerBeneficiario(@PathVariable("id") Integer idBeneficiario) throws Exception {
        service.removerBeneficiario(idBeneficiario);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void validar(Object obj ) throws BeneficiarioException,Exception{
        Class clazz = obj.getClass();
        Field[] flds  = clazz.getDeclaredFields();
        for (int i = 0; i < flds.length; i++) {
            Field fld  = flds[i];
            JsonProperty prop = fld.getAnnotation(JsonProperty.class);
            if(prop != null && prop.required()){
                if(fld.get(obj)== null){
                    throw new BeneficiarioException("O campo "+fld.getName()+" é obrigatório");
                } else {

                    Object objVal = fld.get(obj);
                    if(objVal.getClass().isArray()){
                        Class clazzArr = objVal.getClass();
                        Method methodGet = Array.class.getDeclaredMethod("get", objVal.getClass(), Integer.class);
                        Method methodSize = Array.class.getDeclaredMethod("getLength");
                        int size  = (int) methodSize.invoke(objVal);
                        for (int j = 0; j < size; j++) {
                            validar(methodGet.invoke(objVal,j));
                        }
                    } else if(objVal instanceof List<?>){
                        List lst = (List) objVal;
                        for (Object it:lst){
                            validar(it);
                        }
                    } else if(!isNumeric(objVal.toString()) && !(objVal instanceof String)){
                        validar(objVal);
                    }
                }
            }
        }
    }


    @GetMapping("/gretting")
    @Secured("ANY")
    public String greeting(){

        return "OK";
    }

}
