package Øving9;

import java.io.*;
import java.util.*;

public class Djikstra {
    private PriorityQueue<Node> pq;
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public Djikstra(String nodeFile, String edgdeFile) throws IOException {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();

        readFile(nodeFile, edgdeFile);

        System.out.println("size of nodes: " + nodes.size());
        System.out.println("size of edges: " + edges.size());

        PriorityQueue<Node> queue = new PriorityQueue<>();
    }

    public void readFile(String nodeFile, String edgeFile) throws IOException {
        // Read Nodes
        BufferedReader brNodes = new BufferedReader(new FileReader(nodeFile));
        StringTokenizer stNodes = new StringTokenizer(brNodes.readLine());
        int size = Integer.parseInt(stNodes.nextToken());
        for (int i = 0; i < size; i++) {
            stNodes = new StringTokenizer(brNodes.readLine());
            int number = Integer.parseInt(stNodes.nextToken());
            // double lo = Double.parseDouble(stNodes.nextToken());
            // double la = Double.parseDouble(stNodes.nextToken());
            //nodes.add(new Node(number, lo, la));

            // kaller på konstruktør nummer 2
            nodes.add(new Node(number)); // New nodes with cost = infinite

        }
        
        // Read edges
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

        addNeigbours(); // legger til
    }


    /**
     * legger til alle nabo noder til nodene
     */
    public void addNeigbours() {
        for (Edge e: edges) {
            // Fra noden til en enge
            Node n = e.getFrom();
            // Legger til edge i vedsiden av listen til node
            // NB: alle noder betår av en vedsiden av liste (adj_list)
            n.addNeigbour(e);

        }
    }

    // for å hente ut spesifik node fra en index
    public Node getNodeFromList(int index) {
        return nodes.get(index);
    }


    /**
     *
     * @param start
     */
    public void findShortestDistance(Node start) {
        start.setDistance(0); // start have cost 0

        PriorityQueue<Node> queue = new PriorityQueue<>();

        queue.add(start);
        start.setVisisted(true);

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            for (Edge e: current.getAdjList()) {

                Node n = e.getTo();

                if (!n.isVisisted()) {
                    int dist = current.getDistance() + e.getWeight();

                    if (dist < n.getDistance()) {
                        // remove from queue
                        queue.remove(n);
                        // update values
                        n.setDistance(dist);
                        n.setPredeseccor(current);
                        // add to queue again with new values
                        queue.add(n);
                    }
                }
            }
            current.setVisisted(true);
        }
    }

    public List<Node> getShortestPath(Node target) {
        List<Node> path = new ArrayList<>();

        for (Node n = target; n != null; n = n.getPredeseccor() ) {
            path.add(n);
        }
        Collections.reverse(path);
        return path;
    }
}
