package com.example.springemailsender.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service @RequiredArgsConstructor @Slf4j
public class EmailService implements EmailSender{

    private final JavaMailSender javaMailSender;

    @Override @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    mimeMessage, String.valueOf(StandardCharsets.UTF_8)
            );
            mimeMessageHelper.setText(email, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject("Confirm your email");
            mimeMessageHelper.setFrom("privilgescalate@gmail.com");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email : {}", e);
            throw new IllegalStateException("failed to send email");
        }

    }
}
