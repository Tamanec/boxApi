package ru.altarix.marm.queryLanguage.dialect.mongo;

import antlr.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;
import ru.altarix.marm.queryLanguage.dialect.FilterTranslator;
import ru.altarix.marm.queryLanguage.request.body.Filter;
import ru.altarix.marm.queryLanguage.request.body.Operator;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;

@Component
public class MongoFilterTranslator implements FilterTranslator<Bson> {

    public List<Bson> translate(List<Filter> requestFilters) {
        List<Bson> filters = new LinkedList<>();
        for (Filter filter : requestFilters) {
            filters.add(getMongoFilter(filter));
        }

        return filters;
    }

    private Bson getMongoFilter(Filter filter) throws UnsupportedOperationException {
        Bson mongoFilter;
        Operator operator = Operator.findByName(filter.getOperator());
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
            case NOT_IN:
                mongoFilter = new Document(
                    filter.getParamName(),
                    new Document("$nin", filter.getValue())
                );
                break;
            case GREATER_THAN:
                mongoFilter = gt(filter.getParamName(), filter.getValue());
                break;
            case GREATER_THAN_EQUAL:
                mongoFilter = gte(filter.getParamName(), filter.getValue());
                break;
            case LESS_THAN:
                mongoFilter = lt(filter.getParamName(), filter.getValue());
                break;
            case LESS_THAN_EQUAL:
                mongoFilter = lte(filter.getParamName(), filter.getValue());
                break;
            case REGEX:
                String value = StringUtils.stripFrontBack(
                    filter.getValue().toString(),
                    "/",
                    "/");
                Pattern regPattern = Pattern.compile(value);

                Document regex = new Document("$regex", value);
                if (!filter.getModificators().isEmpty()) {
                    regex.append("$options", String.join("", filter.getModificators()));
                }

                mongoFilter = new Document(filter.getParamName(), regex);
                break;
            case AND:
                mongoFilter = and(translate((List<Filter>) filter.getValue()));
                break;
            case OR:
                mongoFilter = or(translate((List<Filter>) filter.getValue()));
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