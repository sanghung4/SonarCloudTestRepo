package com.reece.platform.products.utilities;

import java.beans.PropertyEditorSupport;
import lombok.val;

public class CaseInsensitiveEnumMapper extends PropertyEditorSupport {

    @SuppressWarnings("rawtypes")
    private final Class<? extends Enum> enumType;

    private final String[] enumNames;

    public CaseInsensitiveEnumMapper(Class<?> type) {
        enumType = type.asSubclass(Enum.class);
        val values = type.getEnumConstants();
        if (values == null) {
            throw new IllegalArgumentException("Unsupported " + type);
        }

        enumNames = new String[values.length];

        for (var i = 0; i < values.length; i++) {
            enumNames[i] = ((Enum<?>) values[i]).name();
        }
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.isEmpty() || "undefined".equals(text)) {
            setValue(null);
            return;
        }

        for (val name : enumNames) {
            if (name.equalsIgnoreCase(text)) {
                @SuppressWarnings("unchecked")
                val newValue = Enum.valueOf(enumType, name);
                setValue(newValue);
                return;
            }
        }

        throw new IllegalArgumentException(
            "No enum constant " + enumType.getCanonicalName() + " equals ignore case " + text
        );
    }
}
