package br.com.rbsoftware.cadBeneficiario.infra.doc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDoc {

        @Bean
        public OpenAPI getDOc(){
            return new OpenAPI().info(new Info().title("Cadastro de Beneficiario Demo")
                    .version("v1")
                    .description("Aplicação Restfull para cadastro de beneficiario.\r\nPara acessar as operação utilisar autenticação básica com o usuario "+ "\""+"user"+"\"" +" e senha "+" 12345"));
        }
}
