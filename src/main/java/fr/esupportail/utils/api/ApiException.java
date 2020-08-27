/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.utils.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiException extends Exception {
    private HttpStatus status; 
    private List<ApiFieldError> errors;  
    private List<String> messages;
    
    public ApiException(HttpStatus status, String message) {
        this(status, new ArrayList<String>(Arrays.asList(message)));       
    }
    
    public ApiException(HttpStatus status, String message, List<ApiFieldError> errors)  {
        this(status, new ArrayList<String>(Arrays.asList(message)), errors); 
    }
    
    public ApiException(HttpStatus status, List<String> messages) {
        this(status, messages, null);       
    }
    
    public ApiException(HttpStatus status, List<String> messages, List<ApiFieldError> errors)  {
        this.messages = messages;
        this.status = status;
        this.errors = errors;
    }
    
    @Override
    public String getMessage() {
        if(messages == null) {
            return null;
        }
        StringBuilder messageBuilder = new StringBuilder();
        for(int index=0; index<messages.size(); index++) {            
            messageBuilder.append(messages.get(index));
            if(index != messages.size() -1) {
                messageBuilder.append(" | ");
            }
        }
        return messageBuilder.toString();
    }
    
    public void setMessage(String message) {
        messages = new ArrayList();
        messages.add(message);
    }
}
