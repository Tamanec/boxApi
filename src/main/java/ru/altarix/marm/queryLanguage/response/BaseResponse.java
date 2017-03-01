package ru.altarix.marm.queryLanguage.response;

import java.util.List;

public class BaseResponse {

    private List<Object> data;

    private int code = 200;

    private String Message;

    public BaseResponse() {
    }

    public BaseResponse(List<Object> data) {
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
