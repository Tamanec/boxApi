package ru.altarix.marm.queryLanguage.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseResponse {

    private List<Map<String, Object>> data = new ArrayList<>();

    private int code = 200;

    private String Message;

    public BaseResponse() {
    }

    public BaseResponse(List<Map<String, Object>> data) {
        this.data = data;
    }

    public BaseResponse addDocuments(List<Map<String, Object>> docs) {
        if (docs != null) {
            data.addAll(docs);
        }

        return this;
    }

    public BaseResponse addDocument(Map<String, Object> doc) {
        if (doc != null) {
            data.add(doc);
        }

        return this;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
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
