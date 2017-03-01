package ru.altarix.marm.api;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.altarix.marm.queryLanguage.response.BaseResponse;
import ru.altarix.marm.queryLanguage.dataProvider.Mongo;
import ru.altarix.marm.queryLanguage.request.FindAllRequest;

import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

@RestController
public class DataController {

    private MongoClient mongo;

    @Autowired
    public DataController(MongoClient mongo) {
        this.mongo = mongo;
    }

    @RequestMapping("/data/findAll")
    public BaseResponse find(@RequestBody FindAllRequest request) {
        MongoDatabase db = mongo.getDatabase("nadzor");
        Mongo dataProvider = new Mongo(db);

        Document doc = dataProvider.find(request);

        List<Object> data = new LinkedList<>();
        data.add(doc);

        return new BaseResponse(data);
    }
}
