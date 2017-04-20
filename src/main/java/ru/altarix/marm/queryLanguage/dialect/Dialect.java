package ru.altarix.marm.queryLanguage.dialect;

import ru.altarix.marm.queryLanguage.query.Query;
import ru.altarix.marm.queryLanguage.request.FindRequest;

public interface Dialect<T> {

    public T getQuery(FindRequest request);

}
