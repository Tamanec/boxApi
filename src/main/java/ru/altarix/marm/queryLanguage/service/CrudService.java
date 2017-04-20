package ru.altarix.marm.queryLanguage.service;

import ru.altarix.marm.queryLanguage.request.FindRequest;

import java.util.List;

public interface CrudService<T> {

    public List<T> find(FindRequest request);

    //public T save(SaveRequest request);

    //public int delete(DeleteRequest request);

}
