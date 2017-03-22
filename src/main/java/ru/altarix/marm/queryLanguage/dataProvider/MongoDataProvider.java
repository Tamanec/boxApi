package ru.altarix.marm.queryLanguage.dataProvider;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;

public class MongoDataProvider {

    private FilterParser filterParser;
    private MongoDatabase db;

    public MongoDataProvider(MongoDatabase db, FilterParser filterParser) {
        this.db = db;
        this.filterParser = filterParser;
    }

    public List<Document> find(FindAllRequest request) {
        List<Bson> filters = filterParser.parseFilters(request.getFilters());
        Bson mongoFilters = filters.size() == 1 ? filters.get(0) : and(filters);

        MongoCollection<Document> docs = db.getCollection("docs");
        List<Document> result = new LinkedList<>();

        FindIterable<Document> cursor = docs
            .find(mongoFilters)
            .limit(request.getLimit());

        if (request.getSort().size() != 0) {
            Document orderBy = new Document();
            for (Map.Entry<String, Integer> entry : request.getSort().entrySet()) {
                orderBy.append(entry.getKey(), entry.getValue());
            }

            cursor.sort(orderBy);
        }

        cursor.forEach((Block<Document>) doc -> {
                ObjectId id = doc.getObjectId("_id");
                doc.append("id", id.toString());
                doc.remove("_id");

                result.add(doc);
            });

        return result;
    }

}
