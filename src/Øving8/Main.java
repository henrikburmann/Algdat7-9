package Øving8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException, IOException {

        File test = new File("test.txt"); // fil som skal komprimeres

        File LZ77Comp = new File("compressed_test.txt"); // fil komprimert med lebel ziv

        File HuffmanComp = new File("huffmanComp.txt"); // fil komprimert videre med huffman

        File HuffmanDecomp = new File("huffmanDecomp" + // fil dekomprimert med huffman
                ".txt");
        File LZ77Decomp = new File("decompressed_test" + // fil decomprimert med lebel ziv, skal være lik test.txt
                ".txt");

        CompLZ77 compLZ77 = new CompLZ77(test, LZ77Comp);
        compLZ77.compressFile();

        HuffmanEncode.compress(LZ77Comp.toString(), HuffmanComp.toString());

        HuffmanDecode.deCompress(HuffmanComp.toString(), HuffmanDecomp.toString());

        DecompLZ77 decompLZ77 = new DecompLZ77(HuffmanDecomp, LZ77Decomp);
        decompLZ77.decompress();
    }
}  
