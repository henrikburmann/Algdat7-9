package Øving9;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Preproseccor {

    private ArrayList<Node> nodes;
    private ArrayList<int[]> toLandmarks;
    private ArrayList<int[]> fromLandmarks;
    private String fromNodeToLandmarkFile;
    private String toNodeFromLandmarkFile;

    public Preproseccor(ArrayList<Node> nodes, ArrayList<int[]> toLandmarks, ArrayList<int[]> fromLandmarks, String fromNodeToLandmarkFile, String toNodeFromLandmarkFile) {
        this.nodes = nodes;
        this.toLandmarks = toLandmarks;
        this.fromLandmarks = fromLandmarks;
        this.fromNodeToLandmarkFile = fromNodeToLandmarkFile;
        this.toNodeFromLandmarkFile = toNodeFromLandmarkFile;
    }

    /**
     * Lager en fil med distansen fra startnodene
     * til alle landemerkene
     */
    public void generateFromNodeToLandmarkFile(int n, int s, int e, int w) throws IOException {
        FileWriter fileWriter = new FileWriter(fromNodeToLandmarkFile);
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
        FileWriter outFile = new FileWriter(toNodeFromLandmarkFile);
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
}