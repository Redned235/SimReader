package me.redned.simreader.sc4.storage.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.redned.simreader.sc4.type.Prop;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.util.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PropSubfile {

    private final List<Prop> props;

    public static PropSubfile parse(byte[] bytes, int size) {
        List<Prop> props = new ArrayList<>();

        int offset = 0;
        while (size > 0) {
            int currentSize = Utils.readUInt32(offset, bytes);

            byte[] propBytes = new byte[currentSize];
            System.arraycopy(bytes, offset, propBytes, 0, currentSize);

            Prop prop = Prop.parse(new FileBuffer(propBytes, offset));
            props.add(prop);

            offset += currentSize;
            size -= currentSize;
        }

        if (size != 0) {
            System.err.println("Did not read all props! Had " + size + " remaining bytes!");
        }

        return new PropSubfile(props);
    }
}
