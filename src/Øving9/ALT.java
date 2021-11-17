package Øving9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/* Litt notater til oppgaven :)

    Samme som A* bare at vi også tar i bruk Landemerker.

    Landemerker:
        - noder hvor vi beregner:
            1. Avstand til alle noder (Dijkstra)
            2. Avstand fra alle andre noder (Dijkstra på omv.graf)
        - Må gjøre en preprossesering av kartet der vi lagrer dataene i en fil (hvordan skal det se ut?)
        - bør ligge spredt rundt kanten på kartet??

        ALT er bedre når man hele tiden har preprosseseringen.

        3 - 8 landemerker.

        Todo:
            1. hvordan skal man velge landemerker? (burde være ytterst)
            2. avstandsregnign
            3. programmet skal vise hvor lang reiseruta er

            noder:
            number  longitude   latitude

            kanter:
            from    to  weight  ?   ?

            interessepkt: (bruke for å finne nodenr til kjente kryss)
            nodenr  kode    "Navn på stedet"

            kode: 1 for stedsnavn eks "Trondheim", 2 for bensinstasjon eks "Shell Herlev"

        nodenr samme som nr. i nodefila

        Todo: finne ut hvordan filtype man skal lagre preprosseseringen i (CSV)



             */

public class ALT {
    public ArrayList<Node> nodes;
    public ArrayList<Edge> edges;



    public ALT(String nodeFile, String edgeFile, String inpktFile) throws IOException {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();

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


}
