package ru.altarix.marm.queryLanguage.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${mongo.db.docs}")
    private String docsDbName;

    @Value("${mongo.db.templates}")
    private String templatesDbName;

    @Bean
    public MongoClient mongoClient() {
        MongoClientURI uri = new MongoClientURI(mongoUri);
        return new MongoClient(uri);
    }

    @Bean
    public MongoDatabase docsDb() {
        return mongoClient().getDatabase(docsDbName);
    }

    @Bean
    public MongoDatabase templatesDb() {
        return mongoClient().getDatabase(templatesDbName);
    }

    @Bean
    public MongoCollection<Document> docsCollection() {
        return docsDb().getCollection("docs");
    }

    @Bean
    public MongoCollection<Document> templatesCollection() {
        return templatesDb().getCollection("templates");
    }

}
