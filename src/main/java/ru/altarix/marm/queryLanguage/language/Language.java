package ru.altarix.marm.queryLanguage.language;

import ru.altarix.marm.queryLanguage.query.Query;
import ru.altarix.marm.queryLanguage.request.FindRequest;

public interface Language {

    public Query getFindQuery(FindRequest request);

    /*public T getInsertQuery(FindRequest request);

    public T getUpdateQuery(FindRequest request);

    public T getDeleteQuery(FindRequest request);*/

}
