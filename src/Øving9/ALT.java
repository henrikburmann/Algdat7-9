package Øving9;

import java.io.*;
import java.util.*;

public class ALT {
    public ArrayList<Node> nodes;
    public ArrayList<Edge> edges;
    public ArrayList<Interessepkt> interessepkts;


    Filehandler filehandler;

    public ArrayList<int[]> toLandmarks = new ArrayList<>();
    public ArrayList<int[]> fromLandmarks = new ArrayList<>();

    public ArrayList<int[]> readtoLandmarks = new ArrayList<>();
    public ArrayList<int[]> readFromLandmarks = new ArrayList<>();

    private final PriorityQueue<Node> priorityQueue;

    private int amountVisited;

    public ALT(String nodeFile, String edgeFile, String inpktFile) throws IOException {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        interessepkts = new ArrayList<>();

        // Leser av filene til oppgaven
        filehandler = new Filehandler(nodes, edges, interessepkts);

        filehandler.readNodes(nodeFile);
        filehandler.readEdges(edgeFile);
        filehandler.readIntrestPoints(inpktFile);

        // legger til nabo nodene til hver node
        addNeigbours();
        addOppoNeigbours();

        System.out.println("Antall noder: " + nodes.size());
        System.out.println("Antall kanter: " + edges.size());

        priorityQueue = new PriorityQueue<>(nodes.size(), new DistanceComprator());
    }

    public void readNodeLandmarkData() throws IOException {

        filehandler.readFromLandmarks("src/Øving9/Files/outfiles/from_landmark_to_node.txt", readFromLandmarks);
        filehandler.readToLandmarks("src/Øving9/Files/outfiles/from_node_to_landmarks.txt", readtoLandmarks);

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
     * Legger naboer til node
     * når man skal bruke reverse graph
     *
     * @return
     */
    private void addOppoNeigbours() {
        for (Edge e : edges) {
            Node n = e.getTo();
            n.addOppoNeigbour(e);
        }
    }

    // for å hente ut spesifik node fra en index i nodes liste
    public Node getNodeFromList(int index) {
        return nodes.get(index);
    }

    public int getAmountVisited() {
        return amountVisited;
    }

    /**
     * Lager en fil med distansen fra startnodene
     * til alle landemerkene
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
        pw.close();
    }

    /**
     * @throws FileNotFoundException
     * Lager en fil med distansen til startnode
     * fra alle landemerkene
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
        pw.close();
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
     * @param start Tar inn en startnode og finner avstand
     *              til node fra start
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
     * @param start method from Dijkstra
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

    //
    public int estimateLandmark(int from, int to, int landmark) {
        int landmarkToCurrent = readFromLandmarks.get(landmark)[from];
        int landmarkToTarget = readFromLandmarks.get(landmark)[to];

        int currentToLandmark = readtoLandmarks.get(landmark)[from];
        int targetToLandmark = readtoLandmarks.get(landmark)[to];

        // if negative calculation, set r1 and r2 to zero
        int r1 = Math.max(landmarkToTarget - landmarkToCurrent, 0);
        int r2 = Math.max(currentToLandmark - targetToLandmark, 0);

        return Math.max(r1, r2);
    }


    public int findEstimate(int from, int to) {
        int estimate = -1;
        int tempEstimate = -1;
        int length = 4; // hardkodet, avhengig av hvor mye landemerker som brukes

        for (int i = 0; i < length; i++) {
            tempEstimate = estimateLandmark(from, to, i); // landmark
            if (tempEstimate > estimate) {
                estimate = tempEstimate;
            }
        }
        return estimate;
    }

    /**
     * @param startNode
     * @param endNode
     * @return distance from startnode to endnode
     * ALT search (djikstra with landmarks)
     */
    public int altSearch(int startNode, int endNode) {
        priorityQueue.clear();
        reset();
        amountVisited = 0;
        Node start = getNodeFromList(startNode);
        Node end = getNodeFromList(endNode);
        start.setDistance(0);
        priorityQueue.add(start);

        while (!priorityQueue.isEmpty()) {

            Node polled = priorityQueue.poll();
            amountVisited++;
            polled.setVisisted(true);
            polled.setEnQueued(false);

            for (Edge edge : polled.getAdjList()) {
                Node toNode = edge.getTo();
                int newDistance = polled.getDistance() + edge.getWeight();

                if (newDistance < toNode.getDistance()) {
                    toNode.setDistance(newDistance);
                    toNode.setPredeseccor(polled); // set forgjenger
                    if (!toNode.isVisisted()) {
                        priorityQueue.remove(toNode);

                        if (toNode.getEstimatedDistance() == -1) {
                            int estimate = findEstimate(toNode.number, end.number);
                            toNode.setEstimatedDistance(estimate);
                        }
                        priorityQueue.add(toNode);
                        toNode.setEnQueued(true);
                    }
                }
                if (!toNode.isVisisted() && !toNode.isEnQueued()) {
                    priorityQueue.add(toNode);
                    toNode.setEnQueued(true);
                }
            }
            if (end.isVisisted()) {
                System.out.println("Antall besøkte: " + amountVisited);
                break;
            }
        }
        // distance from start to endnode
        return end.getDistance();
    }

    public List<String> getPath(Node target) {
        List<Node> reversedPath = new ArrayList<>();
        //Finner veien i reversert rekkefølge
        for (Node n = target; n != null; n = n.getPredeseccor()) {
            reversedPath.add(n);
        }
        //Reverserer den reverserte veien for å få den riktig retning.
        List<Node> path = new ArrayList<>();
        for (int i = reversedPath.size() - 1; i >= 0; i--) {
            path.add(reversedPath.get(i));
        }
        System.out.println("ALT: Antall noder i pathen: " + path.size());
        List<String> koordinater = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            koordinater.add(path.get(i).writeCoordinates());
        }
        return koordinater;
    }

    class DistanceComprator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            // o1.findsumDistance, o2.findSumDistance
            return o1.sumDistance() - o2.sumDistance();
        }
    }

    // henter ut interessepktet basert på nodenummer, (kan ikke bruke index..)
    public Interessepkt getInteressepkt(int number) {
        for (int i = 0; i < interessepkts.size() ; i++) {
            if (interessepkts.get(i).getNodeNumber() == number) {
                return interessepkts.get(i);
            }
        }
        return null;
    }
}