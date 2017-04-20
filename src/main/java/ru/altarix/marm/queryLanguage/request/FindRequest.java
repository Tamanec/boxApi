package ru.altarix.marm.queryLanguage.request;

import ru.altarix.marm.queryLanguage.request.body.Filter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FindRequest {

    private String name;

    private List<Filter> filters = new LinkedList<>();

    private int limit = 0;

    private Map<String, Integer> sort = new HashMap<>();

    private List<String> fields = new LinkedList<>();

    private int offset = 0;

    public FindRequest addFilter(Filter filter) {
        filters.add(filter);
        return this;
    }

    public FindRequest addFilter(
        String paramName,
        String operator,
        Object value
    ) {
        filters.add(new Filter(
            paramName,
            operator,
            value
        ));
        return this;
    }

    public Boolean hasFilters() {
        return !filters.isEmpty();
    }

    public String getName() {
        return name;
    }

    public FindRequest setName(String name) {
        this.name = name;
        return this;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public int getLimit() {
        return limit;
    }

    public FindRequest setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public Map<String, Integer> getSort() {
        return sort;
    }

    public FindRequest setSort(Map<String, Integer> sort) {
        this.sort = sort;
        return this;
    }

    public FindRequest addSort(String fieldName, Integer direction) {
        sort.put(fieldName, direction);
        return this;
    }

    public Boolean hasSort() {
        return !sort.isEmpty();
    }

    public List<String> getFields() {
        return fields;
    }

    public FindRequest setFields(List<String> fields) {
        this.fields = fields;
        return this;
    }

    public Boolean hasProjection() {
        return !fields.isEmpty();
    }

    public int getOffset() {
        return offset;
    }

    public FindRequest setOffset(int offset) {
        this.offset = offset;
        return this;
    }
}
