package br.com.rbsoftware.cadBeneficiario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"br.com.rbsoftware.cadBeneficiario.infra.doc","br.com.rbsoftware.cadBeneficiario.infra.security","br.com.rbsoftware.cadBeneficiario.controller","br.com.rbsoftware.cadBeneficiario.infra.data","br.com.rbsoftware.cadBeneficiario.service"})
public class CadBeneficiarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadBeneficiarioApplication.class, args);
	}

}
