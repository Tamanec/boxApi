package ru.altarix.marm.queryLanguage.dataProvider;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import ru.altarix.marm.api.FindAllRequest;

import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class Mongo {

    private MongoDatabase db;

    public Mongo(MongoDatabase db) {
        this.db = db;
    }

    public Document find(FindAllRequest request) {
        MongoCollection<Document> docs = db.getCollection("docs");
        List<Bson> filters = new LinkedList<>();

        Object value = request.getFilters().get(0).get("value").toString();
        Bson extIdFilter = eq("ext_id", value);
        filters.add(extIdFilter);

        Bson templateFilter = eq("template", "crosswalk");
        filters.add(templateFilter);

        Document doc = docs.find(and(filters)).first();

        if (doc != null) {
            ObjectId id = doc.getObjectId("_id");
            doc.append("id", id.toString());
            doc.remove("_id");
        }

        return doc;
    }

}
