package ru.altarix.marm.queryLanguage.request;

import java.util.HashMap;

public enum EntityType {

    DOC("docs"),
    TEMPLATE("templates"),
    VIEW("views"),
    STYLE("styles"),
    RULE("rules"),
    SCRIPT("scripts");

    private String name;

    protected static HashMap<String, EntityType> dictionary = new HashMap<>();

    EntityType(String name) {
        this.name = name;
    }

    public static EntityType findByName(String name) throws IllegalArgumentException {
        if (dictionary.isEmpty()) {
            for (EntityType operator : EntityType.values()) {
                dictionary.put(operator.getName(), operator);
            }
        }

        if (!dictionary.containsKey(name)) {
            throw new IllegalArgumentException("Unknown entityType name: " + name);
        }

        return dictionary.get(name);
    }

    public String getName() {
        return name;
    }

}
