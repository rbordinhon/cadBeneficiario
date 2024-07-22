package br.com.rbsoftware.cadBeneficiario.infra.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("br.com.rbsoftware.cadBeneficiario.domain")
@ComponentScan("br.com.rbsoftware.cadBeneficiario.domain.repository")
public class DomainConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
