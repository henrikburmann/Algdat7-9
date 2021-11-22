package Øving9;

import java.io.*;
import java.util.*;

public class ALT {
    public ArrayList<Node> nodes;
    public ArrayList<Edge> edges;
    public ArrayList<Interessepkt> interessepkts;

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

        readFile(nodeFile, edgeFile, inpktFile);

        System.out.println("Antall noder: " + nodes.size());
        System.out.println("Antall kanter: " + edges.size());

        priorityQueue = new PriorityQueue<>(nodes.size(), new DistanceComprator());
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

        // leser interessepkt
        BufferedReader brInteressepkt = new BufferedReader(new FileReader(inpktFile));
        StringTokenizer stInteressepkt = new StringTokenizer(brInteressepkt.readLine());
        int intressepktSize = Integer.parseInt(stInteressepkt.nextToken());

        for (int i = 0; i < intressepktSize; i++) {
            stInteressepkt = new StringTokenizer(brInteressepkt.readLine());
            int nodeNumber = Integer.parseInt(stInteressepkt.nextToken());
            int type = Integer.parseInt(stInteressepkt.nextToken());
            String name = stInteressepkt.nextToken();

            // legger til alle interessepktene
            // todo: Må finne en måte å koble interessepkt opp mot
            interessepkts.add(new Interessepkt(nodeNumber, type, name));

        }

        addNeigbours();
        addOppoNeigbours();
        //  readLandmarkDistances();
    }

    /**
     * @throws IOException Read all distances from each landmark to every node
     */
    public void readFromLandmarks() throws IOException {
        BufferedReader bfToNode = new BufferedReader(new FileReader("src/Øving9/Files/outfiles/from_landmark_to_node.txt"));
        StringTokenizer stToNode = null;
        for (int i = 0; i < 4; i++) {
            readFromLandmarks.add(new int[nodes.size()]);
        }
        for (int i = 0; i < nodes.size(); i++) {
            // må finne en måte å se om man kan lese neste linje
            stToNode = new StringTokenizer(bfToNode.readLine());

            for (int j = 0; j < 4; j++) {

                if (!stToNode.hasMoreTokens()) {
                    break;
                }
                String token = stToNode.nextToken();
                readFromLandmarks.get(j)[i] = Integer.parseInt(token);
            }
        }
        bfToNode.close();

        int s = 0;
        for (int i = 0; i < 4; i++) {
            s += readFromLandmarks.get(i).length;
        }
        System.out.println("Antall ting og sånn er: " + s);
    }

    /**
     * @throws IOException Read all distances to each landmark from every node
     */
    public void readToLandmarks() throws IOException {
        BufferedReader bfToNode = new BufferedReader(new FileReader("src/Øving9/Files/outfiles/from_node_to_landmarks.txt"));
        StringTokenizer stToNode = null;
        for (int i = 0; i < 4; i++) {
            readtoLandmarks.add(new int[nodes.size()]);
        }
        for (int i = 0; i < nodes.size(); i++) {
            // ikke riktig størrelse
            String next = bfToNode.readLine().trim();
            if (next.isEmpty()) {
                bfToNode.close();
            } else {
                stToNode = new StringTokenizer(next);
            }

            for (int j = 0; j < 4; j++) {
                readtoLandmarks.get(j)[i] = Integer.parseInt(stToNode.nextToken());
            }
        }
        bfToNode.close();
        int s = 0;
        for (int i = 0; i < 4; i++) {
            s += readtoLandmarks.get(i).length;
        }
        System.out.println("Antall ting og sånn er til landemerkene: " + s);
    }


    // liten test bare for å se at den lagrer distansene i fromLandmark eller toLandmark
    public void printLandmarks(int landmark, ArrayList<int[]> list) {
        if (landmark < 0 || landmark > 3) {
            landmark = 0;
        }
        int[] li = list.get(landmark);
        for (int i = 0; i < li.length; i++) {
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

    // for å hente ut spesifik node fra en index i nodes
    public Node getNodeFromList(int index) {
        return nodes.get(index);
    }

    public int getAmountVisited() {
        return amountVisited;
    }

    /**
     * Lager en fil med distansen fra startnodene til
     * alle landemerkene som man har definert
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
     *              Tar inn en startnode og finner avstand
     *              fra node til start
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
    public int landmarkEstimate(int from, int to, int landmark) {
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
            tempEstimate = landmarkEstimate(from, to, i); // landmark
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
    public int search(int startNode, int endNode) {
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
            /*
            if (polled.equals(endNode)) {
                return; // we have reached the end
            }
             */

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

    // henter ut interessepkt basert på nodenummer
    public Interessepkt getInteressepkt(int number) {
        for (int i = 0; i < interessepkts.size() ; i++) {
            if (interessepkts.get(i).getNodeNumber() == number) {
                return interessepkts.get(i);
            }
        }
        return null;
    }
}