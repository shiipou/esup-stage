/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.utils.mail;

import com.sun.mail.smtp.SMTPMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class SmtpService {
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Value("${spring.mail.fromEmail}")
    private String fromEmail;
    
    @Value("${spring.mail.fromName}")
    private String fromName;
    
    @Value("${spring.mail.interceptEmail}")
    private String interceptEmail;
    
    @Value("${spring.mail.charset}")
    private String encoding;
 
    private static final Logger logger = LoggerFactory.getLogger(SmtpService.class);
    
    @Async
    public void send(String to, String subject, String htmlBody) {
        send(to, subject, htmlBody, null);
    }
    
    @Async
    public void send(String to, String subject, String htmlBody, String textBody) {
        send(to, subject, htmlBody, textBody, new ArrayList<File>());
    }
    
    @Async
    public void send(String to, String subject, String htmlBody, String textBody, String returnPath) {
        send(to, subject, htmlBody, textBody, returnPath, null);
    }
    
    @Async
    public void send(String to, String subject, String htmlBody, String textBody, List<File> files) {
        String[] tos = tos = new String[] {to};
        
        if(to != null && to.contains(",")) {
            tos = to.split(",");
        }
        send(tos, null, subject, htmlBody, textBody, files);
    }
    
    @Async
    public void send(String to[], String[] cc, String subject, String htmlBody, String textBody, List<File> files) {
        send(to, cc, subject, htmlBody, textBody, null, files);
    }
    
    @Async
    public void send(String to, String subject, String htmlBody, String textBody, String returnPath, List<File> files) {
        String[] tos = new String[] {to};
        
        if(to != null && to.contains(",")) {
            tos = to.split(",");
        }
        send(tos, null, subject, htmlBody, textBody, returnPath, files);
    }      
    
    @Async
    public void sendException(String to, String subject, String htmlBody) {
        String[] tos = new String[] {to};
        
        if(to != null && to.contains(",")) {
            tos = to.split(",");
        }
        
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true, encoding);            
            helper.setFrom(fromEmail);
            helper.setTo(tos);               
            helper.setSubject(subject);
            
            if(htmlBody != null) {
                helper.setText(htmlBody, true);
            }
            
            logger.debug("Envoi du mail d'exception");            
                        
            javaMailSender.send(mail);
        } catch (MessagingException e) {
            logger.error("Problème lors de l'envoi du mail d'exception: " + e.getMessage(), e);
        }        
    }
    
    @Async
    public void send(String to[], String[] cc, String subject, String htmlBody, String textBody, String returnPath, List<File> files) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true, encoding);            
            helper.setFrom(fromEmail);
            
            if(interceptEmail != null && !interceptEmail.isEmpty()) {
                String interceptions = "\"interception ";
                interceptions += String.join(" ", to);
                interceptions += "\"";
                helper.setTo(interceptions + " <"+interceptEmail+">");
            } else {
                helper.setTo(to);
            }     
            
            if(cc != null) {
                if(interceptEmail != null && !interceptEmail.isEmpty()) {
                    String interceptions = "\"interception ";
                interceptions += String.join(" ", cc);
                interceptions += "\"";
                    helper.setCc(interceptions + " <"+interceptEmail+">");
                } else {
                    helper.setCc(cc);
                }
            }
            
            helper.setSubject(subject);
            
            if(textBody != null && htmlBody != null) {
                helper.setText(textBody, htmlBody);
            } else {
                if(textBody != null) {
                    helper.setText(textBody, false);;
                }
                
                if(htmlBody != null) {
                    helper.setText(htmlBody, true);
                }
            }
            
            if(files != null) {
                for(File file : files) {
                    helper.addAttachment(file.getName(), file);
                }                
            }
            
            logger.debug("Envoi du mail");
            
            if(returnPath == null || returnPath.trim().isEmpty()) {
                javaMailSender.send(mail);
                return;
            } 
                        
            // récuperation d'un second mimemessage, pas terrible mais pas d'autres moyens 
            // avec Spring boot de changer le return-path
            SMTPMessage smtpMessage = new SMTPMessage(mail);
            smtpMessage.setEnvelopeFrom(returnPath);
            javaMailSender.send(smtpMessage);
        } catch (MessagingException e) {
            logger.error("Problème d'envoi du mail: " + e.getMessage(), e);
        }        
    }
    
    public void sendSynchrone(String to, String subject, String htmlBody) {
        sendSynchrone(to, subject, htmlBody, null);
    }
    
    public void sendSynchrone(String to, String subject, String htmlBody, String textBody) {
        sendSynchrone(to, subject, htmlBody, textBody, new ArrayList<File>());
    }
    
    public void sendSynchrone(String to, String subject, String htmlBody, String textBody, String returnPath) {
        sendSynchrone(to, subject, htmlBody, textBody, returnPath, null);
    }
    
    public void sendSynchrone(String to, String subject, String htmlBody, String textBody, List<File> files) {
        String[] tos = tos = new String[] {to};
        
        if(to != null && to.contains(",")) {
            tos = to.split(",");
        }
        sendSynchrone(tos, null, subject, htmlBody, textBody, files);
    }
    
    public void sendSynchrone(String to, String subject, String htmlBody, String textBody, String returnPath, List<File> files) {
        String[] tos = new String[] {to};
        
        if(to != null && to.contains(",")) {
            tos = to.split(",");
        }
        sendSynchrone(tos, null, subject, htmlBody, textBody, returnPath, files);
    }
        
    public void sendSynchrone(String to[], String[] cc, String subject, String htmlBody, String textBody, List<File> files) {
        sendSynchrone(to, cc, subject, htmlBody, textBody, null, files);
    }
    
    public void sendSynchrone(String to[], String[] cc, String subject, String htmlBody, String textBody, String returnPath, List<File> files) {
        send(to, cc, subject, htmlBody, textBody, returnPath, files);
    }
}
