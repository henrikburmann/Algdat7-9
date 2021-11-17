package Øving9;

import java.io.File;
import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        File nodeEksempel = new File("src/Øving9/Files/Oppg/noder.txt");
        File edgeEksempel = new File("src/Øving9" +
                "/Files/Oppg/kanter.txt");

        //File nodeFile = new File("src/Øving9/Files/nodeFile");
        //File edgeFile = new File("src/Øving9/Files/edgeFile");

/*
        int fra = 0;
        int til = 5263302; // bergen
        // ALT alt = new ALT(nodeEksempel.toString(), edgeEksempel.toString());
        Djikstra d = new Djikstra(nodeEksempel.toString(), edgeEksempel.toString() );
        // Tar utgangspunkt i en node og regner ut alle korteste distanser fra denne til de andre nodene
        d.findShortestDistance(d.getNodeFromList(fra), d.getNodeFromList(til));
        // Printer ut distansen fra startnoden øverst
        System.out.println("\nDistanse fra node " + fra + " til " + til  + " = " + d.getNodeFromList(til).getDistance());

        System.out.println("======================================");
        fra = 0;
        til = 2313313; // alta
        // ALT alt = new ALT(nodeEksempel.toString(), edgeEksempel.toString());
        d = new Djikstra(nodeEksempel.toString(), edgeEksempel.toString() );
        // Tar utgangspunkt i en node og regner ut alle korteste distanser fra denne til de andre nodene
        d.findShortestDistance(d.getNodeFromList(fra), d.getNodeFromList(til));
        // Printer ut distansen fra startnoden øverst
        System.out.println("\nDistanse fra node " + fra + " til " + til  + " = " + d.getNodeFromList(til).getDistance());
        System.out.println("==========================");
        fra = 0;
        til = 4102960; // esbjerg
        // ALT alt = new ALT(nodeEksempel.toString(), edgeEksempel.toString());
        d = new Djikstra(nodeEksempel.toString(), edgeEksempel.toString() );
        // Tar utgangspunkt i en node og regner ut alle korteste distanser fra denne til de andre nodene
        d.findShortestDistance(d.getNodeFromList(fra), d.getNodeFromList(til));
        // Printer ut distansen fra startnoden øverst
        System.out.println("\nDistanse fra node " + fra + " til " + til  + " = " + d.getNodeFromList(til).getDistance());
        System.out.println("=============================");
        fra = 0;
        til = 1089301; // joensuu, Finland
        // ALT alt = new ALT(nodeEksempel.toString(), edgeEksempel.toString());
        d = new Djikstra(nodeEksempel.toString(), edgeEksempel.toString() );
        // Tar utgangspunkt i en node og regner ut alle korteste distanser fra denne til de andre nodene
        d.findShortestDistance(d.getNodeFromList(fra), d.getNodeFromList(til));
        // Printer ut distansen fra startnoden øverst
        System.out.println("\nDistanse fra node " + fra + " til " + til  + " = " + d.getNodeFromList(til).getDistance());


 */

        int fra = 0;
        // ALT alt = new ALT(nodeEksempel.toString(), edgeEksempel.toString());
        Djikstra d = new Djikstra(nodeEksempel.toString(), edgeEksempel.toString() );
        d.findShortestDistanceToAll(d.getNodeFromList(0));
        // printer ut path fra startnoden som man har definert øverst
        // System.out.println(d.getShortestPath(d.getNodeFromList(til)));
        d.generateToStartFromLandmarkFile(5263302,2313313, 4102960, 1089301);

    }
}