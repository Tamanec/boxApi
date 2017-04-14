package ru.altarix.marm.queryLanguage.dataProvider.sql;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;
import ru.altarix.marm.queryLanguage.request.body.Filter;
import ru.altarix.marm.utils.MarmTestWatcher;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FindAllReferencesTest {

    @Autowired
    private SqlDAO dataProvider;

    private Logger logger;

    private FindAllRequest request;

    @Rule
    public TestWatcher watcher = new MarmTestWatcher(this.getClass().getCanonicalName());

    public FindAllReferencesTest() throws Exception {
        logger = LogManager.getLogger(this.getClass().getCanonicalName());
    }

    @Before
    public void initTest() {
        request = new FindAllRequest()
            .setName("orderType");
    }

    @Test
    public void noFilters() {
        List<Map<String, Object>> refList = dataProvider.find(request);
        assertThat(refList).isNotNull();
    }

    @Test
    public void equalFilter() {
        request.addFilter(
                "id",
                "equal",
                31
            );
        List<Map<String, Object>> refList = dataProvider.find(request);

        assertThat(refList).isNotNull();
        assertThat(refList.size()).isEqualTo(1);
        assertThat(refList.get(0).get("id")).isEqualTo(31);
    }

    @Test
    public void twoFilters() {
        request.addFilter(
                "id",
                "equal",
                31
            )
            .addFilter(
                "status",
                "equal",
                "actual"
            );
        List<Map<String, Object>> refList = dataProvider.find(request);
        assertThat(refList).isNotNull();
        assertThat(refList.size()).isEqualTo(1);
        assertThat(refList.get(0).get("id")).isEqualTo(31);
        assertThat(refList.get(0).get("status")).isEqualTo("actual");
    }

    @Test
    public void notEqualFilter() {
        FindAllRequest request = new FindAllRequest()
            .setName("orderType");
    }

    @Test
    public void andFilter() {
        LinkedHashMap<String, Object> firstFilter = new LinkedHashMap<>();
        firstFilter.put("paramName", "id");
        firstFilter.put("operator", "equal");
        firstFilter.put("value", 31);

        LinkedHashMap<String, Object> secondFilter = new LinkedHashMap<>();
        secondFilter.put("paramName", "status");
        secondFilter.put("operator", "equal");
        secondFilter.put("value", "actual");

        Filter filter = new Filter("and", Arrays.asList(
            firstFilter,
            secondFilter
        ));

        request.addFilter("ext_id", "equal", 313).addFilter(filter);
        List<Map<String, Object>> refList = dataProvider.find(request);
        assertThat(refList).isNotNull();
        assertThat(refList.size()).isEqualTo(1);
        assertThat(refList.get(0).get("id")).isEqualTo(31);
        assertThat(refList.get(0).get("status")).isEqualTo("actual");
        assertThat(refList.get(0).get("ext_id")).isEqualTo(313);
    }

}