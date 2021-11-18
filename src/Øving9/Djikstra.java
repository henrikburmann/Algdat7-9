package Øving9;

import java.io.*;
import java.util.*;

public class Djikstra {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public Djikstra(String nodeFile, String edgdeFile) throws IOException {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();

        readFile(nodeFile, edgdeFile);

        System.out.println("size of nodes: " + nodes.size());
        System.out.println("size of edges: " + edges.size());

    }

    public void readFile(String nodeFile, String edgeFile) throws IOException {
        // Leser noder
        BufferedReader brNodes = new BufferedReader(new FileReader(nodeFile));
        StringTokenizer stNodes = new StringTokenizer(brNodes.readLine());
        int size = Integer.parseInt(stNodes.nextToken());
        //Da breddegrader ikke er viktig for Djikstra trenger vi ikke lese
        // mer fra filen enn antall noder.
        for (int i = 0; i < size; i++) {
            nodes.add(new Node(i));
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

        addNeigbours();
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

    /**
     * Lager en fil med distansen fra startnodene til
     * alle landemerkene som man har definert
     * Todo: skal man bruke .csv eller .txt?? finner ut av det senere
     */
    public void generateFromStartToLandmarkFile(int n, int s, int e, int v, String outFile) throws FileNotFoundException {


    }


    /**
     *
     * @param n
     * @param s
     * @param e
     * @param v
     * @throws FileNotFoundException
     *
     */

    public void generateToStartFromLandmarkFile(int n, int s, int e, int v) throws FileNotFoundException {

        // OutputStream stream = new FileOutputStream(new File(outFile));
        int[] landmarks = {n,s,e,v};

        for (int landmark: landmarks) {
            findShortestDistanceToAll(nodes.get(0));

            for (int i = 0; i < nodes.size(); i++) {
                // henter ut distansen fra landmark til alle nodene
                // todo: må nu skrive dette til fil
            }
            // resetDistance();
        }
        System.out.println(nodes.get(n).getDistance());
        System.out.println(nodes.get(s).getDistance());
        System.out.println(nodes.get(e).getDistance());
        System.out.println(nodes.get(v).getDistance());
    }

    /**
     * Trenger ikke denne, veldig rart!!
     */
    private void resetDistance() {
        for (Node node: nodes) {
            node.setDistance(Integer.MAX_VALUE);
        }
    }


    /**
     *
     * @param start
     */
    public void findShortestDistanceToAll(Node start) {
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



    //Går fra endenoden til startnoden for å finne veien som ble gått.
    public List<Node> getShortestPath(Node target) {
        List<Node> reversedPath = new ArrayList<>();
        //Finner veien i reversert rekkefølge
        for (Node n = target; n != null; n = n.getPredeseccor() ) {
            reversedPath.add(n);
        }
        //Reverserer den reverserte veien for å få den riktig retning.
        List<Node> path = new ArrayList<>();
        for (int i = reversedPath.size() - 1; i >= 0 ; i--) {
            path.add(reversedPath.get(i));
        }
        return path;
    }
}