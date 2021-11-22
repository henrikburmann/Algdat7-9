package Øving9;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
}
