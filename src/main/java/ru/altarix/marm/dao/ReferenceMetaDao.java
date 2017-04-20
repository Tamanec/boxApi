package ru.altarix.marm.dao;

import com.jayway.jsonpath.JsonPath;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Repository
public class ReferenceMetaDao {

    @Autowired
    private MongoCollection<Document> templatesCollection;

    public String getTableName(String referenceName) {
        Document reference = templatesCollection.find(
            and(
                eq("class", "reference"),
                eq("source.type", "sql"),
                eq("name", referenceName)
            ))
            .first();
        return JsonPath.read(reference, "$.source.name");
    }

}
