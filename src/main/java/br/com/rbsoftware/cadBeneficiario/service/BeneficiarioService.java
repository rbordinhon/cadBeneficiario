package br.com.rbsoftware.cadBeneficiario.service;

import br.com.rbsoftware.cadBeneficiario.domain.Beneficiario;
import br.com.rbsoftware.cadBeneficiario.domain.Documento;
import br.com.rbsoftware.cadBeneficiario.domain.repository.BeneficiarioRepository;
import br.com.rbsoftware.cadBeneficiario.domain.repository.DocumentoRepository;
import br.com.rbsoftware.cadBeneficiario.infra.dto.BuscaBeneficiarioDTO;
import br.com.rbsoftware.cadBeneficiario.infra.dto.BuscaDocumentoDTO;
import br.com.rbsoftware.cadBeneficiario.infra.dto.CriaBeneficiarioDTO;
import br.com.rbsoftware.cadBeneficiario.infra.dto.DocumentoDTO;
import br.com.rbsoftware.cadBeneficiario.service.exception.BeneficiarioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class BeneficiarioService {
    @Autowired
    BeneficiarioRepository repositorioBeneficiario;

    @Autowired
    DocumentoRepository repositorioDocumento;



    public List<BuscaBeneficiarioDTO> buscaBeneficiarios(){
        List<Beneficiario> beneficiarios = repositorioBeneficiario.findAll();
        return beneficiarios.stream().map(BeneficiarioService::mappingDto).toList();
    }



    public BuscaBeneficiarioDTO buscaBeneficiarioPorId(Integer id) throws BeneficiarioException {
        Optional<Beneficiario> busca =  repositorioBeneficiario.findById(id);
        if(!busca.isPresent()){
            throw new BeneficiarioException("Beneficiario não encontrado");
        }
        return mappingDto(busca.get());
    }

    public List<BuscaDocumentoDTO> buscaDocumentosByIdBeneficiario(Integer id) throws BeneficiarioException {
        return repositorioDocumento.buscaDocumentos(id).stream().map(BeneficiarioService::mappingDocumento).toList();
    }

    public BuscaBeneficiarioDTO criarBeneficiario(CriaBeneficiarioDTO dto){
        Beneficiario bene  = mappingBeneficiario(dto);
        List<Documento> docs = bene.getItems();
        bene.setItems(null);
        repositorioBeneficiario.saveAndFlush(bene);
        for (Documento doc:docs){
            doc.setBeneficiario(bene);
            repositorioDocumento.saveAndFlush(doc);
        }
        return mappingDto(bene);
    }

    public BuscaBeneficiarioDTO atualizaBeneficiario(int idBeneficiario,CriaBeneficiarioDTO dto) throws BeneficiarioException{
        Optional<Beneficiario> beneData  =  repositorioBeneficiario.findById(idBeneficiario);
        if(!beneData.isPresent()){
            throw new BeneficiarioException("Beneficiario não encontrado");
        }

        Beneficiario bene  = mappingBeneficiarioAtualizxacao(dto,beneData.get());
        List<Documento> docs = bene.getItems();
        bene.setItems(null);
        repositorioBeneficiario.saveAndFlush(bene);
        List<Documento> removeList  =  repositorioDocumento.buscaDocumentos(idBeneficiario);
        for (Documento doc:docs){
            Optional<Documento>  docexiste =  removeList.stream().filter((docExist)->{
                return docExist.getDescricao().equals(doc.getDescricao()) && docExist.getTipoDocumento().equals(doc.getTipoDocumento());

            }).findFirst();

            if(!docexiste.isPresent()){
                repositorioDocumento.saveAndFlush(doc);
            } else {
                removeList.remove(docexiste.get());
                docexiste.get().setDataAtualizacao(LocalDateTime.now());
                docexiste.get().setDadosDocumento(doc.getDadosDocumento());
                repositorioDocumento.saveAndFlush(docexiste.get());
            }
            for (Documento remove:removeList){
                repositorioDocumento.delete(remove);
            }

        }
        return mappingDto(bene);
    }

    public void removerBeneficiario(int id){
        repositorioBeneficiario.deleteById(id);
    }

    private static BuscaDocumentoDTO mappingDocumento(Documento documento){
        BuscaDocumentoDTO dto = new BuscaDocumentoDTO();
        dto.tipoDocumento= documento.getTipoDocumento();
        dto.dadosDocumento = Base64.getEncoder().encodeToString(documento.getDadosDocumento());
        dto.dataInclusao= documento.getDataInclusao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        dto.dataAtualizacao= documento.getDataAtualizacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        dto.idDocumento= documento.getId();
        dto.descricao= documento.getDescricao();
        return dto;
    }

    private static BuscaBeneficiarioDTO mappingDto(Beneficiario bene){
        BuscaBeneficiarioDTO dto = new BuscaBeneficiarioDTO();
        dto.dataNascimento = bene.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        dto.dataAtualizacao= bene.getDataAtualizacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        dto.dataInclusao= bene.getDataInclusao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        dto.id=bene.getId();
        dto.nome=bene.getNome();
        dto.telefone = bene.getTelefone();
        return dto;
    }

    private static Beneficiario mappingBeneficiarioAtualizxacao(CriaBeneficiarioDTO dto,Beneficiario benAtual){
        Beneficiario bene = new Beneficiario();
        bene.setId(benAtual.getId());
        bene.setNome(dto.nome);
        bene.setDataAtualizacao(null);
        bene.setDataInclusao(benAtual.getDataInclusao());
        bene.setDataAtualizacao(LocalDateTime.now());
        bene.setTelefone(dto.telefone);
        bene.setDataNascimento(LocalDate.parse(dto.dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        List<Documento> docs =  new ArrayList<>();
        for (DocumentoDTO doc:dto.documentos){
            Documento docEnt =  new Documento();
            docEnt.setBeneficiario(bene);
            docEnt.setDescricao(doc.descricao);
            docEnt.setDadosDocumento(Base64.getDecoder().decode(doc.dadosDocumento));
            docEnt.setTipoDocumento(doc.tipoDocumento);
            docEnt.setDataInclusao(LocalDateTime.now());
            docEnt.setDataAtualizacao(docEnt.getDataInclusao());
            docs.add(docEnt);
        }
        bene.setItems(docs);
        return bene;
    }

    private static Beneficiario mappingBeneficiario(CriaBeneficiarioDTO dto){
        Beneficiario bene = new Beneficiario();
        bene.setNome(dto.nome);
        bene.setDataAtualizacao(null);
        bene.setDataInclusao(LocalDateTime.now());
        bene.setDataAtualizacao(bene.getDataInclusao());
        bene.setTelefone(dto.telefone);
        bene.setDataNascimento(LocalDate.parse(dto.dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        List<Documento> docs =  new ArrayList<>();
        for (DocumentoDTO doc:dto.documentos){
            Documento docEnt =  new Documento();
            docEnt.setBeneficiario(bene);
            docEnt.setDescricao(doc.descricao);
            docEnt.setDadosDocumento(Base64.getDecoder().decode(doc.dadosDocumento));
            docEnt.setTipoDocumento(doc.tipoDocumento);
            docEnt.setDataInclusao(LocalDateTime.now());
            docEnt.setDataAtualizacao(docEnt.getDataInclusao());
            docs.add(docEnt);
        }
        bene.setItems(docs);
        return bene;
    }


}
