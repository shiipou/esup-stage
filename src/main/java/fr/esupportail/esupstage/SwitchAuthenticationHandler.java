
package fr.esupportail.esupstage;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class SwitchAuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler{

    private final String frontendUrl; 

    public SwitchAuthenticationHandler(String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }
    
    /**
     * Après authentification réussit avec le switch, forward vers le path /api/account pour renvoyer l'objet user switché
     * @param request
     * @param response
     * @param auth
     * @throws IOException
     * @throws ServletException 
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {        
        request.getRequestDispatcher("/api/account").forward(request, response);
    }

    /**
     * En cas d'erreur du switch, renvoit du code http correspondant
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException 
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.addHeader("Access-Control-Allow-Origin", frontendUrl);
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Content-Type", "application/json");       
    }
}
