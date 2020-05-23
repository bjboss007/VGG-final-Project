package com.vgg.fvp.common.smtp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MailContentDTO {

    @NotNull(message = "A sender is required")
    private String sender;
    @NotNull(message = "A receiver is required")
    private String receiver;
    @NotNull(message = "Subject cannot be blank")
    private String subject;
    @NotBlank
    @NotNull(message = "Content cannot be blank")
    private String content;

    public MailContentDTO() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
