package ru.altarix.marm.repository;

import org.springframework.data.repository.CrudRepository;
import ru.altarix.marm.domain.Document;

public interface DocumentRepository extends CrudRepository<Document, Long> {
}
