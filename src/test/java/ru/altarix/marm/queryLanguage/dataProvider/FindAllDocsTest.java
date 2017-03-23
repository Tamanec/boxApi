package ru.altarix.marm.queryLanguage.dataProvider;

import com.jayway.jsonpath.JsonPath;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;
import ru.altarix.marm.queryLanguage.request.body.Filter;
import ru.altarix.marm.utils.MarmTestWatcher;

import javax.print.Doc;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class FindAllDocsTest {

    private MongoDataProvider dataProvider;

    private Logger logger;

    @Rule
    public TestWatcher watcher = new MarmTestWatcher(this.getClass().getCanonicalName());

    public FindAllDocsTest() throws Exception {
        logger = LogManager.getLogger(this.getClass().getCanonicalName());

        // Готовим подключение к монге
        Properties appProperties = new Properties();
        appProperties.load(new FileReader(new File("src/main/resources/application.properties")));
        String mongoUri = appProperties.getProperty("spring.data.mongodb.uri");

        MongoClient mongo = new MongoClient(new MongoClientURI(mongoUri));
        MongoDatabase db = mongo.getDatabase("nadzor");
        dataProvider = new MongoDataProvider(db, new FilterParser());
    }

    @Test
    public void equalFilter() throws Exception {
        checkEqualFilter("template", "crosswalk", "$.template");
        checkEqualFilter("data.ext_id", "185", "$.data.ext_id");
        checkEqualFilter("data.characteristics.0.name", "marking", "$.data.characteristics[0].name");
    }

    @Test
    public void twoFilters() {
        logger.info("Test 'twoFilters': (template equal crosswalk) and (status equal imported)");

        FindAllRequest request = new FindAllRequest()
            .setName("doc")
            .addFilter(
                "template",
                "equal",
                "crosswalk"
            );

        Filter statusFilter = new Filter(
            "status",
            "equal",
            "imported"
        );
        request.addFilter(statusFilter);

        Document doc = dataProvider.find(request).get(0);

        assertNotNull("Document not found", doc);
        assertEquals("crosswalk", JsonPath.read(doc,"$.template"));
        assertEquals("imported", JsonPath.read(doc,"$.status"));
    }

    @Test
    public void notEqualFilter() {
        Document doc = getDocument("data.ext_id", "ne", "185");

        assertNotNull("Document not found", doc);
        assertNotEquals("185", JsonPath.read(doc, "$.data.ext_id"));
    }

    @Test
    public void inFilter() {
        Document doc = getDocument("data.ext_id","in", Arrays.asList("185", "186"));

        assertNotNull("Document not found", doc);
        assertThat((String) JsonPath.read(doc, "$.data.ext_id")).isIn("185", "186");
    }

    @Test
    public void notInFilter() {
        Document doc = getDocument("data.ext_id","nin", Arrays.asList("185", "186"));

        assertNotNull("Document not found", doc);
        assertThat((String) JsonPath.read(doc, "$.data.ext_id")).isNotIn("185", "186");
    }

    @Test
    public void greaterThanFilter() {
        int publishDate = 1473936928;
        Document doc = getDocument("publishDate", "gt", publishDate);

        assertNotNull("Document not found", doc);
        assertThat((Long) JsonPath.read(doc, "$.publishDate")).isGreaterThan(publishDate);
    }

    @Test
    public void greaterThanOrEqualFilter() {
        int publishDate = 1473936928;
        Document doc = getDocument("publishDate", "gte", publishDate);

        assertNotNull("Document not found", doc);
        assertThat((Long) JsonPath.read(doc, "$.publishDate")).isGreaterThanOrEqualTo(publishDate);
    }

    @Test
    public void lessThanFilter() {
        int publishDate = 1473936929;
        Document doc = getDocument("publishDate", "lt", publishDate);

        assertNotNull("Document not found", doc);
        assertThat((Long) JsonPath.read(doc, "$.publishDate")).isLessThan(publishDate);
    }

    @Test
    public void lessThanOrEqualFilter() {
        int publishDate = 1473936928;
        Document doc = getDocument("publishDate", "lte", publishDate);

        assertNotNull("Document not found", doc);
        assertThat((Long) JsonPath.read(doc, "$.publishDate")).isLessThanOrEqualTo(publishDate);
    }

    @Test
    public void regexFilter() {
        String[] regex = {".*ХРУСТАЛЬНОГО.*", ".*wal.*", ".*walk", "cross.*", ".*хрустального.*"};

        Document doc = getDocument("nameUpper", "regex", regex[0]);
        assertThat(doc).isNotNull();
        assertThat((String) JsonPath.read(doc, "$.nameUpper")).contains("ХРУСТАЛЬНОГО");

        doc = getDocument("template", "regex", regex[1]);
        assertThat(doc).isNotNull();
        assertThat((String) JsonPath.read(doc, "$.template")).isEqualTo("crosswalk");

        doc = getDocument("template", "regex", regex[2]);
        assertThat(doc).isNotNull();
        assertThat((String) JsonPath.read(doc, "$.template")).isEqualTo("crosswalk");

        doc = getDocument("template", "regex", regex[3]);
        assertThat(doc).isNotNull();
        assertThat((String) JsonPath.read(doc, "$.template")).isEqualTo("crosswalk");

        logger.info("Preparing request: nameUpper regex " + regex[4]);
        Filter filter = new Filter("nameUpper", "regex", regex[4]);
        filter.setModificators(Arrays.asList("i"));
        FindAllRequest request = new FindAllRequest()
            .setName("doc")
            .addFilter(filter);
        doc = dataProvider.find(request).get(0);
        assertThat(doc).isNotNull();
        assertThat((String) JsonPath.read(doc, "$.nameUpper")).contains("ХРУСТАЛЬНОГО");
    }

    @Test
    public void andFilter() {
        LinkedHashMap<String, Object> firstFilter = new LinkedHashMap<>();
        firstFilter.put("paramName", "template");
        firstFilter.put("operator", "equal");
        firstFilter.put("value", "crosswalk");

        LinkedHashMap<String, Object> secondFilter = new LinkedHashMap<>();
        secondFilter.put("paramName", "data.ext_id");
        secondFilter.put("operator", "equal");
        secondFilter.put("value", "185");

        Filter filter = new Filter("and", Arrays.asList(
            firstFilter,
            secondFilter
        ));

        FindAllRequest request = new FindAllRequest()
            .setName("doc")
            .addFilter(filter);
        Document doc = dataProvider.find(request).get(0);
        assertThat(doc).isNotNull();
        assertThat((String) JsonPath.read(doc, "$.data.ext_id")).isEqualTo("185");
    }

    @Test
    public void orFilter() {
        LinkedHashMap<String, Object> firstFilter = new LinkedHashMap<>();
        firstFilter.put("paramName", "data.ext_id");
        firstFilter.put("operator", "equal");
        firstFilter.put("value", "185");

        LinkedHashMap<String, Object> secondFilter = new LinkedHashMap<>();
        secondFilter.put("paramName", "data.ext_id");
        secondFilter.put("operator", "equal");
        secondFilter.put("value", "186");

        Filter filter = new Filter("or", Arrays.asList(
            firstFilter,
            secondFilter
        ));
        FindAllRequest request = new FindAllRequest()
            .setName("doc")
            .addFilter(filter);
        Document doc = dataProvider.find(request).get(0);
        assertThat(doc).isNotNull();
        assertThat((String) JsonPath.read(doc, "$.data.ext_id")).isIn("185", "186");
    }

    @Test
    public void existsFilter() {
        Document doc = getDocument("data.amountCross", "exists", true);
        assertThat(doc).isNotNull();
        assertThat((Document) JsonPath.read(doc, "$.data")).containsKey("amountCross");

        doc = getDocument("data.amountCross", "exists", false);
        assertThat(doc).isNotNull();
        assertThat((Document) JsonPath.read(doc, "$.data")).doesNotContainKey("amountCross");
    }

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedFilter() {
        Document doc = getDocument("template", "someUnknownOp", "zxc");
    }

    @Test
    public void limit() {
        FindAllRequest request = new FindAllRequest()
            .setName("doc")
            .addFilter(
                "template",
                "equal",
                "crosswalk"
            )
            .setLimit(2);
        List<Document> docs = dataProvider.find(request);

        assertThat(docs).isNotNull();
        assertThat(docs.size()).isEqualTo(2);
    }

    @Test
    public void sortAsc() {
        FindAllRequest request = new FindAllRequest()
            .setName("doc")
            .addFilter(
                "template",
                "equal",
                "crosswalk"
            )
            .addSort("uptime", 1);
        List<Document> docs = dataProvider.find(request);

        assertThat(docs).isNotNull();

        Long first = JsonPath.read(docs.get(0), "$.uptime");
        Long second = JsonPath.read(docs.get(1), "$.uptime");
        logger.info("first=" + first + ", second=" + second + ", delta=" + (first - second));

        assertThat(first).isLessThanOrEqualTo(second);
    }

    @Test
    public void sortDesc() {
        FindAllRequest request = new FindAllRequest()
            .setName("doc")
            .addFilter(
                "template",
                "equal",
                "crosswalk"
            )
            .addSort("data.ext_id", -1);
        List<Document> docs = dataProvider.find(request);

        assertThat(docs).isNotNull();

        Long first = Long.parseLong(JsonPath.read(docs.get(0), "$.data.ext_id"));
        Long second = Long.parseLong(JsonPath.read(docs.get(1), "$.data.ext_id"));
        logger.info("first=" + first + ", second=" + second + ", delta=" + (first - second));

        assertThat(first).isGreaterThanOrEqualTo(second);
    }

    @Test
    public void projection() {
        FindAllRequest request = new FindAllRequest()
            .setName("doc")
            .addFilter(
                "template",
                "equal",
                "crosswalk"
            )
            .setFields(Arrays.asList("template", "ext_id", "uptime"));
        Document doc = dataProvider.find(request).get(0);

        assertThat(doc).isNotNull();
        assertThat(doc).containsKeys("template", "ext_id", "uptime");
        assertThat(doc).doesNotContainKeys("id", "data");
    }

    private void checkEqualFilter(String fieldName, String fieldValue, String jsonPath) {
        logger.info("Test 'equalFilter': " + fieldName + " equal " + fieldValue);

        Document doc = getDocument(fieldName, "equal", fieldValue);

        assertNotNull("Document not found " + fieldName + "=" + fieldValue, doc);
        assertEquals(fieldValue, JsonPath.read(doc, jsonPath));
    }

    private Document getDocument(String fieldName, String operator, Object fieldValue) {
        logger.info("Preparing request: " + fieldName + " " + operator + " " + fieldValue);
        FindAllRequest request = new FindAllRequest()
            .setName("doc")
            .addFilter(
                fieldName,
                operator,
                fieldValue
            );
        return dataProvider.find(request).get(0);
    }

}