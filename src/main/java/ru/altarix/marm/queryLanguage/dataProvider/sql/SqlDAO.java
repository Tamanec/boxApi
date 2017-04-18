package ru.altarix.marm.queryLanguage.dataProvider.sql;

import com.jayway.jsonpath.JsonPath;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.altarix.marm.queryLanguage.dataProvider.FilterParser;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Repository
public class SqlDAO {

    private MongoCollection<Document> templatesCollection;

    private JdbcTemplate pgClient;

    private FilterParser<SqlClause> filterParser;

    @Autowired
    public SqlDAO(MongoCollection<Document> templatesCollection, JdbcTemplate pgClient, FilterParser<SqlClause> filterParser) {
        this.templatesCollection = templatesCollection;
        this.pgClient = pgClient;
        this.filterParser = filterParser;
    }

    public List<Map<String, Object>> find(FindAllRequest request) {
        List<SqlClause> filters = filterParser.parseFilters(request.getFilters());

        StringBuilder query = new StringBuilder("select ")
            .append(getProjection(request))
            .append(" from ")
            .append(getTableName(request.getName()))
            .append(getClauseTemplate(filters))
            .append(getSort(request))
            .append(getLimit(request))
            .append(getOffset(request));

        return pgClient.queryForList(
            query.toString(),
            getClauseValues(filters).toArray()
        );
    }

    private String getTableName(String referenceName) {
        Document reference = templatesCollection.find(
            and(
                eq("class", "reference"),
                eq("source.type", "sql"),
                eq("name", referenceName)
            ))
            .first();
        return JsonPath.read(reference, "$.source.name");
    }

    private String getProjection(FindAllRequest request) {
        return request.hasProjection() ? String.join(",", request.getFields()) : "*";
    }

    private String getSort(FindAllRequest request) {
        if (!request.hasSort()) {
            return "";
        }

        List<String> fields = new LinkedList<>();
        request.getSort().forEach((field, direction) -> fields
            .add(field + (direction > 0 ? " ASC" : " DESC"))
        );

        return " order by " + String.join(",", fields);
    }

    private String getLimit(FindAllRequest request) {
        return (request.getLimit() != 0) ? " limit " + request.getLimit() : "";
    }

    private String getOffset(FindAllRequest request) {
        return (request.getOffset() != 0) ? " offset " + request.getOffset() : "";
    }

    private String getClauseTemplate(List<SqlClause> filters) {
        if (filters.size() == 0) {
            return "";
        }

        List<String> templates = new LinkedList<>();
        filters.forEach(sqlClause -> {
            templates.add(sqlClause.getTemplate());
        });
        String sqlFilters = templates.size() == 1 ? templates.get(0) : String.join(" and ", templates);

        return " where " + sqlFilters;
    }

    private List<Object> getClauseValues(List<SqlClause> filters) {
        List<Object> values = new LinkedList<>();
        if (filters.size() != 0) {
            filters.forEach(sqlClause -> values.addAll(sqlClause.getValues()));
        }

        return values;
    }

}
