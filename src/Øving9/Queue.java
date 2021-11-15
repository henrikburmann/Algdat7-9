package Øving9;

public class Queue {
    private Node[] array;
    private int start = 0;
    private int end = 0;
    private int num = 0;

    public Queue(int size){
        array = new Node[size];
    }

    public boolean empty(){
        return num == 0;
    }

    public boolean full(){
        return num == array.length;
    }

    public void addToQueue(Node n){
        if (full()) return;
        array[end] = n;
        end = (end + 1) % array.length;
        ++num;
    }

    public Node nextInQueue(){
        if (!empty()){
            Node n = array[start];
            start = (start + 1) % array.length;
            --num;
            return n;
        }
        else return null;
    }

    public Node checkQueue(){
        if (!empty()) return array[start];
        else return null;
    }
}
