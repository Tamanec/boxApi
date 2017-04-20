package ru.altarix.marm.queryLanguage.query.sql;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.altarix.marm.queryLanguage.dialect.sql.SqlClause;
import ru.altarix.marm.queryLanguage.query.Query;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SqlFindQuery implements Query<Map<String,Object>> {

    private JdbcTemplate pgClient;

    private String tableName;

    private List<SqlClause> filters;

    private String projection;

    private String sort;

    private int limit;

    private int offset;

    public SqlFindQuery(String tableName, JdbcTemplate pgClient) {
        this.tableName = tableName;
        this.pgClient = pgClient;
    }

    @Override
    public List<Map<String, Object>> execute() {
        StringBuilder query = new StringBuilder("select ")
            .append(projection)
            .append(" from ")
            .append(tableName)
            .append(getClauseTemplate(filters))
            .append(getSortSql())
            .append(getLimitSql())
            .append(getOffsetSql());

        return pgClient.queryForList(query.toString(), getClauseValues(filters).toArray());
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

    private String getSortSql() {
        return sort != null && !sort.isEmpty() ? " order by " + sort : "";
    }

    private String getLimitSql() {
        return limit > 0 ? " limit " + limit : "";
    }

    private String getOffsetSql() {
        return offset > 0 ? " offset " + offset : "";
    }

    public SqlFindQuery setFilters(List<SqlClause> filters) {
        this.filters = filters;
        return this;
    }

    public SqlFindQuery setProjection(String projection) {
        this.projection = projection;
        return this;
    }

    public SqlFindQuery setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public SqlFindQuery setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public SqlFindQuery setOffset(int offset) {
        this.offset = offset;
        return this;
    }
}
