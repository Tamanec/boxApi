package ru.altarix.marm.queryLanguage.dataProvider;

import ru.altarix.marm.queryLanguage.request.body.Filter;

import java.util.List;

public interface FilterParser<T> {
    public List<T> parseFilters(List<Filter> requestFilters);
}
