package br.com.rbsoftware.cadBeneficiario;

import br.com.rbsoftware.cadBeneficiario.CadBeneficiarioApplication;
import br.com.rbsoftware.cadBeneficiario.infra.dto.BuscaBeneficiarioDTO;
import br.com.rbsoftware.cadBeneficiario.infra.dto.BuscaDocumentoDTO;
import br.com.rbsoftware.cadBeneficiario.infra.dto.CriaBeneficiarioDTO;
import br.com.rbsoftware.cadBeneficiario.infra.dto.DocumentoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.JettyClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CadBeneficiarioIntegrationTeste {

    @BeforeAll
    public static void beforeall(){
      SpringApplication.run(CadBeneficiarioApplication.class, new String[]{});
    }



    private static BuscaBeneficiarioDTO beneficioCorrente;
    private static CriaBeneficiarioDTO beneficioCriado;


    @Test
    @Order(1)
    void criarBeneficio() throws Exception{



        // create headers


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        beneficioCriado  = new CriaBeneficiarioDTO();
        beneficioCriado.telefone="11970706006";
        beneficioCriado.nome="Rodrigo";
        beneficioCriado.dataNascimento= "13/05/1980";
        List<DocumentoDTO> docs = new ArrayList<>();
        DocumentoDTO documento = new DocumentoDTO();
        documento.tipoDocumento="properties";
        documento.descricao="application";
        byte[] documentoDados = Files.readAllBytes(Paths.get(ClassLoader.getSystemClassLoader().getResource("application.properties").toURI()));
        documento.dadosDocumento= Base64.getEncoder().encodeToString(documentoDados);
        docs.add(documento);
        documento = new DocumentoDTO();
        documento.tipoDocumento="pdf";
        documento.descricao="Curriculo";
        documentoDados = Files.readAllBytes(Paths.get(ClassLoader.getSystemClassLoader().getResource("Curriculo.pdf").toURI()));
        documento.dadosDocumento= Base64.getEncoder().encodeToString(documentoDados);
        docs.add(documento);
        beneficioCriado.documentos =docs;
        beneficioCorrente = executeHttp("beneficiarios","POST",beneficioCriado, BuscaBeneficiarioDTO.class);
        assertEquals(beneficioCriado.telefone,beneficioCorrente.telefone);
        assertEquals(beneficioCriado.dataNascimento,beneficioCorrente.dataNascimento);
        assertEquals(beneficioCriado.nome,beneficioCorrente.nome);
        assertTrue(convertDate(beneficioCorrente.dataInclusao).isBefore(LocalDateTime.now()));
        assertEquals(beneficioCorrente.dataInclusao,beneficioCorrente.dataAtualizacao);




    }

    @Test
    @Order(2)
    void criarBeneficio2() throws Exception{



        // create headers


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        CriaBeneficiarioDTO beneficioCriado  = new CriaBeneficiarioDTO();
        beneficioCriado.telefone="11970706001";
        beneficioCriado.nome="Roberval";
        beneficioCriado.dataNascimento= "13/05/1981";
        List<DocumentoDTO> docs = new ArrayList<>();
        DocumentoDTO documento = new DocumentoDTO();
        documento.tipoDocumento="properties";
        documento.descricao="suez";
        byte[] documentoDados = Files.readAllBytes(Paths.get(ClassLoader.getSystemClassLoader().getResource("application.properties").toURI()));
        documento.dadosDocumento= Base64.getEncoder().encodeToString(documentoDados);
        docs.add(documento);
        documento = new DocumentoDTO();
        documento.tipoDocumento="pdf";
        documento.descricao="CurriculoP";
        documentoDados = Files.readAllBytes(Paths.get(ClassLoader.getSystemClassLoader().getResource("Curriculo.pdf").toURI()));
        documento.dadosDocumento= Base64.getEncoder().encodeToString(documentoDados);
        docs.add(documento);
        beneficioCriado.documentos =docs;
        BuscaBeneficiarioDTO beneficioCorrente = executeHttp("beneficiarios","POST",beneficioCriado, BuscaBeneficiarioDTO.class);
        assertEquals(beneficioCriado.telefone,beneficioCorrente.telefone);
        assertEquals(beneficioCriado.dataNascimento,beneficioCorrente.dataNascimento);
        assertEquals(beneficioCriado.nome,beneficioCorrente.nome);
        assertTrue(convertDate(beneficioCorrente.dataInclusao).isBefore(LocalDateTime.now()));
        assertEquals(beneficioCorrente.dataInclusao,beneficioCorrente.dataAtualizacao);




    }

    private static LocalDateTime convertDate(String data){
        return LocalDateTime.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    @Test
    @Order(3)
    public void verificaCricaoBusca() throws JsonProcessingException{
        chamadaBUscaPOrId(false);
    }


	private void chamadaBUscaPOrId(boolean atualizacao) throws JsonProcessingException{



        // create headers


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        BuscaBeneficiarioDTO bene = executeHttp("beneficiarios/"+beneficioCorrente.id,"GET", BuscaBeneficiarioDTO.class);
        assertEquals(beneficioCriado.telefone,bene.telefone);
        assertEquals(beneficioCriado.dataNascimento,bene.dataNascimento);
        assertEquals(beneficioCriado.nome,bene.nome);
        if(!atualizacao){
            assertEquals(beneficioCorrente.dataAtualizacao,bene.dataAtualizacao);
            assertEquals(beneficioCorrente.dataInclusao,bene.dataAtualizacao);
        } else {
            assertTrue(convertDate(beneficioCorrente.dataAtualizacao).isBefore(convertDate(bene.dataAtualizacao)));
            assertEquals(beneficioCorrente.dataInclusao,bene.dataInclusao);
        }
        assertEquals(beneficioCorrente.id,bene.id);

        BuscaDocumentoDTO[] documentos =  executeHttp("beneficiarios/"+beneficioCorrente.id+"/documentos","GET", BuscaDocumentoDTO[].class);
        assertEquals(beneficioCriado.documentos.size(),documentos.length);
        for (BuscaDocumentoDTO docIt:documentos){
            Optional<DocumentoDTO> docCriado  =  beneficioCriado.documentos.stream().filter((doc)->{
                return doc.descricao.equals(docIt.descricao) && doc.tipoDocumento.equals(docIt.tipoDocumento);
            }).findFirst();
            assertTrue(docCriado.isPresent());
            DocumentoDTO criado  =  docCriado.get();
            assertEquals(criado.dadosDocumento,docIt.dadosDocumento);
            assertTrue(convertDate(docIt.dataAtualizacao).isBefore(LocalDateTime.now()));
            if(docIt.idDocumento == 1 && atualizacao){
                assertTrue(convertDate(docIt.dataInclusao).isBefore(convertDate(docIt.dataAtualizacao)));
            } else {
                assertEquals(docIt.dataInclusao,docIt.dataAtualizacao);
            }
        }


	}

    @Test
    @Order(4)
    void atualizarBeneficiario() throws Exception{



        // create headers


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        beneficioCriado  = new CriaBeneficiarioDTO();
        beneficioCriado.telefone="11977706006";
        beneficioCriado.nome="Rodrigo Bordinhon";
        beneficioCriado.dataNascimento= "13/05/1980";
        List<DocumentoDTO> docs = new ArrayList<>();
        DocumentoDTO documento = new DocumentoDTO();
        documento.tipoDocumento="properties";
        documento.descricao="application";
        byte[] documentoDados = Files.readAllBytes(Paths.get(ClassLoader.getSystemClassLoader().getResource("application.properties").toURI()));
        documento.dadosDocumento= Base64.getEncoder().encodeToString(documentoDados);
        docs.add(documento);
        documento = new DocumentoDTO();
        documento.tipoDocumento="pdf";
        documento.descricao="avaliacao_ekan";
        documentoDados = Files.readAllBytes(Paths.get(ClassLoader.getSystemClassLoader().getResource("avaliacao_ekan.pdf").toURI()));
        documento.dadosDocumento= Base64.getEncoder().encodeToString(documentoDados);
        docs.add(documento);
        documento = new DocumentoDTO();
        documento.tipoDocumento="md";
        documento.descricao="Readme";
        documentoDados = Files.readAllBytes(Paths.get(ClassLoader.getSystemClassLoader().getResource("Readme.md").toURI()));
        documento.dadosDocumento= Base64.getEncoder().encodeToString(documentoDados);
        docs.add(documento);
        beneficioCriado.documentos =docs;

        BuscaBeneficiarioDTO bene = executeHttp("beneficiarios/"+beneficioCorrente.id,"PUT",beneficioCriado, BuscaBeneficiarioDTO.class);
        assertEquals(beneficioCriado.telefone,bene.telefone);
        assertEquals(beneficioCriado.dataNascimento,bene.dataNascimento);
        assertEquals(beneficioCriado.nome,bene.nome);
        assertNotEquals(beneficioCorrente.dataAtualizacao,bene.dataAtualizacao);
        assertTrue(convertDate(beneficioCorrente.dataAtualizacao).isBefore(convertDate(bene.dataAtualizacao)));
        assertEquals(beneficioCorrente.dataInclusao,bene.dataInclusao);
        assertEquals(beneficioCorrente.id,bene.id);

        System.out.printf(bene.toString());

    }

    @Test
    @Order(5)
    public void verificaAtualizacaoBusca() throws JsonProcessingException{
        chamadaBUscaPOrId(true);
    }

    private static<T> T executeHttp(String uri, String method, Class<T> clazz) throws JsonProcessingException {
        return executeHttp(uri,method,null,clazz);
    }

    private static<T> T executeHttp(String uri, String method,Object request, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper jack = Jackson2ObjectMapperBuilder.json().build();
        String authStr = "user:12345";
        HttpMethod menhiod =HttpMethod.valueOf(method);
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        //HttpComponentsClientHttpRequestFactoryBasicAuth comp = new HttpComponentsClientHttpRequestFactoryBasicAuth(host)
        //ClientHttpRequestFactory factory= new ClientHttpRequestFactory();
        //factory.setConnectTimeout(Duration.ofSeconds(30));

        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        final RequestEntity entity;
        if(request != null){
            entity =   RequestEntity.method(menhiod, URI.create(uri)).headers(headers).body(request);
        } else {
            entity = new org.springframework.http.RequestEntity<Object>(headers,menhiod, URI.create(uri));
        }
        ResponseEntity  resp = rest.exchange("http://localhost:8080/v1/"+uri,menhiod,entity,String.class);
        String json = (String) resp.getBody();

        ObjectReader writer = jack.reader();
        return (T) writer.forType(clazz).readValue(json);
    }


}
