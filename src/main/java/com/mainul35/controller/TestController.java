package com.mainul35.controller;

import com.mainul35.config.Mailer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/mail/send")
    public ResponseEntity<?> sendMail() {
        Mailer.sendMail("mainuls18@gmail.com", "Test Mail", "This is a test mail");
        return ResponseEntity.ok("Mail sent successfully");
    }

    void test() {
        Set<? extends Number> numList = Set.of(1, 2, 3);

    }
}
