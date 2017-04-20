package ru.altarix.marm.queryLanguage.query.mongo;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import ru.altarix.marm.queryLanguage.query.Query;

import java.util.LinkedList;
import java.util.List;

public class MongoFindQuery implements Query<Document> {

    private Bson filters;

    private Bson projection;

    private Bson sort;

    private int limit;

    private int offset;

    private MongoCollection<Document> collection;

    public MongoFindQuery(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public List<Document> execute() {
        FindIterable<Document> query = collection.find(filters)
            .projection(projection)
            .sort(sort)
            .limit(limit)
            .skip(offset);

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

    public MongoFindQuery setFilters(Bson filters) {
        this.filters = filters;
        return this;
    }

    public MongoFindQuery setProjection(Bson projection) {
        this.projection = projection;
        return this;
    }

    public MongoFindQuery setSort(Bson sort) {
        this.sort = sort;
        return this;
    }

    public MongoFindQuery setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public MongoFindQuery setOffset(int offset) {
        this.offset = offset;
        return this;
    }
}
