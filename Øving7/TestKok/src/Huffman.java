
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Huffman {
    private static final int MAX_BYTE_VALUE = 256;

    // frequencies[i] = frequency of char i
    private List<Byte> bytes;

    private int[] frequencies;
    private String[] bitstrings;
    DataOutputStream out;

    public Huffman() {
        this.frequencies = new int[MAX_BYTE_VALUE];
        this.bitstrings = new String[MAX_BYTE_VALUE];
        bytes = new ArrayList<>();
    }

/*
    public int compress(String filename, String outpath) throws IOException {
        this.filename = filename;
        parseFile();
        HuffmanNode root = buildHuffmanTree();
        parseCodes(root, "");
        writeToOutputFile(outpath);
        return out.size();
    }
 */

    public int compress(byte[] compressedBytes,  String outpath) throws IOException {
        for (int i = 0; i < compressedBytes.length; ++i){
            int b = compressedBytes[i];
            if (compressedBytes[i] < 0)
                frequencies[MAX_BYTE_VALUE + b]++;
            else frequencies[b]++;
        }
        HuffmanNode root = buildHuffmanTree();
        parseCodes(root, "");
        writeToOutputFile(outpath, compressedBytes);

        return out.size();
    }

    private HuffmanNode buildHuffmanTree() {
        MinHeap heap = new MinHeap(frequencies.length);
        heap.createAndBuild(frequencies);

        HuffmanNode root = new HuffmanNode();

        while (!heap.isOfSizeOne()) {
            HuffmanNode left = heap.getMin();
            HuffmanNode right = heap.getMin();

            int topFrequency = left.frequency + right.frequency;
            HuffmanNode top = new HuffmanNode('\0', topFrequency);

            top.left = left;
            top.right = right;

            heap.insert(top);
            root = top;
        }

        return root;
    }

    private void parseCodes(HuffmanNode root, String s) {
        if (root.left == null && root.right == null) {
            bitstrings[root.character] = s;
            return;
        }

        parseCodes(root.left, s + "0");
        parseCodes(root.right, s + "1");
    }

    private void writeToOutputFile(String outpath, byte[] compressedBytes) throws IOException {
        out = new DataOutputStream(new FileOutputStream(outpath));

        for (int i : frequencies)
            out.writeInt(i);

        int lastByte = parseBitStringsAndGetLastByte(compressedBytes);
        out.writeInt(lastByte);

        writeBytes();

        out.close();
    }

    private void writeBytes() throws IOException {
        for (Byte s : bytes) {
            out.write(s);
        }
    }

    private int parseBitStringsAndGetLastByte(byte[] compressedBytes ) throws IOException {
        int input;
        int i = 0;
        int j;
        int k;
        long currentByte = 0L;

        for (k = 0; k < compressedBytes.length; k++) {
            input = compressedBytes[k];
            if (input < 0)
                input += MAX_BYTE_VALUE;

            String bitString = bitstrings[input];

            j = 0;
            while (j < bitString.length()) {
                if (bitString.charAt(j) == '0')
                    currentByte = (currentByte << 1);
                else
                    currentByte = ((currentByte << 1) | 1); // times 2 + 1

                ++j;
                ++i;

                if (i == 8) {
                    bytes.add((byte) currentByte);
                    i = 0;
                    currentByte = 0L;
                }
            }
        }

        int lastByte = i; // ?
        while (i < 8 && i != 0) {
            currentByte = (currentByte << 1);
            ++i;
        }
        bytes.add((byte) currentByte);

        return lastByte;
    }

    private static ArrayList<HuffmanNode> makeNodeList(int[] frequensies) {
        ArrayList<HuffmanNode> nodeList = new ArrayList<>();
        for (int i = 0; i < frequensies.length; i++) {
            if (frequensies[i] != 0) {
                nodeList.add(new HuffmanNode((char) i, frequensies[i]));
            }
        }
        return nodeList;
    }

    public byte[] decompress(String inputFile) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(inputFile));
        int[] frequencies = new int[256];
        for (int i = 0; i < frequencies.length; i++) {
            int freq = in.readInt();
            frequencies[i] = freq;
        }
        ArrayList<Byte> out = new ArrayList<>();

        int lastByte = in.readInt();
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(256, (a, b) -> a.frequency - b.frequency);
        pq.addAll(makeNodeList(frequencies));
        HuffmanNode tree = HuffmanNode.makeHuffmanTree(pq);
        byte ch;
        byte[] bytes = in.readAllBytes();
        in.close();
        int length = bytes.length;
        Bitstring h = new Bitstring(0, 0);
        if (lastByte > 0) length--;
        for (int i = 0; i < length; i++) {
            ch = bytes[i];
            Bitstring b = new Bitstring(8, ch);
            h = Bitstring.concat(h, b);
            h = writeChar(tree, h, out);
        }
        if (lastByte > 0) {
            Bitstring b = new Bitstring(lastByte, bytes[length] >> (8 - lastByte));
            h = Bitstring.concat(h, b);
            writeChar(tree, h, out);
        }
        in.close();

        return toByteArray(out);
    }

    public byte[] toByteArray(ArrayList<Byte> list) throws IOException {
        byte[] byteArray = new byte[list.size()];
        for (int i = 0; i < list.size(); i++){
            byteArray[i] = list.get(i);
        }
        return byteArray;
    }

    private static Bitstring writeChar(HuffmanNode tree, Bitstring bitstring, ArrayList<Byte> decompressedBytes) throws IOException {

        HuffmanNode tempTree = tree;
        int c = 0;
        for (long j = 1 << bitstring.lengde - 1; j != 0; j >>= 1) {
            c++;
            if ((bitstring.biter & j) == 0)
                tempTree = tempTree.left;
            else
                tempTree = tempTree.right;

            if (tempTree.left == null) {
                long cha = tempTree.character;
                decompressedBytes.add((byte) cha);
                long temp = (long) ~(0);
                bitstring.biter = (bitstring.biter & temp);
                bitstring.lengde = bitstring.lengde - c;
                c = 0;
                tempTree = tree;
            }
        }
        return bitstring;
    }

    static class Bitstring {
        int lengde;
        long biter;

        Bitstring() {
        }

        Bitstring(int len, long bits) {
            lengde = len;
            biter = bits;
        }

        Bitstring(int len, byte b) {
            this.lengde = len;
            this.biter = convertByte(b, len);
        }

        static Bitstring concat(Bitstring bitstring, Bitstring other) {
            Bitstring ny = new Bitstring();
            ny.lengde = bitstring.lengde + other.lengde;

            if (ny.lengde > 64)
                throw new IllegalArgumentException("For lang bitstreng, går ikke! " + ny.biter + ", lengde=" + ny.lengde);

            ny.biter = other.biter | (bitstring.biter << other.lengde);
            return ny;
        }

        public long convertByte(byte b, int length) {
            long temp = 0;
            for (long i = 1 << length - 1; i != 0; i >>= 1) {
                if ((b & i) == 0) {
                    temp = (temp << 1);
                } else temp = ((temp << 1) | 1);
            }
            return temp;
        }

        public void remove() {
            this.biter = (biter >> 1);
            this.lengde--;
        }
    }
}