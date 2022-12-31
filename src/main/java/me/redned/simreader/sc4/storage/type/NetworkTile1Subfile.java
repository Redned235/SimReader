package me.redned.simreader.sc4.storage.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.redned.simreader.sc4.type.NetworkTile1;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.util.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class NetworkTile1Subfile {

    private final List<NetworkTile1> networkTiles;

    public static NetworkTile1Subfile parse(byte[] bytes, int size) {
        List<NetworkTile1> networkTiles = new ArrayList<>();

        int offset = 0;
        while (size > 0) {
            int currentSize = Utils.readUInt32(offset, bytes);

            byte[] networkTileBytes = new byte[currentSize];
            System.arraycopy(bytes, offset, networkTileBytes, 0, currentSize);

            NetworkTile1 networkTile = NetworkTile1.parse(new FileBuffer(networkTileBytes, offset));
            networkTiles.add(networkTile);

            offset += currentSize;
            size -= currentSize;
        }

        if (size != 0) {
            System.err.println("Did not read all network tiles! Had " + size + " remaining bytes!");
        }

        return new NetworkTile1Subfile(networkTiles);
    }
}
