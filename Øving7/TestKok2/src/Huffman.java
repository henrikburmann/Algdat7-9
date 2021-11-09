import java.util.PriorityQueue;

public class Huffman {
    private HuffmanNode root;
    private int ARRAY_SIZE;
    private String[] bitStrings;

    public Huffman(int[] frequencyArray) {
        this.ARRAY_SIZE = frequencyArray.length;
        this.root = generateRoot(frequencyArray);
        this.bitStrings = new String[ARRAY_SIZE + 1];
    }
    
    public HuffmanNode getRoot() {
        return root;
    }

    public HuffmanNode generateRoot(int[] freq){
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
            HuffmanNode right = priorityQueue.poll();
            HuffmanNode parent = new HuffmanNode('\0',
                    left.frequency + right.frequency,
                    left, right);
            priorityQueue.add(parent);
        }
        return priorityQueue.poll();
    }

    public String[] getEncodingArray(){
        lookUpTable(root, "");
        return bitStrings;
    }


    private void lookUpTable(HuffmanNode n, String s){
        if (n.isLeaf()){
            bitStrings[n.character] = s;
            return;
        }
        lookUpTable(n.leftChild,  s + "0");
        lookUpTable(n.rightChild, s + "1");
    }
    
    
    public Integer decodeHuffmanCode(String huffManString){
        HuffmanNode current = getRoot();
        for(char c : huffManString.toCharArray()){
            current = (c == '0') ? current.leftChild : current.rightChild;
        }

        if(current == null || current.leftChild != null || current.rightChild != null) return null;
        return (int)current.character;
    }

}

class HuffmanNode implements Comparable<HuffmanNode> {
    char character;
    int frequency;
    HuffmanNode leftChild;
    HuffmanNode rightChild;

    HuffmanNode(char character, int frequency, HuffmanNode leftChild,
                HuffmanNode rightChild){
        this.character = character;
        this.frequency = frequency;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    boolean isLeaf(){
        return this.leftChild == null && this.rightChild == null;
    }

    @Override
    public int compareTo(HuffmanNode n) {
        int freqComparison = Integer.compare(this.frequency, n.frequency);
        if (freqComparison != 0){
            return freqComparison;
        }
        return Integer.compare(this.character, n.character);
    }
}

