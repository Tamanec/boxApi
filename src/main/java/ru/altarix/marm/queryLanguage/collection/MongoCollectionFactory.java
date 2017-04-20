package ru.altarix.marm.queryLanguage.collection;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class MongoCollectionFactory {

    @Autowired
    private ApplicationContext context;

    public MongoCollection<Document> create(String name) {
        String dbName = name.equals("doc") ? "docsDb" : "templatesDb";
        String collectionName = name + "s";

        MongoDatabase db = context.getBean(dbName, MongoDatabase.class);
        return db.getCollection(collectionName);
    }

}
