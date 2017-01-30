package ru.altarix.marm.utils;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.sql.Timestamp;

public class TimestampJsonSerializer extends StdSerializer<Timestamp> {

    public TimestampJsonSerializer() {
        this(null);
    }

    public TimestampJsonSerializer(Class<Timestamp> t) {
        super(t);
    }

    @Override
    public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // remove milliseconds
        gen.writeNumber(value.getTime() / 1000);
    }
}
