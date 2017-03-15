package ru.altarix.marm.queryLanguage.request.body;

import java.util.HashMap;

public enum Operator {

    EQUAL ("equal"),
    NOT_EQUAL ("ne"),
    IN("in"),
    NIN("nin"),
    GT("gt"),
    GTE("gte"),
    LT("lt"),
    LTE("lte"),
    REGEX("regex"),
    AND("and"),
    OR("or"),
    EXISTS("exists");

    private String name;

    protected static HashMap<String, Operator> dictionary = new HashMap<>();

    Operator(String name) {
        this.name = name;
    }

    public static Operator findByName(String name) {
        if (dictionary.isEmpty()) {
            for (Operator operator : Operator.values()) {
                dictionary.put(operator.getName(), operator);
            }
        }

        return dictionary.get(name);
    }

    public String getName() {
        return name;
    }

}