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

    public static Operator findByName(String name) throws IllegalArgumentException {
        if (dictionary.isEmpty()) {
            for (Operator operator : Operator.values()) {
                dictionary.put(operator.getName(), operator);
            }
        }

        if (!dictionary.containsKey(name)) {
            throw new IllegalArgumentException("Unknown operator name: " + name);
        }

        return dictionary.get(name);
    }

    public String getName() {
        return name;
    }

}