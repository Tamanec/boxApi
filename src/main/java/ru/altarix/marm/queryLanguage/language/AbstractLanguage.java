package ru.altarix.marm.queryLanguage.language;

import ru.altarix.marm.queryLanguage.dialect.Dialect;

abstract public class AbstractLanguage implements Language {

    protected Dialect findDialect;

    public AbstractLanguage(Dialect findDialect) {
        this.findDialect = findDialect;
    }

    /*protected Dialect insertDialect;

    protected Dialect updateDialect;

    protected Dialect deleteDialect;*/

}
