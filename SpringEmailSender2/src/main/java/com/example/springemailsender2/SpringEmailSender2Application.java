package com.example.springemailsender2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SpringEmailSender2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringEmailSender2Application.class, args);
    }

    // ==================================================================================================

    private final EmailSenderService emailSenderService;

    public SpringEmailSender2Application(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @EventListener(value = ApplicationReadyEvent.class)
    public void sendMail() {
        emailSenderService.sendEmail("revi1337@naver.com", "This is Subject", "<h1>BODY</h1>");
    }

}
