package ru.altarix.marm.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper jsonMapper;

    @Test
    public void dataFindEqualByExtIdAndTempate() throws Exception {
        // Готовим запрос
        FindAllRequest request = new FindAllRequest();
        request.setName("doc");

        // Готовим фильтр
        Map<String, Object> extIdFilter = new HashMap<>();
        extIdFilter.put("paramName", "ext_id");
        extIdFilter.put("operator", "equal");
        extIdFilter.put("value", "185");

        List<Map<String, Object>> filters = new LinkedList<>();
        filters.add(extIdFilter);

        request.setFilters(filters);

        String requestJson = jsonMapper.writeValueAsString(request);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> httpRequest = new HttpEntity<>(requestJson, headers);

        String response = restTemplate.postForObject(
                "http://localhost:" + port + "/data/findAll",
                httpRequest,
                String.class
        );
        assertThat(response).contains("185");
        System.out.println("RESPONSE: " + response.substring(0, 60));


    }

}
