package ru.altarix.marm.api;

import java.util.List;

public class Response {

    private List<Object> data;

    private int code = 200;

    private String Message;

    public Response() {
    }

    public Response(List<Object> data) {
        this.data = data;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
