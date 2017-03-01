package ru.altarix.marm.queryLanguage.request;

import ru.altarix.marm.queryLanguage.request.body.Filter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FindAllRequest {

    private String name;

    private List<Filter> filters = new LinkedList<>();

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
