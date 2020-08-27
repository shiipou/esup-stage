/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.utils.i18n;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author david
 */
@Component
public class MessageService {
    
    @Autowired
    private MessageSource messageSource;

    public String getMessage(String id) {
        return getMessage(id,null);
    }
    
    public String getMessage(String id, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(id,args,locale);
    }
    
    public String getMessage(MessageSourceResolvable msr, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msr,locale);
    }
    
    public String getMessage(String id, Object[] args, String defaultMessage) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(id, args, defaultMessage, locale);
    }
}