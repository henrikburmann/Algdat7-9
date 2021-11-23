package Øving9;

import java.io.*;
import java.util.*;

public class ALT {
    public ArrayList<Node> nodes;
    public ArrayList<Edge> edges;
    public ArrayList<Interessepkt> interessepkts;


    Filehandler filehandler;
    Preproseccor preproseccor;

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

    public void preprocess(String fromNodeToLandmarkFile, String toNodeFromLandmarkFile, int n, int s, int e, int w) throws IOException {
        preproseccor = new Preproseccor(nodes, toLandmarks, fromLandmarks, fromNodeToLandmarkFile, toNodeFromLandmarkFile);
        preproseccor.generateFromNodeToLandmarkFile(n,s,e,w);
        preproseccor.generateToNodeFromLandmarkFile(n,s,e,w);
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