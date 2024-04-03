package com.StarJ.food_recipe.Email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Async // CompletableFuture<Boolean>
    public void sendMailReject(String targetEmail, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setFrom("ghdtjdwo126@gmail.com");
        simpleMailMessage.setText(body);
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
        }
    }
}
