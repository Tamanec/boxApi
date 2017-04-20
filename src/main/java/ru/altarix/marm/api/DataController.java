package ru.altarix.marm.api;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.altarix.marm.queryLanguage.request.FindRequest;
import ru.altarix.marm.queryLanguage.response.BaseResponse;
import ru.altarix.marm.queryLanguage.service.mongo.DocsCrudService;

import java.util.List;

@RestController
public class DataController {

    private DocsCrudService dataProvider;

    @Autowired
    public DataController(DocsCrudService dataProvider) {
        this.dataProvider = dataProvider;
    }

    @RequestMapping("/data/findAll")
    public BaseResponse find(@RequestBody FindRequest request) {
        List<Document> docs = dataProvider.find(request);

        return new BaseResponse().addAllDocuments(docs);
    }
}
