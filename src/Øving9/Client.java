package Øving9;

import java.io.File;
import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        File nodeEksempel = new File("src/Øving9/Files/nodeExample");
        File edgeEksempel = new File("src/Øving9" +
                "/Files/edgeExample");
        File nodeFile = new File("src/Øving9/Files/nodeFile");
        File edgeFile = new File("src/Øving9/Files/edgeFile");
        Djikstra d = new Djikstra(nodeFile.toString(), edgeFile.toString() );


        int fra = 0;
        int til = 4;


        // Tar utgangspunkt i en node og regner ut alle korteste distanser fra denne til de andre nodene
        d.findShortestDistance(d.getNodeFromList(fra), d.getNodeFromList(til));

        // Printer ut distansen fra startnoden øverst
        System.out.println("\nDistanse fra node " + fra + " til " + til  + " = " + d.getNodeFromList(til).getDistance());

        // printer ut path fra startnoden som man har definert øverst
        System.out.println(d.getShortestPath(d.getNodeFromList(til)));

    }
}