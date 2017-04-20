package ru.altarix.marm.queryLanguage.service.mongo;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.altarix.marm.queryLanguage.dialect.Dialect;
import ru.altarix.marm.queryLanguage.dialect.mongo.MongoFindDialect;
import ru.altarix.marm.queryLanguage.query.Query;
import ru.altarix.marm.queryLanguage.request.FindRequest;
import ru.altarix.marm.queryLanguage.service.AbstractCrudService;

import java.util.List;

//@Service
public class DocsCrudService extends AbstractCrudService<Document> {

    //@Autowired
    public DocsCrudService(Dialect<? extends Query<Document>> dialect) {
        super(dialect);
    }

    @Override
    public List<Document> find(FindRequest request) {
        Query<Document> query = dialect.getQuery(request);
        return query.execute();
    }
}
