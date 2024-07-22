package br.com.rbsoftware.cadBeneficiario.domain.repository;

import br.com.rbsoftware.cadBeneficiario.domain.Beneficiario;
import br.com.rbsoftware.cadBeneficiario.domain.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento,Integer> {

    @Query(value = "select doc.* from Documento doc where doc.id_beneficiario=:idBeneficiario",nativeQuery = true)
    public List<Documento> buscaDocumentos(@Param("idBeneficiario") Integer idBeneficiario);


}
