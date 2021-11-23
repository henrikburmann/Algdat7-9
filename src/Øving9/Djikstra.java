package Øving9;

import java.io.*;
import java.util.*;

public class Djikstra {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private ArrayList<Interessepkt> interessepkts;

    public Djikstra(String nodeFile, String edgdeFile, String interessepktPath) throws IOException {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        interessepkts = new ArrayList<>();

        readFile(nodeFile, edgdeFile, interessepktPath);
        addNeigbours();

        System.out.println("size of nodes: " + nodes.size());
        System.out.println("size of edges: " + edges.size());
        System.out.println("size of interessepkts: " + interessepkts.size());

    }

    public void readFile(String nodeFile, String edgeFile, String intressepktPath) throws IOException {
        // Leser noder
        BufferedReader brNodes = new BufferedReader(new FileReader(nodeFile));
        StringTokenizer stNodes = new StringTokenizer(brNodes.readLine());
        int size = Integer.parseInt(stNodes.nextToken());
        //Da breddegrader ikke er viktig for Djikstra trenger vi ikke lese
        // mer fra filen enn antall noder.
        for (int i = 0; i < size; i++) {
            stNodes = new StringTokenizer(brNodes.readLine());
            int number = Integer.parseInt(stNodes.nextToken());
            double lo = Double.parseDouble(stNodes.nextToken());
            double la = Double.parseDouble(stNodes.nextToken());
            nodes.add(new Node(number, lo, la));
        }
        
        // Leser kanter
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

        BufferedReader brInteressepkt = new BufferedReader(new FileReader(intressepktPath));
        StringTokenizer stInteressepkt = new StringTokenizer(brInteressepkt.readLine());
        int intressepktSize = Integer.parseInt(stInteressepkt.nextToken());

        for (int i = 0; i < intressepktSize; i++) {
            stInteressepkt = new StringTokenizer(brInteressepkt.readLine());
            int nodeNumber = Integer.parseInt(stInteressepkt.nextToken());
            int type = Integer.parseInt(stInteressepkt.nextToken());
            nodes.get(nodeNumber).setType(type); //S

            String name = stInteressepkt.nextToken(); // funker ikke hvis navn har mellomrom

            interessepkts.add(new Interessepkt(nodeNumber, type, name));
        }

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
     * Forbedring
     * Stopper når man kommer til sluttnode
     * @param start
     */
    public void findShortestDistance(Node start, Node end) {

        start.setDistance(0); // start have cost 0

        PriorityQueue<Node> queue = new PriorityQueue<>();

        queue.add(start);
        start.setVisisted(true);
        int nodesVisited = 0;
        while (!queue.isEmpty()) {

            Node current = queue.poll();
            nodesVisited++;
            //Hvis endenoden tas ut av køen returnerer metoden
            if (current == end){
                System.out.println("Antall noder besøkt: " + nodesVisited);
                return;
            }
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

    //Går fra endenoden til startnoden for å finne veien som ble gått.
    public List<Node> getShortestPath(Node target) {
        List<Node> reversedPath = new ArrayList<>();
        //Bactracer fra end node til startnode
        for (Node n = target; n != null; n = n.getPredeseccor() ) {
            reversedPath.add(n); // legger til i reversert rekkefølge
        }
        //Reverserer den reverserte veien for å få den riktig retning.
        List<Node> path = new ArrayList<>();
        for (int i = reversedPath.size() - 1; i >= 0 ; i--) {
            path.add(reversedPath.get(i));
        }

        Collections.reverse(reversedPath); // snur om listen

        return reversedPath;
    }


    private boolean isInteressePunkt(Node node, int type, ArrayList<Interessepkt> interessepkter ) {
        for (Interessepkt ip: interessepkter) {
            if (node.number == ip.getNodeNumber() && node.getType() == type) { // prøver å fjerne type check
                return true;
            }
        }
        return false;
    }

    private void reset(){
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setDistance(Integer.MAX_VALUE / 2);
            nodes.get(i).setVisisted(false);
        }
    }
    public List<Node> xNearestGasStations(int startNode, int amount, int type) throws IOException {
        reset();
        int visited = 0;
        Node start = getNodeFromList(startNode);
        start.setDistance(0); // start have cost 0

        PriorityQueue<Node> queue = new PriorityQueue<>();
        List<Node> interesser = new ArrayList<>();
        System.out.println("Størrelse interessepkt: " + interessepkts.size());
        queue.add(start);
        start.setVisisted(true);
        int nodesVisited = 0;
        while (!queue.isEmpty()) {

            Node current = queue.poll();
            nodesVisited++;


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
            if (isInteressePunkt(current, type, interessepkts)) {
                visited++;
                interesser.add(current);
                if (visited == amount ) {
                    return interesser;
                }
            }
            current.setVisisted(true);
        }
        return interesser;
    }
}