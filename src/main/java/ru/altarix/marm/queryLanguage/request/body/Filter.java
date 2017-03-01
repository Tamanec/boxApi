package ru.altarix.marm.queryLanguage.request.body;

public class Filter {

    private String paramName;

    private String operator;

    private Object value;

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

    public String getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }
}
