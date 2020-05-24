package com.vgg.fvp.common.utils;

public class AppResponse {

    private String data;
    private String type;

    public AppResponse(String data) {
        this.data = data;
    }

    public AppResponse(String data, String type) {
        this.data = data;
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
