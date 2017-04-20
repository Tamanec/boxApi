package ru.altarix.marm.queryLanguage.dialect;

import ru.altarix.marm.queryLanguage.query.Query;
import ru.altarix.marm.queryLanguage.request.FindRequest;
import ru.altarix.marm.queryLanguage.request.body.Filter;

import java.util.List;
import java.util.Map;

abstract public class AbstractFindDialect implements Dialect<Query> {

    protected Query query;
    
    @Override
    public Query getQuery(FindRequest request) {
        initQuery(request);

        addFilters(request.getFilters());
        addProjection(request.getFields());
        addSort(request.getSort());
        addLimit(request.getLimit());
        addOffset(request.getOffset());

        return query;
    }

    abstract protected void initQuery(FindRequest request);

    abstract protected void addFilters(List<Filter> filters);

    abstract protected void addProjection(List<String> fields);

    abstract protected void addSort(Map<String, Integer> sort);

    abstract protected void addLimit(int limit);

    abstract protected void addOffset(int offset);

}
