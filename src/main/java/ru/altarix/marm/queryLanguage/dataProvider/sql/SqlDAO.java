package ru.altarix.marm.queryLanguage.dataProvider.sql;

import com.jayway.jsonpath.JsonPath;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import ru.altarix.marm.queryLanguage.dataProvider.FilterParser;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.sql.Types;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Repository
public class SqlDAO {

    private MongoDatabase templatesDb;

    private JdbcTemplate pgClient;

    private FilterParser<SqlClause> filterParser;

    @Autowired
    public SqlDAO(MongoDatabase templatesDb, JdbcTemplate pgClient, FilterParser<SqlClause> filterParser) {
        this.templatesDb = templatesDb;
        this.pgClient = pgClient;
        this.filterParser = filterParser;
    }

    public List<Map<String, Object>> find(FindAllRequest request) {
        List<SqlClause> filters = filterParser.parseFilters(request.getFilters());

        StringBuilder query = new StringBuilder("select ")
            .append(getProjection(request))
            .append(" from ")
            .append(getTableName(request))
            .append(getClauseTemplate(filters))
            .append(getSort(request));

        return pgClient.queryForList(
            query.toString(),
            getClauseValues(filters).toArray()
        );
    }

    private String getTableName(FindAllRequest request) {
        MongoCollection<Document> templates = templatesDb.getCollection("templates");
        Document reference = templates.find(
            and(
                eq("class", "reference"),
                eq("source.type", "sql"),
                eq("name", request.getName())
            ))
            .first();
        return JsonPath.read(reference, "$.source.name");
    }

    private String getProjection(FindAllRequest request) {
        return request.hasProjection() ? String.join(",", request.getFields()) : "*";
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

}
