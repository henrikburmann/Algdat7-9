package Øving8;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException {
        String in = "src/Øving8/Fil";
        String out = "src/Øving8/Fil2";

        Huffman huffman = new Huffman(new File(in), new File(out));

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
