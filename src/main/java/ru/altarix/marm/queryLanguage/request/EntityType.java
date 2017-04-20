package ru.altarix.marm.queryLanguage.request;

import java.util.HashMap;

public enum EntityType {

    DOC("doc"),
    TEMPLATE("template"),
    VIEW("view"),
    STYLE("style"),
    RULE("rule"),
    SCRIPT("script");

    private String name;

    protected static HashMap<String, EntityType> dictionary = new HashMap<>();

    EntityType(String name) {
        this.name = name;
    }

    public static EntityType findByName(String name) {
        if (dictionary.isEmpty()) {
            for (EntityType operator : EntityType.values()) {
                dictionary.put(operator.getName(), operator);
            }
        }

        if (!dictionary.containsKey(name)) {
            return null;
        }

        return dictionary.get(name);
    }

    public String getName() {
        return name;
    }

}
