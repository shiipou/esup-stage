package fr.esupportail.utils.mail;

import fr.esupportail.utils.api.ApiError;
import fr.esupportail.utils.api.ApiFieldError;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Permet d'établir un rapport text/html d'une exception et de son environnement
 */
@Slf4j
@Component
public class MailReportService {
    
    private static final long serialVersionUID = -1899794329062663727L;
    
     /**
     * @param request
     * @param applicationName
     * @param apiError
     * @return le rapport HTML complet de l'erreur
     */
    public MailReport getHtmlReport(HttpServletRequest request, String applicationName, ApiError apiError) {
        
        ReportUtils reportUtils = new ReportUtils(request);
        return getHtmlReport(reportUtils, applicationName, apiError);
    }
    
    /**
     * @param reportUtils
     * @param applicationName
     * @param apiError
     * @return le rapport HTML complet de l'erreur
     */
    public MailReport getHtmlReport(ReportUtils reportUtils, String applicationName, ApiError apiError) {        
        StringBuilder body = new StringBuilder(h1("Une erreur"));
        body.append("\n<table border=\"1\">");
        body.append(getHtmlReportInformation(reportUtils, applicationName, (Date) apiError.getTimestamp()));
        body.append(getHtmlReportException(apiError));
        body.append(getHtmlReportRequestParameters(reportUtils));
        body.append(getHtmlReportMemory(reportUtils));
        body.append("\n</table>");        
        
        String subject = getReportSubject(reportUtils, applicationName, apiError);
        
        return new MailReport(subject, body.toString());
    }

    /**
     * 
     * @param reportUtils
     * @param applicationName
     * @param apiError
     * @return le sujet du mail
     */
    private String getReportSubject(ReportUtils reportUtils, String applicationName, ApiError apiError) {
        String server = reportUtils.getServer();
        
        String name = (String)apiError.getExceptionName();
        if(name != null) {
            name = name.substring(name.lastIndexOf(".")+1);
        } else {
            HttpStatus status = HttpStatus.valueOf(apiError.getStatus());
            List<String> messages = apiError.getMessages();
            if(status != null) {
                name = status.getReasonPhrase();
            }

            if(messages != null && messages.size() > 0 && !messages.get(0).equals(name)) {
                name += " - " + messages.get(0);
            }
        }
        String serverStr = server;
        if (serverStr == null) {
            serverStr = "inconnu";
        }

        return "[" + applicationName + "] " + name + " sur " + serverStr;
    }

    /**
     * 
     * @param title
     * @return l'entête HTML du rapport
     */
    private String header(final String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<tr>");
        sb.append("\n<th colspan=\"2\" align=\"left\">");
        sb.append(title);
        sb.append("\n</th>");
        sb.append("\n</tr>");
        return sb.toString();
    }

    /**
     * @param str1
     * @param str2
     * @return une ligne HTML de 2 cellules
     */
    private String row2(final String str1, final String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<tr>");
        sb.append("\n<td valign=\"top\" width=\"10%\" nowrap=\"true\" >");
        sb.append(str1);
        sb.append("\n</td>");
        sb.append("\n<td width=\"100%\" >");
        sb.append(str2);
        sb.append("\n</td>");
        sb.append("\n</tr>");
        return sb.toString();
    }

    /**
     * @param str
     * @return une ligne HTML de 2 cellules fusionnées
     */
    private String row(final String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<tr>");
        sb.append("\n<td colspan=\"2\" align=\"left\">");
        sb.append(str);
        sb.append("\n</td>");
        sb.append("\n</tr>");
        return sb.toString();
    }

    /**
     * @param str
     * @return une emphase du texte mis en paramètre
     */
    private String em(final String str) {
        return new StringBuilder("<em>").append(str).append("</em>").toString();
    }

    /**
     * wrap a string into a H1 tag.
     *
     * @param str
     * @return  le titre du rapport
     */
    private String h1(final String str) {
        return new StringBuilder("<h1>").append(str).append("</h1>").toString();
    }

