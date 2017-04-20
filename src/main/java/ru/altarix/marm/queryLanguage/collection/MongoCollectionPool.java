package ru.altarix.marm.queryLanguage.collection;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoCollectionPool {

    @Autowired
    private MongoDatabase docsDb;

    @Autowired
    private MongoDatabase templatesDb;

    public MongoCollection<Document> getCollection(String name) {
        return name == "doc" ? docsDb.getCollection("docs") : templatesDb.getCollection(name + "s");
    }

}
