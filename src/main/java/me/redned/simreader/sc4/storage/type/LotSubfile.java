package me.redned.simreader.sc4.storage.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.redned.simreader.util.Utils;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.sc4.type.Lot;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class LotSubfile {

    private final List<Lot> lots;

    public static LotSubfile parse(byte[] bytes, int size) {
        List<Lot> lots = new ArrayList<>();

        int offset = 0;
        while (size > 0) {
            int currentSize = Utils.readUInt32(offset, bytes);

            byte[] lotBytes = new byte[currentSize];
            System.arraycopy(bytes, offset, lotBytes, 0, currentSize);

            Lot lot = Lot.parse(new FileBuffer(lotBytes, offset));
            lots.add(lot);

            offset += currentSize;
            size -= currentSize;
        }

        if (size != 0) {
            System.err.println("Did not read all lots! Had " + size + " remaining bytes!");
        }

        return new LotSubfile(lots);
    }
}
