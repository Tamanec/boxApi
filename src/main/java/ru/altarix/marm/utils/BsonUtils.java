package ru.altarix.marm.utils;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.List;

public class BsonUtils {

    public static <T> T getValue(Document doc, String field, Class<T> clazz) {
        if (field.indexOf('.') == -1) {
            return doc.get(field, clazz);
        }

        // Вложенные поля
        String[] fieldsChain = field.split("\\.");
        Object parentFieldValue;
        Object subFieldValue = doc;
        Class subFieldClass;

        for (int i = 0; i < fieldsChain.length; i++) {
            // Определяем тип вложенного поля
            boolean isLastSubField = (i == (fieldsChain.length - 1));
            if (isLastSubField) {
                subFieldClass = clazz;
            } else if (StringUtils.isNumeric(fieldsChain[i + 1])) {
                subFieldClass = List.class;
            } else {
                subFieldClass = Document.class;
            }

            // Получаем значение вложенного поля
            parentFieldValue = subFieldValue;
            String subFieldName = fieldsChain[i];
            if (parentFieldValue instanceof Document) {
                Document parentValue = (Document) parentFieldValue;
                subFieldValue = parentValue.get(subFieldName, subFieldClass);
            } else if (parentFieldValue instanceof List) {
                List parentValue = (List) parentFieldValue;
                subFieldValue = parentValue.get(Integer.parseInt(subFieldName));
            } else {
                throw new IllegalArgumentException(
                        "Invalid parent field type, name: " + fieldsChain[i - 1]
                        + ", value class: " + parentFieldValue.getClass()
                );
            }
        }

        return clazz.cast(subFieldValue);
    }

}
