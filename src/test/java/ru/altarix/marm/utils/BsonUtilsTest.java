package ru.altarix.marm.utils;

import org.apache.log4j.LogManager;
import org.bson.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class BsonUtilsTest {

    private String fieldName;
    private String expectedValue;
    private Document doc;

    @Rule
    public TestWatcher watcher = new MarmTestWatcher(this.getClass().getCanonicalName());

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"template", "crosswalk"},
                {"data.ext_id", "185"},
                {"data.characteristics.0.name", "marking"}
            }
        );
    }

    public BsonUtilsTest(String fieldName, String expectedValue) {
        this.fieldName = fieldName;
        this.expectedValue = expectedValue;
    }

    @Before
    public void init() {
        LogManager.getLogger(this.getClass().getCanonicalName()).info(
            "fieldName=" + fieldName +
            ", expectedValue=" + expectedValue
        );

        doc = new Document()
            .append("template", "crosswalk")
            .append("data", new Document()
                .append("ext_id", "185")
                .append("characteristics", Arrays.asList(
                    new Document("name", "marking"),
                    new Document("name", "unmarking")
                ))
            );
    }

    @Test
    public void getValue() throws Exception {
        String result = BsonUtils.getValue(doc, fieldName, String.class);

        assertNotNull(result);
        assertEquals(expectedValue, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getValueWrongField() throws Exception {
        BsonUtils.getValue(doc, "data.ext_id.test", String.class);
    }

}