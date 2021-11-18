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
    private List<Edge> oppositeAdjList;

    //Konstruktør for bruk av ALT
    public Node(int number, double longitude, double latitude){
        this.number = number;
        this.longitude = convertToRadian(longitude);
        this.latitude = convertToRadian(latitude);
        this.adjList = new ArrayList<>();
        this.oppositeAdjList = new ArrayList<>();
        this.distance = Integer.MAX_VALUE;
    }

    // Konstruktør for bruk av Djikstra
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

    //Converts degrees to radians.
    private double convertToRadian(double degrees){
        return degrees * Math.PI / 180;
    }

    public int getDistance() {
        return distance;
    }

    public void addNeigbour(Edge edge) {
        this.adjList.add(edge);
    }

    public void addOppoNeigbour(Edge edge){
        this.oppositeAdjList.add(edge);
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

    public List<Edge> getOppositeAdjList(){
        return oppositeAdjList;
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
    public int compareTo(Node n) {
        return Integer.compare(this.distance, n.distance);
    }

    @Override
    public String toString() {
        return number + "";
    }
}
