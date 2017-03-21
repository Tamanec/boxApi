package ru.altarix.marm;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import ru.altarix.marm.queryLanguage.dataProvider.FilterParser;
import ru.altarix.marm.queryLanguage.dataProvider.MongoDataProvider;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class DocsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocsApiApplication.class, args);
	}

	@Bean
	protected Properties appProperties() throws IOException {
        Resource resource = new ClassPathResource("/application.properties");
        return PropertiesLoaderUtils.loadProperties(resource);
    }

	@Bean
    public MongoClient mongoClient() throws IOException {
        MongoClientURI uri = new MongoClientURI(appProperties().getProperty("spring.data.mongodb.uri"));
	    return new MongoClient(uri);
    }

	@Bean
	public MongoDatabase mongoDatabase() throws IOException {
		return mongoClient().getDatabase(appProperties().getProperty("mongo.db.docs"));
	}

    @Bean
    public FilterParser filterParser() {
        return new FilterParser();
    }

	@Bean
	public MongoDataProvider mongoDataProvider() throws IOException {
	    return new MongoDataProvider(mongoDatabase(), filterParser());
    }

    @Bean
	public Filter logRequestFilter() {
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludeClientInfo(true);
		filter.setIncludeHeaders(true);
		filter.setIncludeQueryString(true);
		filter.setIncludePayload(true);
		filter.setMaxPayloadLength(5120);
		return filter;
	}

}
