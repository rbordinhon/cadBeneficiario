package br.com.rbsoftware.cadBeneficiario.service;

import br.com.rbsoftware.cadBeneficiario.domain.Beneficiario;
import br.com.rbsoftware.cadBeneficiario.domain.repository.BeneficiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class BeneficiarioLookupService {

    @Autowired
    private BeneficiarioRepository repository;




}
