package br.com.rbsoftware.cadBeneficiario.domain.repository;

import br.com.rbsoftware.cadBeneficiario.domain.Beneficiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BeneficiarioRepository extends JpaRepository<Beneficiario,Integer> {
}
