package me.redned.simreader.storage.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.redned.simreader.storage.FileBuffer;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SaveGameProperty {
    private final int propertyNameValue;
    private final int propertyNameValueCopy;
    private final int unknown1;
    private final byte valueType;
    private final byte keyType;
    private final short unknown2;
    private final int dataRepeatedCount;
    private final List<Object> data;

    public static SaveGameProperty parse(FileBuffer buffer) {
        int propertyNameValue = buffer.readUInt32();
        int propertyNameValueCopy = buffer.readUInt32();
        int unknown1 = buffer.readUInt32();
        byte valueType = buffer.readByte();
        byte keyType = buffer.readByte();
        short unknown2 = buffer.readUInt16();

        int dataRepeatedCount = 0;
        List<Object> data = new ArrayList<>();
        if (keyType == Byte.MIN_VALUE) { // technically 0x80 but overflows in Java to Byte.MIN_VALUE
            dataRepeatedCount = buffer.readUInt32();
            for (int i = 0; i < dataRepeatedCount; i++) {
                buffer.readValueType(data, valueType);
            }
        } else {
            buffer.readValueType(data, valueType);
        }

        return new SaveGameProperty(
                propertyNameValue,
                propertyNameValueCopy,
                unknown1,
                valueType,
                keyType,
                unknown2,
                dataRepeatedCount,
                data
        );
    }

    public static List<SaveGameProperty> parseAll(FileBuffer buffer, int count) {
        List<SaveGameProperty> properties = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            SaveGameProperty property = SaveGameProperty.parse(buffer);
            properties.add(property);
        }

        return properties;
    }
}
