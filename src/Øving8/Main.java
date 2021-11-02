package Øving8;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        String filePath = "src/Øving8/files/opg8-2021.pdf";
        String compressedPath = "src/Øving8/files/compressed.pdf";

        compressDocument(filePath, compressedPath);


    }

    public static void compressDocument(String path, String outPath) {
        LZ78 lz78 = new LZ78();
        byte[] compressed = lz78.compress(path);



        System.out.println("Komprimert lengde: " + compressed.length);
        File file = new File("src/Øving8/files/opg8-2021.pdf");

    }
}