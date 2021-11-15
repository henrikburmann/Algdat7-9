package Øving9;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Djikstra {
    private PriorityQueue<Node> pq;
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public Djikstra(){
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void readFile(String nodeFile, String edgeFile) throws IOException {
        // Read Nodes
        BufferedReader brNodes = new BufferedReader(new FileReader(nodeFile));
        StringTokenizer stNodes = new StringTokenizer(brNodes.readLine());
        int size = Integer.parseInt(stNodes.nextToken());
        for (int i = 0; i < size; i++) {
            stNodes = new StringTokenizer(brNodes.readLine());
            int number = Integer.parseInt(stNodes.nextToken());
            double lo = Double.parseDouble(stNodes.nextToken());
            double la = Double.parseDouble(stNodes.nextToken());
            nodes.add(new Node(number, lo, la));
        }
        
        //Read edges
        BufferedReader brEdges = new BufferedReader(new FileReader(edgeFile));
        StringTokenizer stEdges = new StringTokenizer(brEdges.readLine());
        size = Integer.parseInt(stEdges.nextToken());
        for (int i = 0; i < size; i++) {
            stEdges = new StringTokenizer(brEdges.readLine());
            Node from = nodes.get(Integer.parseInt(stEdges.nextToken()));
            Node to = nodes.get(Integer.parseInt(stEdges.nextToken()));
            int weight = Integer.parseInt(stEdges.nextToken());
            edges.add(new Edge(from, to, weight));
        }
        for (int i = 0; i < edges.size(); i++) {
            System.out.println(edges.get(i).getFrom().number + " " + edges.get(i).getTo().number + " " + edges.get(i).getWeight());
        }
    }
}
