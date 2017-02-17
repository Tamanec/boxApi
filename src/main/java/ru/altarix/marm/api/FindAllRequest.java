package ru.altarix.marm.api;

import java.util.List;
import java.util.Map;

public class FindAllRequest {

    private String name;

    private List<Map<String, Object>> filters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, Object>> getFilters() {
        return filters;
    }

    public void setFilters(List<Map<String, Object>> filters) {
        this.filters = filters;
    }
}
