package Øving8;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        String filePath = "src/Øving8/files/opg8-2021.pdf";
        String compressedPath = "src/Øving8/files/compressed.txt";

        byte[] compressed = compressDocument(filePath);
        System.out.println("de-komprimert lengde: " + compressed.length);

        LZ78 lz78 = new LZ78();

        lz78.deCompress(compressed, compressedPath);

    }

    public static byte[] compressDocument(String path) {
        LZ78 lz78 = new LZ78();
        byte[] compressed = lz78.compress(path);
        return compressed;
    }
}