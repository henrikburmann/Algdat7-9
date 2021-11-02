package Øving8;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LZ78 {

    /*
    1. gjør om en fil til bytes format
    2. tekstsøk (hvor langt bak? hvor lange ord?)
    3.
     */

    // Konverterer fil til bytes versjon
    static void readByt() {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("src/Øving8/files/opg8-2021.pdf"));
            String content = new String(bytes);
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) {
        //toBinary();
        readByt();

    }
}
