package com.vgg.fvp.common.utils;

public enum Status {
    IN_PROGRESS("in_progress"),
    DELIVERED("delivered"),
    SENT("sent"),
    CANCELLED("cancelled"),
    PAID("paid"),
    NOT_PAID("not_paid"),
    READ("read"),
    UNREAD("unread");

    private final String status;
    Status(String s){
		this.status = s;
    }
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
