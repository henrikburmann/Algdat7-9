package Øving9;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    int number;
    double longitude; //Lengdegrad
    double latitude; //Breddegrad
    private int distance;

    private boolean visisted;
    private Node predeseccor;
    private List<Edge> adjList;

    public Node(int number, double longitude, double latitude){
        this.number = number;
        this.longitude = longitude;
        this.latitude = latitude;
        this.adjList = new ArrayList<>();
        this.distance = Integer.MAX_VALUE;
    }

    // Hvis man bare skal kjøre djikstras uten ALT
    public Node(int number) {
        this.number = number;
        this.distance = Integer.MAX_VALUE; // Infinite
        this.visisted = false;
        this.adjList = new ArrayList<>();
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int incrementValue(int add) {
        if (this.distance == Integer.MAX_VALUE) {
            distance = 0;
        }
        distance += add;
        return distance;
    }


    public int getDistance() {
        return distance;
    }

    public void addNeigbour(Edge edge) {
        this.adjList.add(edge);
    }

    public boolean isVisisted() {
        return visisted;
    }

    public Node getPredeseccor() {
        return predeseccor;
    }

    public List<Edge> getAdjList() {
        return adjList;
    }

    public void setVisisted(boolean visisted) {
        this.visisted = visisted;
    }

    public void setPredeseccor(Node predeseccor) {
        this.predeseccor = predeseccor;
    }

    public void setAdjList(List<Edge> adjList) {
        this.adjList = adjList;
    }

    @Override
    public int compareTo(Node o) {
        Node yo = (Node) o;
        return Integer.compare(this.distance, yo.distance);
    }

    @Override
    public String toString() {
        return number + "";
    }
}
