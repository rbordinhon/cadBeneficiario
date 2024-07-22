package br.com.rbsoftware.cadBeneficiario.infra.controller;

import br.com.rbsoftware.cadBeneficiario.service.exception.BeneficiarioException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@ControllerAdvice
public class ErroController   {
    private static  final Logger logger = LoggerFactory.getLogger(ErroController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorVo> handleError(HttpServletRequest req, Exception ex) {
        logger.error("Request: " + req.getRequestURL() + " raised " + ex);
        return createVo(req,ex);
    }

    /*
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(status, errors);

        return new ResponseEntity<>(apiErrorMessage, apiErrorMessage.getStatus());
    }
     */

    private ResponseEntity<ErrorVo> createVo(HttpServletRequest req,Exception ex){
        ByteArrayOutputStream baous=  new ByteArrayOutputStream();
        ErrorVo mav = new ErrorVo();
        if(! (ex instanceof BeneficiarioException)){
            PrintWriter writter = new PrintWriter(baous);
            ex.printStackTrace(writter);
            mav.setStackTrace(new String(baous.toByteArray()));
        } else {
            mav.setStackTrace("");
        }
        mav.message = ex.getMessage();
        mav.url =  req.getRequestURL().toString();
        return new ResponseEntity<ErrorVo>(mav,HttpStatusCode.valueOf(500));

    }

    @ExceptionHandler(BeneficiarioException.class)
    public ResponseEntity<ErrorVo> handleError(HttpServletRequest req, BeneficiarioException ex) {
        logger.error("Request: " + req.getRequestURL() + " raised " + ex);

        return createVo(req,ex);
    }

}
