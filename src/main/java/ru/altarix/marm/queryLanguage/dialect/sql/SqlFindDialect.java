package ru.altarix.marm.queryLanguage.dialect.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.altarix.marm.dao.ReferenceMetaDao;
import ru.altarix.marm.queryLanguage.dialect.AbstractFindDialect;
import ru.altarix.marm.queryLanguage.dialect.FilterTranslator;
import ru.altarix.marm.queryLanguage.query.sql.SqlFindQuery;
import ru.altarix.marm.queryLanguage.request.FindRequest;
import ru.altarix.marm.queryLanguage.request.body.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SqlFindDialect extends AbstractFindDialect<SqlFindQuery> {

    @Autowired
    private FilterTranslator<SqlClause> filterTranslator;

    @Autowired
    private ReferenceMetaDao referenceMeta;

    @Autowired
    private JdbcTemplate pgClient;

    @Override
    protected void initQuery(FindRequest request) {
        query = new SqlFindQuery(
            referenceMeta.getTableName(request.getName()),
            pgClient
        );
    }

    @Override
    protected void addFilters(List<Filter> filters) {
        List<SqlClause> sqlFilters = filterTranslator.translate(filters);
        query.setFilters(sqlFilters);
    }

    @Override
    protected void addProjection(List<String> fields) {
        String projection = (fields != null && !fields.isEmpty())
            ? String.join(",", fields)
            : "*";
        query.setProjection(projection);

    }

    @Override
    protected void addSort(Map<String, Integer> sort) {
        List<String> sortableFields = new ArrayList<>();
        sort.forEach((field, direction) -> sortableFields
            .add(field + (direction > 0 ? " ASC" : " DESC"))
        );
        query.setSort(String.join(",", sortableFields));
    }

    @Override
    protected void addLimit(int limit) {
        query.setLimit(limit);
    }

    @Override
    protected void addOffset(int offset) {
        query.setOffset(offset);
    }
}
