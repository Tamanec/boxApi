package ru.altarix.marm.queryLanguage.request.body;

import java.util.LinkedList;
import java.util.List;

public class Filter {

    private String paramName;

    private String operator;

    private Object value;

    private List<String> modificators = new LinkedList<>();

    public Filter() {
    }

    public Filter(String operator, Object value) {
        this.operator = operator;
        this.value = value;
    }

    public Filter(String paramName, String operator, Object value) {
        this.paramName = paramName;
        this.operator = operator;
        this.value = value;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<String> getModificators() {
        return modificators;
    }

    public void setModificators(List<String> modificators) {
        this.modificators = modificators;
    }
}
