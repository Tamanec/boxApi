package ru.altarix.marm.queryLanguage.request.body;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Filter {

    private String paramName;

    private String operator;

    private Object value;

    private List<String> modificators = new LinkedList<>();

    public Filter() {
    }

    public Filter(String operator, Object value) {
        this.operator = operator;
        setValue(value);
    }

    public Filter(String paramName, String operator, Object value) {
        this.paramName = paramName;
        this.operator = operator;
        setValue(value);
    }

    public Filter(Map<String, Object> rawFilter) {
        paramName = rawFilter.containsKey("paramName") ? rawFilter.get("paramName").toString() : null;
        operator = rawFilter.get("operator").toString();
        setValue(rawFilter.get("value"));
        modificators = rawFilter.containsKey("modificators") ? (List<String>) rawFilter.get("modificators") : null;
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
        Operator op = (operator != null) ? Operator.findByName(operator) : null;
        if (op == Operator.AND || op == Operator.OR) {
            List<Filter> filtersList = new LinkedList<>();
            List<Map<String, Object>> rawFiltersList = (List<Map<String, Object>>) value;
            for (Map<String, Object> rawFilter : rawFiltersList) {
                Filter filter = new Filter(rawFilter);
                filtersList.add(filter);
            }

            this.value = filtersList;
            return;
        }

        this.value = value;
    }

    public List<String> getModificators() {
        return modificators;
    }

    public void setModificators(List<String> modificators) {
        this.modificators = modificators;
    }
}
