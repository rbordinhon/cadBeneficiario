package br.com.rbsoftware.cadBeneficiario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TEsteMain {
    public static void main(String[] args) {
        BCryptPasswordEncoder encode= new BCryptPasswordEncoder();
        System.out.printf( encode.encode("12345"));
    }
}
