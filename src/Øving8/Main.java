package Øving8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException, IOException {
        File test = new File("src/Øving8/test_files/test.txt");

        File LZ77Comp = new File("src/Øving8/test_files/compressed_test.txt");
        File HuffmanComp = new File("src/Øving8/test_files/huffmanComp.txt");

        File HuffmanDecomp = new File("src/Øving8/test_files/huffmanDecomp" +
                ".txt");
        File LZ77Decomp = new File("src/Øving8/test_files/decompressed_test" +
                ".txt");

        CompLZ77 compLZ77 = new CompLZ77(test, LZ77Comp);
        compLZ77.compressFile();

        HuffmanEncode.compress(LZ77Comp.toString(), HuffmanComp.toString());

        HuffmanDecode.deCompress(HuffmanComp.toString(), HuffmanDecomp.toString());

        DecompLZ77 decompLZ77 = new DecompLZ77(HuffmanDecomp, LZ77Decomp);
        decompLZ77.decompress();
    }
}  
