package Øving9;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;


public class ALT {
    public ArrayList<Node> nodes;
    public ArrayList<Edge> edges;

    public ArrayList<int[]> toLandmarks = new ArrayList<>();
    public ArrayList<int[]> fromLandmarks = new ArrayList<>();



    public ALT(String nodeFile, String edgeFile, String inpktFile) throws IOException {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        //toLandmarks = new ArrayList<>();
        //fromLandmarks = new ArrayList<>();

        readFile(nodeFile, edgeFile, inpktFile);

        System.out.println("Antall noder: " + nodes.size());
        System.out.println("Antall kanter: " + edges.size());
    }

    public void readFile(String nodeFile, String edgeFile, String inpktFile) throws IOException {
        // Leser noder
        BufferedReader brNodes = new BufferedReader(new FileReader(nodeFile));
        StringTokenizer stNodes = new StringTokenizer(brNodes.readLine());


        int size = Integer.parseInt(stNodes.nextToken());

        for (int i = 0; i < size; i++) {
            stNodes = new StringTokenizer(brNodes.readLine());
            int number = Integer.parseInt(stNodes.nextToken());
            double longitude = Double.parseDouble(stNodes.nextToken());
            double latitude = Double.parseDouble(stNodes.nextToken());
            nodes.add(new Node(number, longitude, latitude));
        }

        //Leser kanter
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

        // leser fil som består av interessepunkter
        BufferedReader interessePkt = new BufferedReader(new FileReader(inpktFile));
        StringTokenizer stInpkt = new StringTokenizer(interessePkt.readLine());

        addNeigbours();
        addOppoNeigbours();
    }

    public void findShortestDistance(int startNode){

    }


    /**
     * legger til alle nabo noder til nodene
     */
    private void addNeigbours() {
        for (Edge e : edges) {
            // Fra noden til en enge
            Node n = e.getFrom();
            // Legger til edge i vedsiden av listen til node
            // NB: alle noder betår av en vedsiden av liste (adj_list)
            n.addNeigbour(e);

        }
    }

    /**
     * Får sjå om det funker
     *
     * @return
     */
    private void addOppoNeigbours() {
        for (Edge e : edges) {
            Node n = e.getTo();
            n.addOppoNeigbour(e);
        }
    }

    // for å hente ut spesifik node fra en index
    public Node getNodeFromList(int index) {
        return nodes.get(index);
    }


    /**
     * Lager en fil med distansen fra startnodene til
     * alle landemerkene som man har definert
     * Todo: skal man bruke .csv eller .txt?? finner ut av det senere
     */
    public void generateFromNodeToLandmarkFile(int n, int s, int e, int w) throws IOException {
        FileWriter fileWriter = new FileWriter("src/Øving9/Files/outfiles/from_node_to_landmark.txt");
        PrintWriter pw = new PrintWriter(fileWriter);
        Node north = nodes.get(n);
        Node south = nodes.get(s);
        Node east = nodes.get(e);
        Node west = nodes.get(w);

        Node[] landmarks = {north, south, east, west};

        for (int i = 0; i < landmarks.length; i++) {
            landmarks[i].setDistance(0);
            int[] distances = findShortestDistanceFromAll(landmarks[i]);
            toLandmarks.add(distances);
        }

        for (int j = 0; j < nodes.size(); j++) {
            pw.write(toLandmarks.get(0)[j] + "," + toLandmarks.get(1)[j] + "," + toLandmarks.get(2)[j] + "," + toLandmarks.get(3)[j]);
            pw.println(" ");
        }
    }


    /**
     * @throws FileNotFoundException
     */
    public void generateToNodeFromLandmarkFile(int n, int s, int e, int w) throws IOException {

        FileWriter outFile = new FileWriter("src/Øving9/Files/outfiles/test.txt");
        PrintWriter pw = new PrintWriter(outFile);

        System.out.println("Størrelse på nodeliste: " + nodes.size());
        Node north = nodes.get(n);
        Node south = nodes.get(s);
        Node east = nodes.get(e);
        Node west = nodes.get(w);

        Node[] landmarks = {north, south, east, west};

        for (int i = 0; i < landmarks.length; i++) {
            landmarks[i].setDistance(0);
            int[] distances = findShortestDistanceToAll(landmarks[i]);
            fromLandmarks.add(distances);
        }

        for (int j = 0; j < nodes.size(); j++) {
            pw.write(fromLandmarks.get(0)[j] + "," + fromLandmarks.get(1)[j] + "," + fromLandmarks.get(2)[j] + "," + fromLandmarks.get(3)[j]);
            pw.println(" ");
        }
    }

    /**
     * Trenger ikke denne, veldig rart!!
     */
    private void reset() {
        for (Node node : nodes) {
            node.setDistance(Integer.MAX_VALUE);
            node.setVisisted(false);
        }
    }

    /**
     * @param start
     */
    private int[] findShortestDistanceToAll(Node start) {
        reset();
        start.setDistance(0); // start have cost 0

        PriorityQueue<Node> queue = new PriorityQueue<>();

        queue.add(start);
        start.setVisisted(true);

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            for (Edge e : current.getAdjList()) {

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
        int[] distances = new int[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            distances[i] = nodes.get(i).getDistance();
        }
        return distances;
    }


    /**
     * @param start
     */
    private int[] findShortestDistanceFromAll(Node start) {
        reset();
        start.setDistance(0); // start have cost 0

        PriorityQueue<Node> queue = new PriorityQueue<>();

        queue.add(start);
        start.setVisisted(true);

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            for (Edge e : current.getOppositeAdjList()) {

                Node n = e.getFrom();

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
        int[] distances = new int[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            distances[i] = nodes.get(i).getDistance();
        }
        return distances;
    }
}
