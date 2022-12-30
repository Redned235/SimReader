# SimReader

A Java parser for game data from the Sim* Franchise.

At the moment, this project only supports SimCity 4, but since many other Maxis games also use a similar format, it could be expanded to support those games too.

**This is still very much a work in progress and unlikely to be actively maintained.** This is mainly just a fun project for myself, but contributions are certainly welcome!

## Usage
You can parse a .sc4 Savegame file by doing the following:
```java
SC4File file = new SC4File(Paths.get("my_city.sc4"));
```        

Often, SimCity 4 stores much of its data inside of Exemplar files, found inside the game directory (usually with file extension `.dat`). Lots of data stored inside Savegame files make references to this data in these Exemplars through it's `PersistentResourceKey`.

Here is an example that prints the name of each building:
```java
SC4File file = new SC4File(Paths.get("my_city.sc4"));
ExemplarFile exemplarFile = new ExemplarFile(Paths.get("exemplar/SimCity_1.dat"));

for (Building building : file.getBuildingFile().getBuildings()) {
    IndexEntry indexEntry = exemplarFile.getEntry(building.getResourceKey());
    byte[] bytes = exemplarFile.getBytesAtIndex(indexEntry);

    ExemplarSubfile exemplar = ExemplarSubfile.parse(ExemplarSubfile::new, new FileBuffer(bytes));
    System.out.println("Building name: " + exemplar.getProperty(ExemplarPropertyTypes.EXEMPLAR_NAME).getValue());
}
```

## Credits
Much of this project would not be possible without the following resources & repositories:
- [SC4Devotion Wiki](https://wiki.sc4devotion.com/)
- [SC4Mapper](https://github.com/wouanagaine/SC4Mapper-2013/)
- [SC4Parser](https://github.com/Killeroo/SC4Parser)