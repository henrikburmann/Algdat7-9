package Øving8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException {
        String test = "alle barna elsker selskap";
        Huffman h = new Huffman(test);


    }
    public static void skrivUt(HuffmanNode n){
        if (n.isLeaf()){
            System.out.println(n.character + " " + n.frequency);
            return;
        }
        skrivUt(n.leftChild);
        skrivUt(n.rightChild);
    }


}
