package Øving8;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.zip.DataFormatException;

public class Huffman {
    private List<Byte> bytes;
    private static int ARRAY_SIZE = 256;
    HuffmanNode root;
    static String[] bitStrings;
    static int[] freq;
    DataOutputStream out;

    public Huffman(File fileIn, File fileOut) throws IOException {
        bytes = new ArrayList<>();
        freq = frequencyTable(fileIn);
        bitStrings = new String[freq.length];
    }

    public int compress(byte[] compressedBytes, String outPath) throws IOException {
        for (int i = 0; i < compressedBytes.length; ++i) {
            int b = compressedBytes[i];
            if (compressedBytes[i] < 0){
                freq[ARRAY_SIZE + b] ++;
            }
            else freq[b] ++;
        }

        lookUpTable(root, "");
        writeToOutputFile(outPath, compressedBytes);
        root = huffmanTree(freq);
        return out.size();
    }

    private static ArrayList<HuffmanNode> makeNodeList(int[] frequencies){
        ArrayList<HuffmanNode> nodeList = new ArrayList<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] != 0){
                nodeList.add(new HuffmanNode((char) i, frequencies[i], null,
                        null));
            }
        }
        return nodeList;
    }
    public byte[] deCompress(String inputFile, String outputFile) throws IOException {
        DataInputStream input =
                new DataInputStream(new FileInputStream(inputFile));
        int[] frequencies = new int[ARRAY_SIZE];
        for (int i = 0; i < frequencies.length; i++) {
            int freq = input.readInt();
            frequencies[i] = freq;
        }

        ArrayList<Byte> out = new ArrayList<>();

        int lastByte = input.readInt();
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(256,
                (a,b) -> a.frequency -b.frequency);
        priorityQueue.addAll(makeNodeList(frequencies));
        HuffmanNode tree = huffmanTree(frequencies);//Kan måtte gjøres om
        byte ch;
        byte[] bytes = input.readAllBytes();
        input.close();

        int length = bytes.length;
        Bitstring h = new Bitstring(0, 0);
        if (lastByte > 0) length --;
        for (int i = 0; i < length; i++) {
            ch = bytes[i];
            Bitstring b = new Bitstring(8, ch);
            h = Bitstring.concat(h,b);
            h =writeChar(tree, h, out);
        }
        if (lastByte > 0){
            Bitstring b = new Bitstring(lastByte, bytes[length] >> (8 - lastByte));
            h = Bitstring.concat(h, b);
            writeChar(tree, h, out);
        }
        input.close();
        return toByteArray(out);
    }

    public void writeDecomp(byte[] arr, File outPut) throws IOException {
        BufferedWriter bufferedWriter =
                new BufferedWriter(new FileWriter(outPut.getPath()));
        String x = new String(arr);
        bufferedWriter.write(x);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public byte[] toByteArray(ArrayList<Byte> list){
        byte[] byteArray = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            byteArray[i] = list.get(i);
        }
        return byteArray;
    }
    private static Bitstring writeChar(HuffmanNode tree, Bitstring bitstring,
                                       ArrayList<Byte> decompessedBytes){
        HuffmanNode tempTree = tree;
        int c = 0;
        for(long j = 1 << bitstring.length -1; j != 0; j >>= 1){
            c++;

            if ((bitstring.bits & j) == 0){
                tempTree = tempTree.leftChild;
            }
            else tempTree = tempTree.rightChild;

        if (tempTree.leftChild == null){
            long cha = tempTree.character;
            decompessedBytes.add((byte) cha);
            long temp = (long) ~(0);
            bitstring.bits = (bitstring.bits & temp);
            bitstring.length = bitstring.length - c;
            c = 0;
            tempTree = tree;
        }}
        return bitstring;
    }

    public int[] frequencyTable(File text) throws IOException {
        byte[] fileBytes = Files.readAllBytes(text.toPath());
        int[] freq = new int[ARRAY_SIZE + 1];
        for (int i = 0; i < text.length(); i++) {
            freq[(fileBytes[i] + ARRAY_SIZE) % ARRAY_SIZE] ++;
        }
        freq[freq.length - 1] = 1;
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
            priorityQueue.add(new HuffmanNode('\0',1, null, null));
        }
        while (priorityQueue.size() > 1){
            HuffmanNode left = priorityQueue.poll();
            //System.out.println(left.character + " " +left.frequency);
            HuffmanNode right = priorityQueue.poll();
            //System.out.println(right.character + " " + right.frequency);
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
            return;
        }
        lookUpTable(n.leftChild,  s + "0");
        lookUpTable(n.rightChild, s + "1");
    }

    public static byte[] getEncodedByteArray(String fileToString){
        ArrayList<Byte> encodedBytes = new ArrayList<>();

        for (int i = 0; i < fileToString.length(); i++) {
            encodedBytes.add((byte)Integer.parseInt(bitStrings[fileToString.charAt(i)]));
        }
        byte[] answer = new byte[encodedBytes.size()];
        for (int i = 0; i < encodedBytes.size(); i++) {
            answer[i] = encodedBytes.get(i);
        }
        return answer;
    }
    /*public static byte[] endcodingsArray(String fileToString){
        ArrayList<Byte> endcodedBytes = new ArrayList<>();

        for (int i = 0; i < fileToString.length(); i++) {
            endcodedBytes.add((byte) Integer.parseInt())
        }
    }*/

    private void writeToOutputFile(String outpath, byte[] compressedBytes) throws IOException {
       out =
               new DataOutputStream(new FileOutputStream(outpath));
        for (int i: freq){
            out.writeInt(i);
        }

        int lastByte = parseBitStringAndGetLastByte(compressedBytes);
        out.writeInt(lastByte);

        writeBytes();

        out.close();
    }

    private void writeBytes() throws IOException {
        for (Byte b: bytes){
            out.write(b);
        }
    }
    private int parseBitStringAndGetLastByte(byte[] compressedBytes){
        int input;
        int i = 0;
        int j;
        int k;
        long currentByte = 0L;

        for (k = 0; k < compressedBytes.length; k++) {
            input = compressedBytes[k];
            if (input < 0){
                input += ARRAY_SIZE;
            }
            String bitString = bitStrings[input];

            j = 0;
            while(j < bitString.length()){
                if (bitString.charAt(j) == '0'){
                    currentByte = (currentByte << 1);
                }
                else{
                    currentByte = (currentByte << 1 | 1);
                }
                ++j;
                ++i;

                if (i == 8){
                    bytes.add((byte) currentByte);
                    i = 0;
                    currentByte = 0L;
                }
            }
        }
        int lastByte = i;
        while (i < 8 && i != 0){
            currentByte = (currentByte << 1);
            ++i;
        }
        bytes.add((byte) currentByte);

        return lastByte;
    }


    static class Bitstring{
        int length;
        long bits;

        Bitstring(){

        }

        Bitstring(int len, long bits){
            this.length = len;
            this.bits = bits;
        }

        Bitstring(int len, byte b){
            this.length = len;
            this.bits = convertByte(b, len);
        }

        static Bitstring concat(Bitstring bitstring, Bitstring other){
            Bitstring newBitstring = new Bitstring();
            newBitstring.length = bitstring.length + other.length;

            if (newBitstring.length > 64){
                throw new IllegalArgumentException("Bitstring too long");
            }

            newBitstring.bits = other.bits | (bitstring.bits << other.length);
            return newBitstring;
        }

        public long convertByte(byte b, int length){
            long temp = 0;
            for (long i = 1 << length -1; i != 0; i >>= 1) {
                if ((b & 1) == 0){
                    temp = (temp << 1);
                }
                else{
                    temp = ((temp <<1) | 1);
                }
            }
            return temp;
        }

        public void remove(){
            this.bits = (bits >> 1);
            this.length--;
        }
    }
}
/*public void writeToFile(String in, String out) throws IOException {
        File fileIn = new File(in);
        File fileOut = new File(out);
        int[] frequencyTable = frequencyTable(fileIn);
        lookUpTable(root, "");
        byte[] inputFile = Files.readAllBytes(fileIn.toPath());
        String fileToString = "";
        FileWriter fileWriter = new FileWriter(fileOut);
        for (int i = 0; i < frequencyTable.length; i++) {
            fileWriter.write(frequencyTable[i]);
        }
        fileWriter.close();
        FileOutputStream fileOutputStream =
                new FileOutputStream(fileOut.getPath(), true);
        fileToString += bitStrings[256];
        for (byte b: inputFile){
            fileToString += bitStrings[(b + ARRAY_SIZE) % ARRAY_SIZE];
        }
        System.out.println(fileToString);
        fileOutputStream.write(getEncodedByteArray(fileToString));
        fileOutputStream.close();
        /*if (fileToString.length() % 64 == 0 && fileToString.length() > 0){
            fileOutputStream.write(bitStrings);
        }

    }*/