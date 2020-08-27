/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.esupstage.controllers.exceptions;

import fr.esupportail.utils.api.ApiError;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Gestion des exceptions globales (prend en compte les exceptions du framework)
 * @author vagrant
 */
@Slf4j
@RestController
@ApiIgnore
public class GlobalErrorController extends AbstractErrorController {

    @Autowired
    private ApiExceptionComponent errorComponent;
    
    public GlobalErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(value = "/error")
    public ApiError handleError(HttpServletRequest request, WebRequest webRequest) {
        Map<String, Object> attributes = super.getErrorAttributes(request, true);
        HttpStatus status = HttpStatus.resolve((Integer)attributes.get("status"));
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");

        ApiError errorObject = errorComponent.buildErrorObject(attributes, webRequest, status, exception);
        
        if(status != null 
                && !status.equals(HttpStatus.NOT_FOUND) 
                && !status.equals(HttpStatus.UNAUTHORIZED) 
                && !status.equals(HttpStatus.FORBIDDEN)) {
            errorComponent.sendMail(errorObject);
        }
        return errorObject;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
