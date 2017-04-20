package ru.altarix.marm.queryLanguage.query;

import java.util.List;

public interface Query<T> {

    public List<T> execute();

}
