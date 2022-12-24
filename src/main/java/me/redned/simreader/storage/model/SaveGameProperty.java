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
    private final List<Object> data;

    public static SaveGameProperty parse(FileBuffer buffer) {
        int propertyNameValue = buffer.readUInt32();
        int propertyNameValueCopy = buffer.readUInt32();
        int unknown1 = buffer.readUInt32();
        byte valueType = buffer.readByte();
        byte keyType = buffer.readByte();
        short unknown2 = buffer.readUInt16();

        List<Object> data = new ArrayList<>();
        if ((keyType & 0xff) == 0x80) {
            int count = buffer.readUInt32();
            // String uses count to read string size
            if (valueType == 0x0C) {
                buffer.readValueType(data, valueType, count);
            } else {
                for (int i = 0; i < count; i++) {
                    buffer.readValueType(data, valueType);
                }
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
