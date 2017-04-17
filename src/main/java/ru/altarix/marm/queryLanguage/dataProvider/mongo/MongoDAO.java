package ru.altarix.marm.queryLanguage.dataProvider.mongo;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.altarix.marm.queryLanguage.dataProvider.FilterParser;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Projections.*;

@Repository
public class MongoDAO {

    private FilterParser<Bson> filterParser;

    private MongoCollection<Document> docsCollection;

    @Autowired
    public MongoDAO(MongoCollection<Document> docsCollection, FilterParser<Bson> filterParser) {
        this.docsCollection = docsCollection;
        this.filterParser = filterParser;
    }

    public List<Document> find(FindAllRequest request) {
        List<Bson> filters = filterParser.parseFilters(request.getFilters());
        Bson mongoFilters = filters.size() == 1 ? filters.get(0) : and(filters);

        FindIterable<Document> query = docsCollection.find(mongoFilters)
            .limit(request.getLimit())
            .skip(request.getOffset());

        addSort(request, query);
        addProjection(request, query);

        return execute(query);
    }

    private List<Document> execute(FindIterable<Document> query) {
        List<Document> result = new LinkedList<>();
        query.forEach((Block<Document>) doc -> {
                if (doc.containsKey("_id")) {
                    ObjectId id = doc.getObjectId("_id");
                    doc.append("id", id.toString());
                    doc.remove("_id");
                }

                result.add(doc);
            });
        return result;
    }

    private void addProjection(FindAllRequest request, FindIterable<Document> query) {
        if (request.hasProjection()) {
            List<Bson> projections = new LinkedList<>();
            projections.add(include(request.getFields()));
            if (!request.getFields().contains("id")) {
                projections.add(excludeId());
            }

            query.projection(fields(projections));
        }
    }

    private void addSort(FindAllRequest request, FindIterable<Document> query) {
        if (request.hasSort()) {
            Document orderBy = new Document();
            for (Map.Entry<String, Integer> entry : request.getSort().entrySet()) {
                orderBy.append(entry.getKey(), entry.getValue());
            }

            query.sort(orderBy);
        }
    }

    private FindIterable<Document> getDocuments(
        int limit,
        int offset,
        Bson mongoFilters,
        MongoCollection<Document> docs
    ) {
        return docs
                .find(mongoFilters)
                .limit(limit)
                .skip(offset);
    }

}
