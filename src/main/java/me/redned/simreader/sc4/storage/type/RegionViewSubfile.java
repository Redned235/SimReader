package me.redned.simreader.sc4.storage.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.redned.simreader.storage.FileBuffer;

@ToString
@Getter
@RequiredArgsConstructor
public class RegionViewSubfile {
    private final short majorVersion;
    private final short minorVersion;
    private final int tileXLocation;
    private final int tileYLocation;
    private final int citySizeX;
    private final int citySizeY;
    private final int residentialPopulation;
    private final int commercialPopulation;
    private final int industrialPopulation;
    private final byte mayorRating;
    private final byte starCount;
    private final byte tutorialFlag;
    private final int cityGuid;
    private final byte modeFlag;
    private final String cityName;
    private final String formerCityName;
    private final String mayorName;
    private final String internalDescription;

    public static RegionViewSubfile parse(FileBuffer buffer) {
        short majorVersion = buffer.readUInt16();
        short minorVersion = buffer.readUInt16();

        if (minorVersion < 13) {
            System.err.println("Parsing a pre Rush-Hour game. Some of this data may not properly parse!");
        }

        int tileXLocation = buffer.readUInt32();
        int tileYLocation = buffer.readUInt32();
        int citySizeX = buffer.readUInt32() * 64;
        int citySizeY = buffer.readUInt32() * 64;
        int residentialPopulation = buffer.readUInt32();
        int commercialPopulation = buffer.readUInt32();
        int industrialPopulation = buffer.readUInt32();

        if (minorVersion > 9) {
            buffer.offset(4);
        }

        byte mayorRating = 0;
        if (minorVersion > 10) {
            mayorRating = buffer.readByte();
        }

        byte startCount = buffer.readByte();
        byte tutorialFlag = buffer.readByte();
        int cityGuid = buffer.readUInt32();

        buffer.offset(4 * 5); // Skip over unknown fields

        byte modeFlag = buffer.readByte();
        int cityNameLength = buffer.readUInt32();
        String cityName = buffer.readString(cityNameLength);

        int formerCityNameLength = buffer.readUInt32();
        String formerCityName = buffer.readString(formerCityNameLength);

        int mayorNameLength = buffer.readUInt32();
        String mayorName = buffer.readString(mayorNameLength);

        int internalDescriptionLength = buffer.readUInt32();
        String internalDescriptionName = buffer.readString(internalDescriptionLength);

        return new RegionViewSubfile(
                majorVersion,
                minorVersion,
                tileXLocation,
                tileYLocation,
                citySizeX,
                citySizeY,
                residentialPopulation,
                commercialPopulation,
                industrialPopulation,
                mayorRating,
                startCount,
                tutorialFlag,
                cityGuid,
                modeFlag,
                cityName,
                formerCityName,
                mayorName,
                internalDescriptionName
        );
    }
}
