package Øving8;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Huffman {
    private static int ARRAY_SIZE = 256;
    HuffmanNode root;
    String[] bitStrings;

    public Huffman(String text){
        root = huffmanTree(frequencyTable(text));
        bitStrings = new String[ARRAY_SIZE];
        lookUpTable(root, "");
    }

    public int[] frequencyTable(String text){
        int[] freq = new int[ARRAY_SIZE];
        for (char character: text.toCharArray()){
            freq[character]++;
        }
        return freq;
    }

    public HuffmanNode huffmanTree(int[] freq){
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (char i = 0; i < ARRAY_SIZE; i++) {
            if (freq[i] > 0){
                priorityQueue.add(new HuffmanNode(i, freq[i], null, null));
            }
        }
        if (priorityQueue.size() == 1){
            priorityQueue.add(new HuffmanNode('z',1, null, null));
        }
        while (priorityQueue.size() > 1){
            HuffmanNode left = priorityQueue.poll();
            System.out.println(left.character + " " +left.frequency);
            HuffmanNode right = priorityQueue.poll();
            System.out.println(right.character + " " + right.frequency);
            HuffmanNode parent = new HuffmanNode('\0',
                    left.frequency + right.frequency,
                    left, right);
            priorityQueue.add(parent);
        }
        return priorityQueue.poll();
    }

    public void lookUpTable(HuffmanNode n, String s){
        if (n.isLeaf()){
            bitStrings[n.character] = s;
            System.out.println(n.character + bitStrings[n.character]);
            return;
        }
        lookUpTable(n.leftChild,  s + "0");
        lookUpTable(n.rightChild, s + "1");
    }

    public String getLookUp(){
        return bitStrings[107];
    }

}
