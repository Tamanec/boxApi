package ru.altarix.marm.queryLanguage.request;

import ru.altarix.marm.queryLanguage.request.body.Filter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FindAllRequest {

    private String name;

    private List<Filter> filters = new LinkedList<>();

    private int limit = 0;

    private Map<String, Integer> sort = new HashMap<>();

    public FindAllRequest addFilter(Filter filter) {
        filters.add(filter);
        return this;
    }

    public FindAllRequest addFilter(
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

    public String getName() {
        return name;
    }

    public FindAllRequest setName(String name) {
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

    public FindAllRequest setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public Map<String, Integer> getSort() {
        return sort;
    }

    public FindAllRequest setSort(Map<String, Integer> sort) {
        this.sort = sort;
        return this;
    }

    public FindAllRequest addSort(String fieldName, Integer direction) {
        sort.put(fieldName, direction);
        return this;
    }
}
