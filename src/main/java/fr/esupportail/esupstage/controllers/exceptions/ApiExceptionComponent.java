
package fr.esupportail.esupstage.controllers.exceptions;

import fr.esupportail.utils.api.ApiException;
import fr.esupportail.utils.api.ApiError;
import fr.esupportail.utils.mail.MailReport;
import fr.esupportail.utils.mail.MailReportService;
import fr.esupportail.utils.mail.SmtpService;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author vagrant
 */
@Slf4j
@Component
public class ApiExceptionComponent {
    
    @Autowired
    private MailReportService reportService;

    @Autowired
    private SmtpService smtpService;
    
    @Value("${application.title}")
    private String applicationTitle;

    @Value("${debug}")
    private Boolean debug;

    @Value("${spring.mail.exceptionEmail}")
    private String to;
    
    public ApiError buildErrorObject(Map<String, Object> attributes, WebRequest request, HttpStatus status) {
        return buildErrorObject(attributes, request, status, null);
    }
    
    public ApiError buildErrorObject(Map<String, Object> attributes, WebRequest request, HttpStatus status, Exception ex) {
        return buildErrorObject(attributes, request, status, ex, null);
    }
    
    public ApiError buildErrorObject(Map<String, Object> attributes, WebRequest request, HttpStatus status, Exception ex, String errorMessage) {
        
        ApiError errorObject = new ApiError();
        
        String path = ((ServletWebRequest)request).getRequest().getRequestURI();
        errorObject.setTimestamp((Date)attributes.get("timestamp"));        
        errorObject.setPath(path);
        errorObject.setStatus(status.value());   
        
        if(errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = status.getReasonPhrase();
        }
        errorObject.addMessage(errorMessage);                        
        // Ajout de l'exception
        if(ex != null) {
            StringWriter stackTrace = new StringWriter();
            ex.printStackTrace(new PrintWriter(stackTrace));
            stackTrace.flush();        

            errorObject.setStackTrace(stackTrace.toString());
            errorObject.setExceptionName(ex.getClass().getName());
            errorObject.setDebugMessage(ex.getLocalizedMessage());
        }

        return errorObject;
    }
    
    public ApiError buildErrorObject(Map<String, Object> attributes, WebRequest request, ApiException ex) {        
        ApiError errorObject = buildErrorObject(attributes, request, ex.getStatus(), ex, ex.getMessage());
        errorObject.setFieldErrors(ex.getErrors());
        return errorObject;
    }
    
    public void sendMail(ApiError error) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getRequest();

        MailReport report = reportService.getHtmlReport(request, applicationTitle, error);
        
        StringBuilder errorMessage = new StringBuilder();
        if(error.getStatus() != null) {
            errorMessage.append(error.getStatus());            
        }
        
        if(error.getMessages() != null) {
            for(String message : error.getMessages()) {
                errorMessage.append(" | ");
                errorMessage.append(message);                
            }
        }
        
        if(error.getDebugMessage() != null) {
            errorMessage.append(" : ");
            errorMessage.append(error.getDebugMessage());
        }
        
        if(error.getStackTrace() != null) {
            errorMessage.append(" ==> Stacktrace: \n");
            errorMessage.append(error.getStackTrace());
        }
        
        log.error(errorMessage.toString());
        
        smtpService.sendException(to, report.getSubject(), report.getBody());
    }
}