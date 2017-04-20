package ru.altarix.marm.queryLanguage.service.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.altarix.marm.queryLanguage.dialect.sql.SqlFindDialect;
import ru.altarix.marm.queryLanguage.query.Query;
import ru.altarix.marm.queryLanguage.request.FindRequest;
import ru.altarix.marm.queryLanguage.service.AbstractCrudService;

import java.util.List;
import java.util.Map;

@Service
public class ReferenceCrudService extends AbstractCrudService<Map<String, Object>> {

    @Autowired
    public ReferenceCrudService(SqlFindDialect dialect) {
        super(dialect);
    }

    @Override
    public List<Map<String, Object>> find(FindRequest request) {
        Query<Map<String,Object>> query = dialect.getQuery(request);
        return query.execute();
    }

}
