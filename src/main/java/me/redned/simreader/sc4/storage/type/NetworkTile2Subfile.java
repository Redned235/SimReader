package me.redned.simreader.sc4.storage.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.redned.simreader.sc4.storage.SC4File;
import me.redned.simreader.sc4.type.NetworkTile2;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.util.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class NetworkTile2Subfile {

    private final List<NetworkTile2> networkTiles;

    public static NetworkTile2Subfile parse(SC4File sc4File, byte[] bytes, int size) {
        List<NetworkTile2> networkTiles = new ArrayList<>();

        int offset = 0;
        while (size > 0) {
            int currentSize = Utils.readUInt32(offset, bytes);

            byte[] networkTileBytes = new byte[currentSize];
            System.arraycopy(bytes, offset, networkTileBytes, 0, currentSize);

            NetworkTile2 networkTile = NetworkTile2.parse(new FileBuffer(networkTileBytes, sc4File, offset));
            networkTiles.add(networkTile);

            offset += currentSize;
            size -= currentSize;
        }

        if (size != 0) {
            System.err.println("Did not read all network tiles! Had " + size + " remaining bytes!");
        }

        return new NetworkTile2Subfile(networkTiles);
    }
}
