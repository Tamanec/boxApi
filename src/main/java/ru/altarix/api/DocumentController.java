package ru.altarix.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.altarix.domain.Document;
import ru.altarix.repository.DocumentRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DocumentController {

    private DocumentRepository docsRepo;

    @Autowired
    public DocumentController(DocumentRepository docsRepo) {
        this.docsRepo = docsRepo;
    }

    @RequestMapping("/test")
    public List<Document> test() {
        List<Document> documents = new ArrayList<>();
        for (Document document : docsRepo.findAll()) {
            documents.add(document);
        }

        return documents;
    }
}
