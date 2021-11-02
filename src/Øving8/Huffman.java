package Øving8;

import java.io.*;
import java.util.ArrayList;

public class Huffman {
    private static int arraySize = 256;

    public Huffman(String text){
        int freqency[] = frequencyTable(text);
    }

    public static int[] frequencyTable(String text){
        int[] freq = new int[arraySize];
        for (char character: text.toCharArray()){
            freq[character]++;
        }
        return freq;
    }

}
