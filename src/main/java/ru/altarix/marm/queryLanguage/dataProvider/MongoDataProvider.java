package ru.altarix.marm.queryLanguage.dataProvider;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;

import java.util.LinkedList;
import java.util.List;

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

        MongoCollection<Document> docs = db.getCollection("docs");

        Bson mongoFilters = filters.size() == 1 ? filters.get(0) : and(filters);
        List<Document> result = new LinkedList<>();
        docs.find(mongoFilters).forEach((Block<Document>) doc -> {
            ObjectId id = doc.getObjectId("_id");
            doc.append("id", id.toString());
            doc.remove("_id");

            result.add(doc);
        });

        return result;
    }

}
