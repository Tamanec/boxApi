package ru.altarix.marm.queryLanguage.dataProvider.sql;

import org.springframework.stereotype.Service;
import ru.altarix.marm.queryLanguage.dataProvider.FilterParser;
import ru.altarix.marm.queryLanguage.request.body.Filter;
import ru.altarix.marm.queryLanguage.request.body.Operator;

import java.util.*;

@Service
public class SqlFilterParser implements FilterParser<SqlClause> {

    private Map<Operator, String> sqlOpsMap = new HashMap<>();

    public SqlFilterParser() {
        sqlOpsMap.put(Operator.EQUAL, " = ");
        sqlOpsMap.put(Operator.NOT_EQUAL, " != ");
        sqlOpsMap.put(Operator.IN, " in ");
        sqlOpsMap.put(Operator.NOT_IN, " not in ");
        sqlOpsMap.put(Operator.GREATER_THAN, " > ");
        sqlOpsMap.put(Operator.GREATER_THAN_EQUAL, " >= ");
        sqlOpsMap.put(Operator.LESS_THAN, " < ");
        sqlOpsMap.put(Operator.LESS_THAN_EQUAL, " <= ");
        sqlOpsMap.put(Operator.REGEX, " ~ ");
        sqlOpsMap.put(Operator.AND, " and ");
        sqlOpsMap.put(Operator.OR, " or ");
        sqlOpsMap.put(Operator.EXISTS, " is not null = ");
    }

    @Override
    public List<SqlClause> parseFilters(List<Filter> requestFilters) {
        List<SqlClause> filters = new LinkedList<>();
        for (Filter filter : requestFilters) {
            filters.add(getSqlFilter(filter));
        }

        return filters;
    }

    private SqlClause getSqlFilter(Filter filter) throws UnsupportedOperationException {
        StringBuilder sqlFilter = new StringBuilder();
        SqlClause clause = new SqlClause();
        Operator operator = Operator.findByName(filter.getOperator());
        switch (operator) {
            case EQUAL:
            case NOT_EQUAL:
            case GREATER_THAN:
            case GREATER_THAN_EQUAL:
            case LESS_THAN:
            case LESS_THAN_EQUAL:
            case EXISTS:
                sqlFilter.append(filter.getParamName())
                    .append(sqlOpsMap.get(operator))
                    .append("?");
                clause.setTemplate(sqlFilter.toString())
                    .addValue(filter.getValue());
                break;

            case IN:
            case NOT_IN:
                sqlFilter.append(filter.getParamName())
                    .append(sqlOpsMap.get(operator))
                    .append(" (")
                    .append(String.join(
                        ",",
                        Collections.nCopies(
                            ((List) filter.getValue()).size(),
                            "?"
                        )
                    ))
                    .append(")");
                clause.setTemplate(sqlFilter.toString()).
                    addAllValues((List<Object>) filter.getValue());
                break;

            case REGEX:
                String sqlOperator = (
                    !filter.getModificators().isEmpty() &&
                    filter.getModificators().contains("i")
                ) ? " ~* " : sqlOpsMap.get(operator);

                sqlFilter.append(filter.getParamName())
                    .append(sqlOperator)
                    .append("?");
                clause.setTemplate(sqlFilter.toString())
                    .addValue(filter.getValue());
                break;

            case AND:
            case OR:
                List<SqlClause> subClauses = parseFilters((List<Filter>) filter.getValue());

                List<String> templates = new LinkedList<>();
                subClauses.forEach((sqlClause -> {
                    templates.add(sqlClause.getTemplate());
                    clause.addAllValues(sqlClause.getValues());
                }));

                sqlFilter.append(String.join(sqlOpsMap.get(operator), templates));
                sqlFilter.insert(0, "(").append(")");

                clause.setTemplate(sqlFilter.toString());
                break;

            default:
                throw new UnsupportedOperationException("Unknown operator: " + operator.getName());
        }

        return clause;
    }

}
