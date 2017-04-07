package ru.altarix.marm.queryLanguage.dataProvider.sql;

import java.util.LinkedList;
import java.util.List;

public class SqlClause {

    private String template;

    private List<Object> values = new LinkedList<>();

    public String getTemplate() {
        return template;
    }

    public SqlClause setTemplate(String template) {
        this.template = template;
        return this;
    }

    public List<Object> getValues() {
        return values;
    }

    public SqlClause setValues(List<Object> values) {
        this.values = values;
        return this;
    }

    public SqlClause addValue(Object value) {
        this.values.add(value);
        return this;
    }
}
