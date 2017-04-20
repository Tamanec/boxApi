package ru.altarix.marm.queryLanguage.service;

import ru.altarix.marm.queryLanguage.dialect.Dialect;
import ru.altarix.marm.queryLanguage.query.Query;

abstract public class AbstractCrudService<T> implements CrudService<T> {

    protected Dialect<? extends Query> dialect;

    public AbstractCrudService(Dialect dialect) {
        this.dialect = dialect;
    }
}
