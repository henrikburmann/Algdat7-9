import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        LZ77 lz77 = new LZ77();
        byte[] compressedLz77 = lz77.compress("src/Testfiles/Test");
        Files.write(Path.of("src/Testfiles/Lz77comp"), compressedLz77);
        HuffmanHenrik huffman = new HuffmanHenrik(new File("src/Testfiles" +
                "/Test"),
                new File("src" +
                "/Testfiles/Huffcomp"));
        //Huffman huffman = new Huffman();
        huffman.compress(compressedLz77, "src/Testfiles/Huffcomp");

        byte[] decompHuff = huffman.deCompress("src/Testfiles/HuffComp", "src" +
                "/Testfiles/HuffDecomp");
        //lz77.deCompress(decompHuff, "src/Testfiles/Ferdig");

        DocumentDecompressor d = new DocumentDecompressor();

    }
}