    /**
     * 
     * @param reportUtils
     * @param applicationName
     * @param timestamp
     * @return Les informations générales du rapport
     */
    private StringBuilder getHtmlReportInformation(ReportUtils reportUtils, String applicationName, Date timestamp) {
        
        String server = reportUtils.getServer();
        String client = reportUtils.getClient();
        String userAgent = reportUtils.getUserAgent();
        String queryString = reportUtils.getQueryString();   
        String userId = reportUtils.getRemoteUser();
        
        StringBuilder sb = new StringBuilder();
        sb.append(header("Informations générales"));
        sb.append(row2("Application", StringEscapeUtils.escapeHtml4(applicationName)));
        String serverStr;
        if (server == null) {
            serverStr = em("inconnu");
        } else {
            serverStr = StringEscapeUtils.escapeHtml4(server);
        }
        sb.append(row2("Serveur", serverStr));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String dateStr = sdf.format(timestamp);
        sb.append(row2("Date", dateStr));

        String userIdStr;
        if (userId == null) {
            userIdStr = em("inconnu");
        } else {
            userIdStr = StringEscapeUtils.escapeHtml4(userId);
        }
        sb.append(row2("Utilisateur", userIdStr));

        String clientStr;
        if (client == null) {
            clientStr = em("inconnu");
        } else {
            clientStr = StringEscapeUtils.escapeHtml4(client);
        }
        sb.append(row2("Client", clientStr));
        String queryStringStr;
        if (queryString == null) {
            queryStringStr
                    = em("inconnu");
        } else {
            queryStringStr = StringEscapeUtils.escapeHtml4(queryString);
        }
        sb.append(row2("Query string", queryStringStr));
        String userAgentStr;
        if (userAgent == null) {
            userAgentStr = em("inconnu");
        } else {
            userAgentStr = StringEscapeUtils.escapeHtml4(userAgent);
        }
        sb.append(row2("Navigateur client", userAgentStr));

        return sb;
    }

    /**
     * @param strings
     * @return a printable String for text outputs.
     */
    private String html(final Collection<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        String separator = "";
        for (String string : strings) {
            result.append(separator).append(StringEscapeUtils.escapeHtml4(string));
            separator = "<br>";
        }
        return result.toString();
    }

    /**
     * 
     * @param apiError
     * @return Les informations de l'exception du rapport
     */
    private StringBuilder getHtmlReportException(ApiError apiError) {
        StringBuilder sb = new StringBuilder();
        sb.append(header("Exception levée"));
        
        HttpStatus status = HttpStatus.valueOf(apiError.getStatus());
        List<String> messages = apiError.getMessages();
        String debugMessage = apiError.getDebugMessage();
        String stackTrace = apiError.getStackTrace();
        String shortStackTrace = apiError.getExceptionName();
        String path = apiError.getPath();
        List<ApiFieldError> errors = apiError.getFieldErrors();

        if(status != null) {
            sb.append(row2("Status", status.value() + " - " + status.getReasonPhrase()));
        }
        
        if(messages != null) {
            StringBuilder message = new StringBuilder();
            for(String messageStr : messages) {
                message.append(messageStr);
                message.append("\n");
            }
            sb.append(row2("Erreur retournée", message.toString()));
        }
        
        if(debugMessage != null) {
            sb.append(row2("Message système", debugMessage));
        }
        
        if(errors != null) {
            StringBuilder errorsString = new StringBuilder();
            for(ApiFieldError error : errors) {
                errorsString.append(error.getField());
                errorsString.append(": ");
                errorsString.append(error.getMessage());
                errorsString.append("\n");
            }
            sb.append(row2("Erreurs de validation", errorsString.toString()));
        }
        
        if(path != null) {
            sb.append(row2("Url", path));
        }
        
        sb.append(row2("Pile", shortStackTrace));
        sb.append(row2("Pile détaillée", stackTrace));
        return sb;
    }

    /**
     * 
     * @param reportUtils
     * @return Les paramètres de requêtes au format HTML du rapport
     */
    private StringBuilder getHtmlReportRequestParameters(ReportUtils reportUtils) {
        Set<String> cookies = reportUtils.getCookies();
        Set<String> requestHeaders = reportUtils.getHeaders();
        Set<String> requestParameters = reportUtils.getParameters();
        Set<String> sessionAttributes = reportUtils.getSessionsAttributes();        

        String sessionAttributesStr = html(sessionAttributes);
        String requestHeadersStr = html(requestHeaders);
        String requestParametersStr = html(requestParameters);
        String cookiesStr = html(cookies);
        StringBuilder sb = new StringBuilder();
        sb.append(header("Attributs de session"));
        if (sessionAttributesStr == null) {
            sessionAttributesStr
                    = em("aucun");
        }
        sb.append(row(sessionAttributesStr));
        sb.append(header("Entêtes HTTP"));
        if (requestHeadersStr == null) {
            requestHeadersStr = em("aucune");
        }
        sb.append(row(requestHeadersStr));
        sb.append(header("Paramètres de la requête"));
        if (requestParametersStr == null) {
            requestParametersStr
                    = em("aucun");
        }
        sb.append(row(requestParametersStr));
        sb.append(header("Cookies"));
        if (cookiesStr == null) {
            cookiesStr = em("aucun");
        }
        sb.append(row(cookiesStr));
        return sb;
    }

    /**
     * 
     * @param reportUtils
     * @return les informations de mémoire lors de la levée d'exception
     */
    private StringBuilder getHtmlReportMemory(ReportUtils reportUtils) {
        StringBuilder sb = new StringBuilder();
        sb.append(header("Mémoire"));
        sb.append(row2("Libre", reportUtils.getFreeMemory() + "Mo"));
        sb.append(row2("Total", reportUtils.getTotalMemory() + "Mo"));
        sb.append(row2("Max", reportUtils.getMaxMemory() + "Mo"));
        return sb;
    }
}

