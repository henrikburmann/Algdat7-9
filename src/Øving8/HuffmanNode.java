package Øving8;

public class HuffmanNode implements Comparable<HuffmanNode> {
        char character;
        int frequency;
        HuffmanNode leftChild;
        HuffmanNode rightChild;
        int weight = 0;

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

