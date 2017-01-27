package ru.altarix.repository;

import org.springframework.data.repository.CrudRepository;
import ru.altarix.domain.Document;

public interface DocumentRepository extends CrudRepository<Document, Long> {
}
