package ru.altarix.marm.queryLanguage.service.mongo;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.altarix.marm.queryLanguage.dialect.AbstractFindDialect;
import ru.altarix.marm.queryLanguage.dialect.Dialect;
import ru.altarix.marm.queryLanguage.dialect.mongo.MongoFindDialect;
import ru.altarix.marm.queryLanguage.language.Language;
import ru.altarix.marm.queryLanguage.query.Query;
import ru.altarix.marm.queryLanguage.request.FindRequest;
import ru.altarix.marm.queryLanguage.service.AbstractCrudService;

import java.util.List;

public class MongoCrudService extends AbstractCrudService<Document> {

    public MongoCrudService(Language language) {
        super(language);
    }

    @Override
    public List<Document> find(FindRequest request) {
        Query<Document> query = language.getFindQuery(request);
        return query.execute();
    }
}
