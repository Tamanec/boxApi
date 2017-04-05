package ru.altarix.marm.queryLanguage.dataProvider.sql;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;
import ru.altarix.marm.utils.MarmTestWatcher;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FindAllReferencesTest {

    @Autowired
    private SqlDataProvider dataProvider;

    private Logger logger;

    @Rule
    public TestWatcher watcher = new MarmTestWatcher(this.getClass().getCanonicalName());

    public FindAllReferencesTest() throws Exception {
        logger = LogManager.getLogger(this.getClass().getCanonicalName());
    }

    @Test
    public void noFilters() {
        logger.info("Test noFilters");

        FindAllRequest request = new FindAllRequest()
            .setName("orderType");
        List<Map<String, Object>> refList = dataProvider.find(request);

        assertThat(refList).isNotNull();
    }

    @Test
    public void equalFilter() {
        logger.info("Test equalFilter");

        FindAllRequest request = new FindAllRequest()
            .setName("orderType")
            .addFilter(
                "id",
                "equal",
                31
            );
        List<Map<String, Object>> refList = dataProvider.find(request);

        assertThat(refList).isNotNull();
        assertThat(refList.size()).isEqualTo(1);
        assertThat(refList.get(0).get("id")).isEqualTo(31);
    }

}