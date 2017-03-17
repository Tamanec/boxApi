package ru.altarix.marm.queryLanguage.response;

import org.bson.Document;

import java.util.LinkedList;
import java.util.List;

public class BaseResponse {

    private List<Document> data = new LinkedList<>();

    private int code = 200;

    private String Message;

    public BaseResponse() {
    }

    public BaseResponse(List<Document> data) {
        this.data = data;
    }

    public BaseResponse addAllDocuments(List<Document> docs) {
        if (docs != null) {
            data.addAll(docs);
        }

        return this;
    }

    public BaseResponse addDocument(Document doc) {
        if (doc != null) {
            data.add(doc);
        }

        return this;
    }

    public List<Document> getData() {
        return data;
    }

    public void setData(List<Document> data) {
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
