package ru.altarix.marm.queryLanguage.dialect.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.altarix.marm.queryLanguage.collection.MongoCollectionPool;
import ru.altarix.marm.queryLanguage.dialect.AbstractFindDialect;
import ru.altarix.marm.queryLanguage.dialect.FilterTranslator;
import ru.altarix.marm.queryLanguage.query.mongo.MongoFindQuery;
import ru.altarix.marm.queryLanguage.request.FindRequest;
import ru.altarix.marm.queryLanguage.request.body.Filter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Projections.*;

//@Component
public class MongoFindDialect extends AbstractFindDialect<MongoFindQuery> {

    /*@Autowired
    private MongoCollectionPool collectionPool;*/

    private MongoCollection<Document> collection;

    //@Autowired
    private FilterTranslator<Bson> filterTranslator;

    public MongoFindDialect(
        MongoCollection<Document> collection,
        FilterTranslator<Bson> filterTranslator
    ) {
        this.collection = collection;
        this.filterTranslator = filterTranslator;
    }

    @Override
    protected void initQuery(FindRequest request) {
        //query = new MongoFindQuery(collectionPool.getCollection(request.getName()));
        query = new MongoFindQuery(collection);
    }

    @Override
    protected void addFilters(List<Filter> filters) {
        List<Bson> MongoFiltersList = filterTranslator.translate(filters);
        Bson mongoFilters = MongoFiltersList.size() == 1 ? MongoFiltersList.get(0) : and(MongoFiltersList);

        query.setFilters(mongoFilters);
    }

    @Override
    protected void addProjection(List<String> fields) {
        if (fields == null || fields.isEmpty()) {
            return;
        }

        List<Bson> projections = new LinkedList<>();
        projections.add(include(fields));
        if (!fields.contains("id")) {
            projections.add(excludeId());
        }

        query.setProjection(fields(projections));
    }

    @Override
    protected void addSort(Map<String, Integer> sort) {
        if (sort == null || sort.isEmpty()) {
            return;
        }

        Document mongoSort = new Document();
        for (Map.Entry<String, Integer> entry : sort.entrySet()) {
            mongoSort.append(entry.getKey(), entry.getValue());
        }

        query.setSort(mongoSort);
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
