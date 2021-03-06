package ru.altarix.marm.queryLanguage.language.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.altarix.marm.queryLanguage.dialect.Dialect;
import ru.altarix.marm.queryLanguage.language.AbstractLanguage;
import ru.altarix.marm.queryLanguage.query.Query;
import ru.altarix.marm.queryLanguage.request.FindRequest;

@Component
public class MongoLanguage extends AbstractLanguage {

    @Autowired
    public MongoLanguage(@Qualifier("mongoFindDialect") Dialect findDialect) {
        super(findDialect);
    }

    @Override
    public Query getFindQuery(FindRequest request) {
        return findDialect.getQuery(request);
    }

}
