package ru.altarix.marm.queryLanguage.dataProvider.sql;

import ru.altarix.marm.queryLanguage.dataProvider.FilterParser;
import ru.altarix.marm.queryLanguage.request.body.Filter;
import ru.altarix.marm.queryLanguage.request.body.Operator;

import java.util.LinkedList;
import java.util.List;

public class SqlFilterParser implements FilterParser<String> {

    @Override
    public List<String> parseFilters(List<Filter> requestFilters) {
        List<String> filters = new LinkedList<>();
        for (Filter filter : requestFilters) {
            filters.add(getSqlFilter(filter));
        }

        return filters;
    }

    private String getSqlFilter(Filter filter) throws UnsupportedOperationException {
        StringBuilder sqlFilter = new StringBuilder();
        Operator operator = Operator.findByName(filter.getOperator());
        switch (operator) {
            case EQUAL:
                sqlFilter.append(filter.getParamName())
                    .append("=")
                    .append(filter.getValue());
                break;
            default:
                throw new UnsupportedOperationException("Unknown operator: " + operator.getName());
        }

        return sqlFilter.toString();
    }

}
