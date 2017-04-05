package ru.altarix.marm.queryLanguage.dataProvider.sql;

import com.jayway.jsonpath.JsonPath;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.altarix.marm.queryLanguage.dataProvider.FilterParser;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class SqlDataProvider {

    private MongoDatabase templatesDb;

    private JdbcTemplate pgClient;

    private FilterParser<String> filterParser;

    public SqlDataProvider(MongoDatabase templatesDb, JdbcTemplate pgClient, FilterParser<String> filterParser) {
        this.templatesDb = templatesDb;
        this.pgClient = pgClient;
        this.filterParser = filterParser;
    }

    public List<Map<String, Object>> find(FindAllRequest request) {
        List<String> filters = filterParser.parseFilters(request.getFilters());
        String sqlFilters = filters.size() == 1 ? filters.get(0) : String.join(" and ", filters);
        String clause = filters.size() != 0 ? " where " + sqlFilters : "";

        String tableName = getTableName(request);

        StringBuilder query = new StringBuilder("select * from ")
            .append(tableName)
            .append(clause);
        return pgClient.queryForList(query.toString());
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

}
