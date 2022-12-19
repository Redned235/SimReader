package me.redned.simreader;

import me.redned.simreader.sc4.storage.SC4File;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        SC4File file = new SC4File(Paths.get("cities/city.sc4"));
        try {
            file.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
