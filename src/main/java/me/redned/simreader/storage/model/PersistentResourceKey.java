package me.redned.simreader.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.redned.simreader.storage.FileBuffer;

@EqualsAndHashCode
@Data
@AllArgsConstructor
public class PersistentResourceKey {
    private int type;
    private int group;
    private int instance;

    @Override
    public String toString() {
        return "PersistentResourceKey{" +
                "type=0x" + Integer.toHexString(this.type) +
                ", group=0x" + Integer.toHexString(this.group) +
                ", instance=0x" + Integer.toHexString(this.instance) +
                '}';
    }

    public static PersistentResourceKey fromHex(String type, String group, String instance) {
        return new PersistentResourceKey((int) Long.parseLong(type, 16), (int) Long.parseLong(group, 16), (int) Long.parseLong(instance, 16));
    }

    public static PersistentResourceKey parse(FileBuffer buffer) {
        return new PersistentResourceKey(
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readUInt32()
        );
    }
}
