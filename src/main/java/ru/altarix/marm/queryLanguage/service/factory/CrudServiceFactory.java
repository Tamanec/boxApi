package ru.altarix.marm.queryLanguage.service.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.altarix.marm.queryLanguage.language.Language;
import ru.altarix.marm.queryLanguage.language.mongo.MongoLanguage;
import ru.altarix.marm.queryLanguage.language.sql.SqlLanguage;
import ru.altarix.marm.queryLanguage.request.EntityType;
import ru.altarix.marm.queryLanguage.request.FindRequest;
import ru.altarix.marm.queryLanguage.service.mongo.MongoCrudService;
import ru.altarix.marm.queryLanguage.service.sql.SqlCrudService;

@Component
public class CrudServiceFactory {

    @Autowired
    private ApplicationContext context;

    public ru.altarix.marm.queryLanguage.service.CrudService create(FindRequest request) {
        EntityType type = EntityType.findByName(request.getName());
        ru.altarix.marm.queryLanguage.service.CrudService service;
        Language language;

        if (type != null) {
            language = context.getBean(MongoLanguage.class);
            service = new MongoCrudService(language);
        } else {
            language = context.getBean(SqlLanguage.class);
            service = new SqlCrudService(language);
        }

        return service;
    }

}
