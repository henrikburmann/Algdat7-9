package Øving9;

public class Interessepkt {

    private int nodeNumber;
    private int type;
    private String name;


    public Interessepkt(int nodeNumber, int type, String name) {
        this.nodeNumber = nodeNumber;
        this.type = type;
        this.name = name;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "";
    }
}