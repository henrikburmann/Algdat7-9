
public class HuffmanNodeHenrik implements Comparable<HuffmanNodeHenrik> {
    char character;
    int frequency;
    HuffmanNodeHenrik leftChild;
    HuffmanNodeHenrik rightChild;
    int weight = 0;

    HuffmanNodeHenrik(char character, int frequency, HuffmanNodeHenrik leftChild,
                HuffmanNodeHenrik rightChild){
        this.character = character;
        this.frequency = frequency;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    boolean isLeaf(){
        return this.leftChild == null && this.rightChild == null;
    }

    @Override
    public int compareTo(HuffmanNodeHenrik n) {
        int freqComparison = Integer.compare(this.frequency, n.frequency);
        if (freqComparison != 0){
            return freqComparison;
        }
        return Integer.compare(this.character, n.character);
    }


}

