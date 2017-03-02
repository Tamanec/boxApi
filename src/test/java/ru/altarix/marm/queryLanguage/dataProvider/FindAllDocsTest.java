package ru.altarix.marm.queryLanguage.dataProvider;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.LogManager;
import org.bson.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.boot.test.context.SpringBootTest;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;
import ru.altarix.marm.queryLanguage.request.body.Filter;
import ru.altarix.marm.utils.BsonUtils;
import ru.altarix.marm.utils.MarmTestWatcher;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
@SpringBootTest
public class FindAllDocsTest {

    private String fieldName;
    private String operator;
    private String fieldValue;

    private Mongo dataProvider;

    @Rule
    public TestWatcher watcher = new MarmTestWatcher(this.getClass().getCanonicalName());

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"template", "equal", "crosswalk"},
                {"data.ext_id", "equal", "185"},
                {"data.characteristics.0.name", "equal", "marking"}
            }
        );
    }

    public FindAllDocsTest(String fieldName, String operator, String fieldValue) throws Exception {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.operator = operator;

        // Готовим подключение к монге
        Properties appProperties = new Properties();
        appProperties.load(new FileReader(new File("src/main/resources/application.properties")));
        String mongoUri = appProperties.getProperty("spring.data.mongodb.uri");

        MongoClient mongo = new MongoClient(new MongoClientURI(mongoUri));
        MongoDatabase db = mongo.getDatabase("nadzor");
        dataProvider = new Mongo(db);
    }

    @Before
    public void init() {
        LogManager.getLogger(this.getClass().getCanonicalName()).info(
            "fieldName=" + fieldName +
            ", operator=" + operator +
            ", fieldValue=" + fieldValue
        );
    }

    @Test
    public void equalFilter() throws Exception {
        FindAllRequest request = new FindAllRequest();
        request.setName("doc");

        Filter templateFilter = new Filter(
            fieldName,
            operator,
            fieldValue
        );
        request.addFilter(templateFilter);

        Document doc = dataProvider.find(request);

        assertNotNull("Document not found", doc);
        assertEquals(fieldValue, BsonUtils.getValue(
            doc,
            fieldName,
            String.class
        ));
    }

}