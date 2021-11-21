package Øving9;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;


public class ALT {
    public ArrayList<Node> nodes;
    public ArrayList<Edge> edges;

    public ArrayList<int[]> toLandmarks = new ArrayList<>();
    public ArrayList<int[]> fromLandmarks = new ArrayList<>();

    public ArrayList<int[]> readtoLandmarks = new ArrayList<>();
    public ArrayList<int[]> readFromLandmarks = new ArrayList<>();

    private final PriorityQueue<Node> priorityQueue = new PriorityQueue<>(nodes.size(), new DistanceComprator()); // se på denne litt mer


    public ALT(String nodeFile, String edgeFile, String inpktFile) throws IOException {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();

        readFile(nodeFile, edgeFile, inpktFile);
        System.out.println("Antall noder: " + nodes.size());
        System.out.println("Antall kanter: " + edges.size());
    }

    public ArrayList<int[]> getToLandmarks() {
        return toLandmarks;
    }

    public ArrayList<int[]> getFromLandmarks() {
        return fromLandmarks;
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
        // todo: fullføre denne senere
        BufferedReader interessePkt = new BufferedReader(new FileReader(inpktFile));
        StringTokenizer stInpkt = new StringTokenizer(interessePkt.readLine());

        addNeigbours();
        addOppoNeigbours();
    //  readLandmarkDistances();
    }

    /**
     * @throws IOException
     * Read all distances from each landmark to every node
     */
    public void readFromLandmarks() throws IOException{
        BufferedReader bfToNode = new BufferedReader(new FileReader("src/Øving9/Files/outfiles/to_node_from_landmarks.txt"));
        StringTokenizer stToNode;
        for (int i = 0; i < 4; i++) {
            readFromLandmarks.add(new int[nodes.size()]);
        }
        for (int i = 0; i < nodes.size(); i++) {
            stToNode = new StringTokenizer(bfToNode.readLine());
            for (int j = 0; j < 4; j++) {
                readFromLandmarks.get(j)[i] = Integer.parseInt(stToNode.nextToken());
                int hei = readFromLandmarks.get(j)[i];
                System.out.println(hei);

            }
        }
        int s = 0;
        for (int i = 0; i < 4; i++) {
            s += readtoLandmarks.get(i).length;
        }
    }

    /**
     * @throws IOException
     * Read all distances to each landmark from every node
     */
    public void readToLandmarks() throws IOException{
        BufferedReader bfToNode = new BufferedReader(new FileReader("src/Øving9/Files/outfiles/from_node_to_landmarks.txt"));
        StringTokenizer stToNode;
        for (int i = 0; i < 4; i++) {
            readFromLandmarks.add(new int[nodes.size()]);
        }
        for (int i = 0; i < nodes.size(); i++) {
            stToNode = new StringTokenizer(bfToNode.readLine());
            for (int j = 0; j < 4; j++) {
                readFromLandmarks.get(j)[i] = Integer.parseInt(stToNode.nextToken());
                int hei = readFromLandmarks.get(j)[i];
                System.out.println(hei);

            }
        }
        int s = 0;
        for (int i = 0; i < 4; i++) {
            s += readtoLandmarks.get(i).length;
        }
    }


    // liten test bare for å se at den lagrer distansene i fromLandmark eller toLandmark
    public void printLandmarks(int landmark, ArrayList<int[]> list) {
        if (landmark < 0 || landmark > 3) {
            landmark = 0;
        }
        int[] li = list.get(landmark);
        for (int i = 0; i < li.length ; i++) {
            System.out.println(li[landmark]);
        }
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
     * @return
     */
    private void addOppoNeigbours() {
        for (Edge e : edges) {
            Node n = e.getTo();
            n.addOppoNeigbour(e);
        }
    }

    // for å hente ut spesifik node fra en index i nodes
    public Node getNodeFromList(int index) {
        return nodes.get(index);
    }


    /**
     * Lager en fil med distansen fra startnodene til
     * alle landemerkene som man har definert
     * Todo: skal man bruke .csv eller .txt?? finner ut av det senere
     */
    public void generateFromNodeToLandmarkFile(int n, int s, int e, int w) throws IOException {
        FileWriter fileWriter = new FileWriter("src/Øving9/Files/outfiles/from_node_to_landmarks.txt");
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
            pw.write(toLandmarks.get(0)[j] + " "
                    + toLandmarks.get(1)[j] + " "
                    + toLandmarks.get(2)[j] + " "
                    + toLandmarks.get(3)[j]);
            pw.println();
        }
    }

    /**
     * @throws FileNotFoundException
     */
    public void generateToNodeFromLandmarkFile(int n, int s, int e, int w) throws IOException {
        FileWriter outFile = new FileWriter("src/Øving9/Files/outfiles/from_landmark_to_node.txt");
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
            pw.write(fromLandmarks.get(0)[j] + " "
                    + fromLandmarks.get(1)[j] + " "
                    + fromLandmarks.get(2)[j] + " "
                    + fromLandmarks.get(3)[j]);
            pw.println();
        }
    }

    /**
     * trenger å resette data for nodene hver gang
     * hvis man kjører skal kjøre korteste vei metoden
     * flere ganger
     */
    private void reset() {
        for (Node node : nodes) {
            node.setDistance(Integer.MAX_VALUE);
            node.setVisisted(false);
        }
    }

    /**
     * @param start
     * Tar inn en startnode og finner avstand
     * til node fra start
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
     * method from Dijkstra
     * Tar inn en startnode og finner avstand
     * fra node til start
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


    // estimate distance from node n to target node
    public int landmarkEstimate(int from, int to, int landmark) {
        int landmarkToCurrent = fromLandmarks.get(landmark)[from];
        int landmarkToTarget = fromLandmarks.get(landmark)[to];

        int currentToLandmark = toLandmarks.get(landmark)[from];
        int targetToLandmark = toLandmarks.get(landmark)[to];

        // if negative calculation, set r1 and r2 to zero
        int r1 = Math.max(landmarkToTarget - landmarkToCurrent, 0);
        int r2 = Math.max(currentToLandmark - targetToLandmark, 0);

        return Math.max(r1, r2);
    }

    public int findEstimate(int from, int to) {
        int estimate = -1;
        int tempEstimate = -1;
        // lengde for landemerke 1 i preprosecced, alle landemerkene har uansett samme lengde, trur jeg??
        int length = fromLandmarks.get(0).length;

        for (int i = 0; i < length  ; i++) {
            tempEstimate = landmarkEstimate(from, to, i); //
            if (tempEstimate > estimate){
                estimate = tempEstimate;
            }
        }
        return estimate;
    }


    public int search() {
        return -1;
    }

    // Ja må se på denne her litt senere
    class DistanceComprator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.getDistance() - o2.getDistance();
        }
    }
}