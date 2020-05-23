package com.vgg.fvp.common.smtp;

import java.util.Map;
import java.lang.Object;

public class MailContent {

    private String sender;
    private String[] receiver;
    private String subject;
    private Map<String, Object> model;

    public MailContent() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String[] getReceiver() {
        return receiver;
    }

    public void setReceiver(String[] receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
