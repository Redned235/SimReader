package me.redned.simreader;

import me.redned.simreader.sc4.storage.SC4File;
import me.redned.simreader.sc4.storage.exemplar.ExemplarFile;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        try {
            SC4File file = new SC4File(Paths.get("cities/city.sc4"));
            file.read();

            ExemplarFile exemplar = new ExemplarFile(Paths.get("exemplar/SimCity_1.dat"));
            exemplar.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
