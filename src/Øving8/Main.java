package Øving8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException, IOException {

        // Fil som skal komprimeres
        File text = new File("src/Øving8/test_files/test.txt");

        // Fil komprimert med lebel ziv
        File lzcompText = new File("src/Øving8//test_files/compressed_test.txt");
        if (lzcompText.exists()) {
            lzcompText.delete();
            lzcompText = new File("src/Øving8//test_files/compressed_test.txt");
        }

        // fil lzcompText som er videre komprimert med huffman
        File huffComp = new File("src/Øving8/test_files/huffmanComp");
        // dekomprimert huffman
        File huffDeComp = new File("src/Øving8/test_files/huffmanDecomp");

        // dekomprimert med lebel ziv
        File lzDeComp = new File("src/Øving8/test_files/decompressed.txt");
        CompLZ77 comp = new CompLZ77(text, lzcompText);
        comp.compressFile();

        // todo: trenger vi denne??
        // byte[] byteInput = Files.readAllBytes(lzcompText.toPath());
        // dekomprimerer med huffman
        Huffman huff = new Huffman(lzcompText, huffComp);


        DecompLZ77 decompLZ77 = new DecompLZ77(huffDeComp, lzDeComp);
        decompLZ77.decompress();




        //DecompLZ77 decomp = new DecompLZ77();

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
