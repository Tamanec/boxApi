package ru.altarix.marm.queryLanguage.dialect;

import ru.altarix.marm.queryLanguage.request.body.Filter;

import java.util.List;

public interface FilterTranslator<T> {
    public List<T> translate(List<Filter> requestFilters);
}
