
import java.util.Comparator;
import java.util.PriorityQueue;

public class HuffmanNode implements Comparator<HuffmanNode> {

    int frequency;
    char character;

    HuffmanNode left;
    HuffmanNode right;

    public HuffmanNode() {
    }

    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }


    public static HuffmanNode makeHuffmanTree(PriorityQueue<HuffmanNode> pq) {
        HuffmanNode tree = new HuffmanNode();
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode top = new HuffmanNode('\0', findSum(left, right));

            top.left = left;
            top.right = right;

            pq.add(top);
            tree = top;
        }
        return tree;

    }

    private static int findSum(HuffmanNode t, HuffmanNode n) {
        return t.frequency + n.frequency;
    }

    @Override
    public int compare(HuffmanNode o1, HuffmanNode o2) {
        return o1.frequency - o2.frequency;
    }
} 