package ru.altarix.marm.queryLanguage.dataProvider;

import org.bson.Document;
import org.bson.conversions.Bson;
import ru.altarix.marm.queryLanguage.request.body.Filter;
import ru.altarix.marm.queryLanguage.request.body.Operator;

import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class FilterParser {

    public List<Bson> parseFilters(List<Filter> requestFilters) {
        List<Bson> filters = new LinkedList<>();
        Bson mongoFilter;
        Operator operator;
        for (Filter filter : requestFilters) {
            operator = Operator.findByName(filter.getOperator());
            mongoFilter = getMongoFilter(filter, operator);
            filters.add(mongoFilter);
        }

        return filters;
    }

    private Bson getMongoFilter(Filter filter, Operator operator) throws UnsupportedOperationException {
        Bson mongoFilter;
        switch (operator) {
            case EQUAL:
                mongoFilter = eq(filter.getParamName(), filter.getValue());
                break;
            case NOT_EQUAL:
                mongoFilter = ne(filter.getParamName(), filter.getValue());
                break;
            case IN:
                mongoFilter = new Document(
                    filter.getParamName(),
                    new Document("$in", filter.getValue())
                );
                break;
            case NIN:
                mongoFilter = new Document(
                    filter.getParamName(),
                    new Document("$nin", filter.getValue())
                );
                break;
            case GT:
                mongoFilter = gt(filter.getParamName(), filter.getValue());
                break;
            case GTE:
                mongoFilter = gte(filter.getParamName(), filter.getValue());
                break;
            case LT:
                mongoFilter = lt(filter.getParamName(), filter.getValue());
                break;
            case LTE:
                mongoFilter = lte(filter.getParamName(), filter.getValue());
                break;
            case REGEX:
                String value = filter.getValue().toString();
                if (!value.startsWith("/") && !value.endsWith("/")) {
                    value = "/" + value + "/";
                }

                Document regex = new Document("$regex", value);
                if (!filter.getModificators().isEmpty()) {
                    regex.append("$options", filter.getModificators());
                }

                mongoFilter = new Document(filter.getParamName(), regex);
                break;
            case AND:
                mongoFilter = and(parseFilters((List<Filter>) filter.getValue()));
                break;
            case OR:
                mongoFilter = or(parseFilters((List<Filter>) filter.getValue()));
                break;
            case EXISTS:
                mongoFilter = new Document(
                    filter.getParamName(),
                    new Document("$exists", filter.getValue())
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown operator: " + operator.getName());
        }

        return mongoFilter;
    }
}