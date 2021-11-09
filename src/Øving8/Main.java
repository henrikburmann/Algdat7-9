package Øving8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException, IOException {

        File text = new File("src/Øving8/test_files/test.txt");
        File lzcompText = new File("src/Øving8//test_files/compressed_test" +
                ".txt");
        File huffComp = new File("src/Øving8/test_files/huffmanComp.txt");
        File huffDeComp = new File("src/Øving8/test_files/huffmanDecomp.txt");
        File lzDeComp = new File("src/Øving8/test_files/decompressed.txt");

        CompLZ77 comp = new CompLZ77(text, lzcompText);
        comp.compressFile();
        byte[] inputFile = Files.readAllBytes(lzcompText.toPath());

        Huffman huff = new Huffman(lzcompText, huffComp);
        huff.compress(inputFile, "src/Øving8/test_files/huffmanComp.txt");
        byte [] arr = huff.deCompress("src/Øving8/test_files/huffmanComp.txt",
                "src/Øving8" +
                "/test_files/huffmanDecomp.txt");

        huff.writeDecomp(arr, huffDeComp);
        DecompLZ77 decomp = new DecompLZ77(huffDeComp, lzDeComp);
        decomp.decompress();

        int i = 0;
        int k = 1;
        int l = 1;



    }


    public static void skrivUt(HuffmanNode n){
        if (n.isLeaf()){
            System.out.println(n.frequency);
            return;
        }
        skrivUt(n.leftChild);
        skrivUt(n.rightChild);
    }
}  
