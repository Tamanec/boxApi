package ru.altarix.marm;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import ru.altarix.marm.queryLanguage.dataProvider.mongo.MongoFilterParser;
import ru.altarix.marm.queryLanguage.dataProvider.mongo.MongoDataProvider;
import ru.altarix.marm.queryLanguage.dataProvider.sql.SqlDataProvider;
import ru.altarix.marm.queryLanguage.dataProvider.sql.SqlFilterParser;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class DocsApiApplication {

    @Autowired
    Environment env;

	public static void main(String[] args) {
		SpringApplication.run(DocsApiApplication.class, args);
	}

	@Bean
    public MongoClient mongoClient() {
        MongoClientURI uri = new MongoClientURI(env.getProperty("spring.data.mongodb.uri"));
	    return new MongoClient(uri);
    }

	@Bean
	public MongoDatabase docsDb() {
		return mongoClient().getDatabase(env.getProperty("mongo.db.docs"));
	}

	@Bean
	public MongoDatabase templatesDb() {
		return mongoClient().getDatabase(env.getProperty("mongo.db.templates"));
	}

    @Bean
    public MongoFilterParser mongoFilterParser() {
        return new MongoFilterParser();
    }

	@Bean
	public MongoDataProvider mongoDataProvider() {
	    return new MongoDataProvider(docsDb(), mongoFilterParser());
    }

    @Bean
    public SqlFilterParser sqlFilterParser() {
	    return new SqlFilterParser();
    }

	@Bean
	public SqlDataProvider sqlDataProvider() {
		return new SqlDataProvider(templatesDb(), jdbcTemplate(), sqlFilterParser());
	}

	@Bean
    public JdbcTemplate jdbcTemplate() {
	    return new JdbcTemplate(pgDataSource());
    }

	@Bean
	public DataSource pgDataSource() {
	    BasicDataSource dataSource = new BasicDataSource();
	    dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
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
