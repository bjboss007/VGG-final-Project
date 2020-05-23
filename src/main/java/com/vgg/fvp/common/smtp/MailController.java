package com.vgg.fvp.common.smtp;


import com.vgg.fvp.common.exceptions.BadRequestException;
import com.vgg.fvp.common.utils.AppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fvp/v1/")
public class MailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("sendmail")
    public ResponseEntity sendMail(@Valid @RequestBody MailContentDTO mailContentDTO){
        try {
            emailService.sendCustomisedMail(mailContentDTO);
        } catch (Exception exception) {
            throw  new BadRequestException("Message cannot be sent");
        }
        return ResponseEntity.ok(new AppResponse("Message successfully sent", "success"));
    }
}
