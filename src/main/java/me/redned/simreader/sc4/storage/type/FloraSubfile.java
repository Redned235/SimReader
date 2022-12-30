package me.redned.simreader.sc4.storage.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.redned.simreader.sc4.type.Flora;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.util.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class FloraSubfile {

    private final List<Flora> flora;

    public static FloraSubfile parse(byte[] bytes, int size) {
        List<Flora> floras = new ArrayList<>();

        int offset = 0;
        while (size > 0) {
            int currentSize = Utils.readUInt32(offset, bytes);

            byte[] floraBytes = new byte[currentSize];
            System.arraycopy(bytes, offset, floraBytes, 0, currentSize);

            Flora flora = Flora.parse(new FileBuffer(floraBytes, offset));
            floras.add(flora);

            offset += currentSize;
            size -= currentSize;
        }

        if (size != 0) {
            System.err.println("Did not read all floras! Had " + size + " remaining bytes!");
        }

        return new FloraSubfile(floras);
    }
}
