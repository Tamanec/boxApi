package ru.altarix.marm.queryLanguage.dialect.sql;

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
import ru.altarix.marm.queryLanguage.request.FindRequest;
import ru.altarix.marm.queryLanguage.request.body.Filter;
import ru.altarix.marm.queryLanguage.service.CrudService;
import ru.altarix.marm.queryLanguage.service.factory.CrudServiceFactory;
import ru.altarix.marm.queryLanguage.service.sql.SqlCrudService;
import ru.altarix.marm.utils.MarmTestWatcher;

import java.util.*;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FindAllReferencesTest {

    @Autowired
    private CrudServiceFactory crudServiceFactory;

    private CrudService<Map<String, Object>> crudService;

    private Logger logger;

    private FindRequest request;

    @Rule
    public TestWatcher watcher = new MarmTestWatcher(this.getClass().getCanonicalName());

    public FindAllReferencesTest() throws Exception {

    }

    @Before
    public void initTest() {
        logger = LogManager.getLogger(this.getClass().getCanonicalName());

        request = new FindRequest().setName("orderType");
        crudService = crudServiceFactory.create(request);
    }

    @Test
    public void noFilters() {
        List<Map<String, Object>> refList = crudService.find(request);
        assertThat(refList).isNotNull();
    }

    @Test
    public void equalFilter() {
        request.addFilter("id", "equal", 31);
        List<Map<String, Object>> refList = crudService.find(request);

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
        List<Map<String, Object>> refList = crudService.find(request);
        assertThat(refList).isNotNull();
        assertThat(refList.size()).isEqualTo(1);
        assertThat(refList.get(0).get("id")).isEqualTo(31);
        assertThat(refList.get(0).get("status")).isEqualTo("actual");
    }

    @Test
    public void notEqualFilter() {
        request.addFilter("id", "ne", 31);
        Map<String, Object> ref = crudService.find(request).get(0);

        assertThat(ref).isNotNull();
        assertThat(ref.get("id")).isNotEqualTo(31);
    }

    @Test
    public void inFilter() {
        request.addFilter(
            "id",
            "in",
            Arrays.asList(30, 31)
        );

        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        assertThat(refList.size()).isEqualTo(2);
        assertThat(refList.get(0).get("id")).isIn(30, 31);
        assertThat(refList.get(1).get("id")).isIn(30, 31);
    }

    @Test
    public void notIn() {
        request.addFilter(
            "id",
            "nin",
            Arrays.asList(30, 31)
        );

        List<Map<String, Object>> refList = crudService.find(request);
        assertThat(refList).isNotNull();

        final List<Integer> ids = new LinkedList<>();
        refList.forEach(ref -> {
            ids.add((Integer) ref.get("id"));
        });

        assertThat(ids).doesNotContain(30, 31);
    }

    @Test
    public void greaterThenFilter() {
        request.addFilter("id", "gt", 30);
        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        assertThat((Integer) refList.get(0).get("id")).isGreaterThan(30);
    }

    @Test
    public void greaterThenEqualFilter() {
        request.addFilter("id", "gte", 31);
        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        assertThat((Integer) refList.get(0).get("id")).isGreaterThanOrEqualTo(31);
    }

    @Test
    public void lessThenFilter() {
        request.addFilter("id", "lt", 2);
        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        assertThat((Integer) refList.get(0).get("id")).isLessThan(2);
    }

    @Test
    public void lessThenEqualFilter() {
        request.addFilter("id", "lte", 1);
        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        assertThat((Integer) refList.get(0).get("id")).isLessThanOrEqualTo(1);
    }

    @Test
    public void regexFilter() {
        String[][] regex = {
            {"name", ".*Ремонт дорог.*"},
            {"name", ".*ремонт дорог.*"},
            {"status", ".*ctua.*"},
            {"status", "actu.*"},
            {"status", ".*tual"}
        };

        checkRegex(regex[0][0], regex[0][1]);

        Filter ignoreCaseFilter = new Filter(
            regex[1][0],
            "regex",
            regex[1][1]);
        ignoreCaseFilter.setModificators(Arrays.asList("i"));

        request.addFilter(ignoreCaseFilter);
        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        assertThat((String) refList.get(0).get(regex[1][0]))
            .containsPattern(Pattern.compile(regex[1][1], Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));

        checkRegex(regex[2][0], regex[2][1]);
        checkRegex(regex[3][0], regex[3][1]);
        checkRegex(regex[4][0], regex[4][1]);
    }

    private void checkRegex(String field, String regex) {
        FindRequest request = new FindRequest()
            .setName("orderType")
            .addFilter(field, "regex", regex);
        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        assertThat((String) refList.get(0).get(field)).containsPattern(regex);
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
        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        assertThat(refList.size()).isEqualTo(1);
        assertThat(refList.get(0).get("id")).isEqualTo(31);
        assertThat(refList.get(0).get("status")).isEqualTo("actual");
        assertThat(refList.get(0).get("ext_id")).isEqualTo(313);
    }

    @Test
    public void orFilter() {
        LinkedHashMap<String, Object> firstFilter = new LinkedHashMap<>();
        firstFilter.put("paramName", "id");
        firstFilter.put("operator", "equal");
        firstFilter.put("value", 30);

        LinkedHashMap<String, Object> secondFilter = new LinkedHashMap<>();
        secondFilter.put("paramName", "id");
        secondFilter.put("operator", "equal");
        secondFilter.put("value", 31);

        Filter filter = new Filter("or", Arrays.asList(
            firstFilter,
            secondFilter
        ));

        request.addFilter(filter);
        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        assertThat(refList.size()).isEqualTo(2);
        assertThat(refList.get(0).get("id")).isIn(30, 31);
        assertThat(refList.get(1).get("id")).isIn(30, 31);
    }

    @Test
    public void existsFilter() {
        request.addFilter("ext_id", "exists", true);
        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        refList.forEach(element -> assertThat(element.get("ext_id")).isNotNull());
    }

    @Test
    public void sortAsc() {
        request.addSort("ext_id", 1);
        List<Map<String, Object>> refList = crudService.find(request);

        Map<String, Object> prevElement = refList.get(0);
        for (Map<String, Object> element : refList) {
            assertThat((Integer) element.get("ext_id"))
                .isGreaterThanOrEqualTo((Integer) prevElement.get("ext_id"));
            prevElement = element;
        }
    }

    @Test
    public void sortDesc() {
        request.addSort("ext_id", -1);
        List<Map<String, Object>> refList = crudService.find(request);

        Map<String, Object> prevElement = refList.get(0);
        for (Map<String, Object> element : refList) {
            assertThat((Integer) element.get("ext_id"))
                .isLessThanOrEqualTo((Integer) prevElement.get("ext_id"));
            prevElement = element;
        }
    }

    @Test
    public void projection() {
        request.setFields(Arrays.asList("ext_id", "name", "uptime"));
        Map<String, Object> ref = crudService.find(request).get(0);

        assertThat(ref).isNotNull();
        assertThat(ref).containsKeys("ext_id", "name", "uptime");
        assertThat(ref).doesNotContainKeys("id", "status", "ext_parent_id");
    }

    @Test
    public void limit() {
        request.setLimit(10);
        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        assertThat(refList.size()).isEqualTo(10);
    }

    @Test
    public void offset() {
        request.addSort("id", 1).setOffset(10);
        List<Map<String, Object>> refList = crudService.find(request);

        assertThat(refList).isNotNull();
        refList.forEach(element -> assertThat((Integer) element.get("id")).isGreaterThan(10));
    }

}