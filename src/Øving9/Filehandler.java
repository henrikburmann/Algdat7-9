package Øving9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Filehandler {

    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private ArrayList<Interessepkt> interessepkts;

    public Filehandler(ArrayList<Node> nodes, ArrayList<Edge> edges, ArrayList<Interessepkt> interessepkts) {

        this.nodes = nodes;
        this.edges = edges;
        this.interessepkts = interessepkts;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public ArrayList<Interessepkt> getInteressepkts() {
        return interessepkts;
    }

    public void readNodes(String nodePath) {
        try {
            // Leser noder
            BufferedReader brNodes = new BufferedReader(new FileReader(nodePath));
            StringTokenizer stNodes = new StringTokenizer(brNodes.readLine());
            int size = Integer.parseInt(stNodes.nextToken());

            for (int i = 0; i < size; i++) {
                stNodes = new StringTokenizer(brNodes.readLine());
                int number = Integer.parseInt(stNodes.nextToken());
                double longitude = Double.parseDouble(stNodes.nextToken());
                double latitude = Double.parseDouble(stNodes.nextToken());
                nodes.add(new Node(number, longitude, latitude));
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void readEdges(String edgePath) {
        // Leser kanter
        try {

            BufferedReader brEdges = new BufferedReader(new FileReader(edgePath));
            StringTokenizer stEdges = new StringTokenizer(brEdges.readLine());
            int size = Integer.parseInt(stEdges.nextToken());

            for (int i = 0; i < size; i++) {
                stEdges = new StringTokenizer(brEdges.readLine());
                Node from = nodes.get(Integer.parseInt(stEdges.nextToken()));
                Node to = nodes.get(Integer.parseInt(stEdges.nextToken()));
                int weight = Integer.parseInt(stEdges.nextToken());
                edges.add(new Edge(from, to, weight));
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void readIntrestPoints(String intressepktPath) {
        // Leser interessepkt

        try {
            BufferedReader brInteressepkt = new BufferedReader(new FileReader(intressepktPath));
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
        } catch (IOException e) {
            e.getMessage();
        }
    }

    /**
     * @throws IOException Read all preprosecced distances from each landmark to every node
     */
    public void readFromLandmarks(String path, ArrayList<int[]> readFromLandmarks) throws IOException {
        BufferedReader bfToNode = new BufferedReader(new FileReader(path));
        StringTokenizer stToNode;
        for (int i = 0; i < 4; i++) {
            readFromLandmarks.add(new int[nodes.size()]);
        }
        for (int i = 0; i < nodes.size(); i++) {
            stToNode = new StringTokenizer(bfToNode.readLine());

            for (int j = 0; j < 4; j++) { // 4 landemerker, litt hardkodet
                if (!stToNode.hasMoreTokens()) {
                    break;
                }
                String token = stToNode.nextToken();
                readFromLandmarks.get(j)[i] = Integer.parseInt(token);
            }
        }
        bfToNode.close();
    }

    /**
     * @throws IOException Read all preprosecced distances to each landmark from every node
     */
    public void readToLandmarks(String path, ArrayList<int[]> readtoLandmarks) throws IOException {
        BufferedReader bfToNode = new BufferedReader(new FileReader(path));
        StringTokenizer stToNode = null;

        for (int i = 0; i < 4; i++) { // 4 landemerker, litt hardkodet
            readtoLandmarks.add(new int[nodes.size()]);
        }
        for (int i = 0; i < nodes.size(); i++) {
            String next = bfToNode.readLine();
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
    }
}