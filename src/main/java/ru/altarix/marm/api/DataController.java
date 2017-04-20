package ru.altarix.marm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.altarix.marm.queryLanguage.request.FindRequest;
import ru.altarix.marm.queryLanguage.response.BaseResponse;
import ru.altarix.marm.queryLanguage.service.CrudService;
import ru.altarix.marm.queryLanguage.service.factory.CrudServiceFactory;

import java.util.List;
import java.util.Map;

@RestController
public class DataController {

    @Autowired
    private CrudServiceFactory crudServiceFactory;

    @RequestMapping("/data/findAll")
    public BaseResponse find(@RequestBody FindRequest request) {
        CrudService crudService = crudServiceFactory.create(request);
        List<Map<String, Object>> docs = crudService.find(request);

        return new BaseResponse().addDocuments(docs);
    }
}
