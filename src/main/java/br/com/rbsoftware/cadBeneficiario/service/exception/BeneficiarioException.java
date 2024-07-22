package br.com.rbsoftware.cadBeneficiario.service.exception;

public class BeneficiarioException extends Exception{

    public BeneficiarioException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeneficiarioException(String message) {
        super(message);
    }
}
