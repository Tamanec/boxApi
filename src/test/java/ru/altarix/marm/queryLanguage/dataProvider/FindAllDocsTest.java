package ru.altarix.marm.queryLanguage.dataProvider;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;
import ru.altarix.marm.queryLanguage.request.body.Filter;
import ru.altarix.marm.utils.BsonUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FindAllDocsTest {

    @Autowired
    private MongoClient mongo;

    private Mongo dataProvider;

    @BeforeClass
    public static void setUp() {
        System.out.println("BEFORE CLASS");
    }

    @Before
    public void init() {
        // Готовим dataProvider
        if (dataProvider == null) {
            MongoDatabase db = mongo.getDatabase("nadzor");
            dataProvider = new Mongo(db);
        }
    }

    @Test
    public void equalFilterByTemplate() throws Exception {
        FindAllRequest request = new FindAllRequest();
        request.setName("doc");

        Filter templateFilter = new Filter(
            "template",
            "equal",
            "crosswalk"
        );
        request.addFilter(templateFilter);

        Document doc = dataProvider.find(request);

        assertNotNull(doc);
        assertEquals("crosswalk", doc.get("template", String.class));
    }

    @Test
    public void equalFilterByCharacteristicName() {
        FindAllRequest request = new FindAllRequest();
        request.setName("doc");

        Filter templateFilter = new Filter(
                "data.characteristics.0.name",
                "equal",
                "marking"
        );
        request.addFilter(templateFilter);

        Document doc = dataProvider.find(request);

        assertNotNull(doc);
        //assertEquals("crosswalk", doc.get("template", String.class));
        assertEquals(
            "marking",
            BsonUtils.getValue(
                doc,
                "data.characteristics.0.name",
                String.class
            )
        );
    }

}