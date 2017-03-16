package ru.altarix.marm.api;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.altarix.marm.queryLanguage.dataProvider.MongoDataProvider;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;
import ru.altarix.marm.queryLanguage.response.BaseResponse;

import java.util.LinkedList;
import java.util.List;

@RestController
public class DataController {

    private MongoDataProvider dataProvider;

    @Autowired
    public DataController(MongoDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @RequestMapping("/data/findAll")
    public BaseResponse find(@RequestBody FindAllRequest request) {
        List<Document> docs = dataProvider.find(request);

        List<Object> data = new LinkedList<>();
        if (docs != null) {
            data.addAll(docs);
        }

        return new BaseResponse(data);
    }
}
