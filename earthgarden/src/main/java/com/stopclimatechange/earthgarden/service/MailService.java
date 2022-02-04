package com.stopclimatechange.earthgarden.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public boolean sendCheckEmail(String email, String code) {

        if (code == null)
            return false;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("EarthGardener 이메일 확인 코드");
        simpleMailMessage.setText("저희 서비스에 가입해 주셔서 감사합니다.\n"
                + "이메일 확인 코드는 " + code + " 입니다.");

        javaMailSender.send(simpleMailMessage);
        return true;
    }
}