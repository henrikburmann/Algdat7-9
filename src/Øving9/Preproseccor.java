package Øving9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Preproseccor {


    public ArrayList<int[]> readtoLandmarks;
    public ArrayList<int[]> readFromLandmarks;

    private ArrayList<Node> nodes;

    public Preproseccor(ArrayList<int[]> readtoLandmarks, ArrayList<int[]> readFromLandmarks, ArrayList<Node> nodes) {
        this.readtoLandmarks = readtoLandmarks;
        this.readFromLandmarks = readFromLandmarks;
        this.nodes = nodes;
    }

    /**
     * @throws IOException Read all distances from each landmark to every node
     */
    public void readFromLandmarks() throws IOException {
        BufferedReader bfToNode = new BufferedReader(new FileReader("src/Øving9/Files/outfiles/from_landmark_to_node.txt"));
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

        for (int i = 0; i < 4; i++) { // 4 landemerker, litt hardkodet
            readtoLandmarks.add(new int[nodes.size()]);
        }
        for (int i = 0; i < nodes.size(); i++) {
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
}