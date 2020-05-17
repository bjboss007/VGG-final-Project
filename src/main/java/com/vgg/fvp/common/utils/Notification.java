package com.vgg.fvp.common.utils;

import com.vgg.fvp.common.data.AbstractEntity;
import com.vgg.fvp.common.data.User;

import javax.persistence.Entity;

@Entity
public class Notification extends AbstractEntity {

    private String message;
    private User subjectUser;
    private String messageStatus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSubjectUser() {
        return subjectUser;
    }

    public void setSubjectUser(User subjectUser) {
        this.subjectUser = subjectUser;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "message='" + message + '\'' +
                ", subjectUser=" + subjectUser +
                ", messageStatus='" + messageStatus + '\'' +
                '}';
    }
}
