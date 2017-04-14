package ru.altarix.marm.api;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.altarix.marm.queryLanguage.dataProvider.mongo.MongoDAO;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;
import ru.altarix.marm.queryLanguage.response.BaseResponse;

import java.util.List;

@RestController
public class DataController {

    private MongoDAO dataProvider;

    @Autowired
    public DataController(MongoDAO dataProvider) {
        this.dataProvider = dataProvider;
    }

    @RequestMapping("/data/findAll")
    public BaseResponse find(@RequestBody FindAllRequest request) {
        List<Document> docs = dataProvider.find(request);

        return new BaseResponse().addAllDocuments(docs);
    }
}
