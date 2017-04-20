package ru.altarix.marm.queryLanguage.service.sql;

import ru.altarix.marm.queryLanguage.language.Language;
import ru.altarix.marm.queryLanguage.query.Query;
import ru.altarix.marm.queryLanguage.request.FindRequest;
import ru.altarix.marm.queryLanguage.service.AbstractCrudService;

import java.util.List;
import java.util.Map;

public class SqlCrudService extends AbstractCrudService<Map<String, Object>> {

    public SqlCrudService(Language language) {
        super(language);
    }

    @Override
    public List<Map<String, Object>> find(FindRequest request) {
        Query<Map<String,Object>> query = language.getFindQuery(request);
        return query.execute();
    }

}
