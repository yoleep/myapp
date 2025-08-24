package com.company.common.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.name:Common Module}")
    private String appName;
    
    @Async
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            log.info("Simple email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send simple email to: {}", to, e);
        }
    }
    
    @Async
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("HTML email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to: {}", to, e);
        }
    }
    
    @Async
    public void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);
            context.setVariable("appName", appName);
            
            String htmlContent = templateEngine.process(templateName, context);
            sendHtmlEmail(to, subject, htmlContent);
            
        } catch (Exception e) {
            log.error("Failed to send template email to: {}", to, e);
        }
    }
    
    @Async
    public void sendWelcomeEmail(String to, String userName) {
        Map<String, Object> variables = Map.of(
                "userName", userName,
                "loginUrl", "http://localhost:3000/login"
        );
        
        String subject = "Welcome to " + appName;
        sendTemplateEmail(to, subject, "welcome-email", variables);
    }
    
    @Async
    public void sendPasswordResetEmail(String to, String resetToken) {
        Map<String, Object> variables = Map.of(
                "resetLink", "http://localhost:3000/reset-password?token=" + resetToken,
                "expirationTime", "24 hours"
        );
        
        String subject = "Password Reset Request - " + appName;
        sendTemplateEmail(to, subject, "password-reset-email", variables);
    }
    
    @Async
    public void sendEmailVerification(String to, String verificationToken) {
        Map<String, Object> variables = Map.of(
                "verificationLink", "http://localhost:3000/verify-email?token=" + verificationToken
        );
        
        String subject = "Email Verification - " + appName;
        sendTemplateEmail(to, subject, "email-verification", variables);
    }
    
    @Async
    public void sendNotificationEmail(String to, String title, String message) {
        Map<String, Object> variables = Map.of(
                "title", title,
                "message", message,
                "dashboardUrl", "http://localhost:3000/dashboard"
        );
        
        sendTemplateEmail(to, title, "notification-email", variables);
    }
    
    @Async
    public void sendBulkEmail(String[] recipients, String subject, String content) {
        for (String recipient : recipients) {
            try {
                sendHtmlEmail(recipient, subject, content);
            } catch (Exception e) {
                log.error("Failed to send bulk email to: {}", recipient, e);
            }
        }
    }
}