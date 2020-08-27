
/**
 * Gestion des exceptions de l'API du projet
 * Ces exceptions ne sont prises en charge que s'il y a eu passage dans un controller
 * si exception dans un cronjob, pendant la phase d'auth, ..., l'exception n'est pas catché
 * @author David
 */
package fr.esupportail.esupstage.controllers.exceptions;

import fr.esupportail.utils.api.ApiException;
import fr.esupportail.utils.api.ApiError;
import fr.esupportail.esupstage.services.exceptions.EntityNotFoundException;
import fr.esupportail.utils.api.ApiFieldError;
import fr.esupportail.utils.i18n.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Gestion des exceptions de l'API du projet
 * Ces exceptions ne sont prises en charge que s'il y a eu passage dans un controller
 * si exception dans un cronjob, pendant la phase d'auth, ..., l'exception n'est pas catché
 * @author David
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @Autowired
    private ApiExceptionComponent errorComponent;
    
    @Autowired
    private ErrorAttributes errorAttributes;
    
    @Autowired
    private MessageService messageService;

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param request WebRequest
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ApiError handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,  
            WebRequest request) {
        return buildErrorObject(request, HttpStatus.BAD_REQUEST, ex, null);
    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param request WebRequest
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ApiError handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            WebRequest request) {
        ApiError error = buildErrorObject(request, HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex, messageService.getMessage("exception.internal"));
        errorComponent.sendMail(error);        
        return error;
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param request WebRequest
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiError handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        ApiError apiError = buildErrorObject(request, HttpStatus.BAD_REQUEST, ex, messageService.getMessage("exception.validation"));
        
        List<ApiFieldError> errors = new ArrayList();        
        
        List<String> messages = apiError.getMessages();
        for(ObjectError bindingError : ex.getBindingResult().getGlobalErrors()) {
            StringBuilder message = new StringBuilder();    
            messages.add(messageService.getMessage(bindingError, bindingError.getArguments()));            
        }
        
        for(FieldError bindingError : ex.getBindingResult().getFieldErrors()) {
            ApiFieldError fieldError = new ApiFieldError(bindingError.getField(), messageService.getMessage(bindingError, bindingError.getArguments()));          
            errors.add(fieldError);
        }
        
        apiError.setFieldErrors(errors);
        
        errorComponent.sendMail(apiError);
        return apiError;
    }

    /**
     * Handles javax.validation.ConstraintViolationException.Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @param request
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ApiError handleConstraintViolation(
            javax.validation.ConstraintViolationException ex, 
            WebRequest request) {
        ApiError error = buildErrorObject(request, HttpStatus.BAD_REQUEST, ex, messageService.getMessage("exception.validation"));
        List<ApiFieldError> violations = new ArrayList();
        for (ConstraintViolation violation : ex.getConstraintViolations()) {
            violations.add(new ApiFieldError(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        error.setFieldErrors(violations);        
        errorComponent.sendMail(error);
        return error;
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param request WebRequest
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ApiError handleHttpMessageNotReadable(HttpMessageNotReadableException ex, 
            WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        ApiError error = buildErrorObject(request, HttpStatus.BAD_REQUEST, ex, messageService.getMessage("exception.internal"));
        errorComponent.sendMail(error);
        return error;
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param request WebRequest
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpMessageNotWritableException.class)
    protected ApiError handleHttpMessageNotWritable(HttpMessageNotWritableException ex, 
            WebRequest request) {
        ApiError error = buildErrorObject(request, HttpStatus.INTERNAL_SERVER_ERROR, ex, messageService.getMessage("exception.internal"));
        errorComponent.sendMail(error);
        return error;
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex
     * @param request
     * @return
     */
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ApiError handleNoHandlerFoundException(
            NoHandlerFoundException ex, WebRequest request) {
        ApiError error = buildErrorObject(request, HttpStatus.BAD_REQUEST, ex, messageService.getMessage("exception.internal"));
        errorComponent.sendMail(error);
        return error;
    }
    
    /**
     * Handles EntityNotFoundException.Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @param request
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    protected ApiError handleEntityNotFound(
            EntityNotFoundException ex, WebRequest request) {
        ApiError error = buildErrorObject(request, HttpStatus.NOT_FOUND, ex, messageService.getMessage("exception.notfound"));
        errorComponent.sendMail(error);
        return error;
    }
    
    /**
     * Handle javax.persistence.EntityNotFoundException
     * @param ex
     * @param request
     * @return 
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ApiError handleEntityNotFound(javax.persistence.EntityNotFoundException ex, 
            WebRequest request) {
        ApiError error = buildErrorObject(request, HttpStatus.NOT_FOUND, ex, messageService.getMessage("exception.notfound"));
        errorComponent.sendMail(error);
        return error;
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @param request
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ApiError handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        ApiError error = buildErrorObject(request, HttpStatus.INTERNAL_SERVER_ERROR, ex, messageService.getMessage("exception.internal"));
        if (ex.getCause() instanceof ConstraintViolationException) {
           error = buildErrorObject(request, HttpStatus.CONFLICT, ex, messageService.getMessage("exception.internal"));
        }
        errorComponent.sendMail(error);
        return error;
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @param request
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ApiError handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        ApiError error = buildErrorObject(request, HttpStatus.BAD_REQUEST, ex, messageService.getMessage("exception.internal"));
        errorComponent.sendMail(error);
        return error;
    }
    
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler({ AccessDeniedException.class })
    public ApiError handleAccessDeniedException(Exception ex, WebRequest request) {
        return buildErrorObject(request, HttpStatus.FORBIDDEN, ex, messageService.getMessage("exception.denied"));
    }
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingPathVariableException.class)
    public ApiError handleMissingPathVariableException(WebRequest request, MissingPathVariableException ex) {
        return buildErrorObject(request, HttpStatus.BAD_REQUEST, ex, messageService.getMessage("exception.internal"));
    }    
    
    /**
     * Handle Exception. .
     *
     * @param ex      Exception
     * @param request WebRequest
     * @return the ApiError object
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ Exception.class })    
    protected ApiError handleException(
            Exception ex,
            WebRequest request) {
        ApiError error = buildErrorObject(request, HttpStatus.INTERNAL_SERVER_ERROR, ex, messageService.getMessage("exception.internal"));
        errorComponent.sendMail(error);
        return error;
    }
    
    /**
     * Handle API Exception. .
     *
     * @param ex      Exception
     * @param request WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler({ ApiException.class })    
    public ResponseEntity<ApiError> handleApiException(
            ApiException ex,
            WebRequest request) {
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(request, true);     
        ApiError error = errorComponent.buildErrorObject(attributes, request, ex);
        return new ResponseEntity(error, ex.getStatus());
    }
    
    private ApiError buildErrorObject(WebRequest request, HttpStatus status, Exception ex, String message) {
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(request, true);        
        return errorComponent.buildErrorObject(attributes, request, status, ex, message);
    }
}