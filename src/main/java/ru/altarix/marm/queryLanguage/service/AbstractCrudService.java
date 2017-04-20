package ru.altarix.marm.queryLanguage.service;

import ru.altarix.marm.queryLanguage.language.Language;

import java.util.Map;

abstract public class AbstractCrudService<T extends Map<String, Object>> implements CrudService<T> {

    protected Language language;

    public AbstractCrudService(Language language) {
        this.language = language;
    }
}
