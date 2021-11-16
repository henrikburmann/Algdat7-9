package Øving9;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        Djikstra d = new Djikstra("src/Øving9/Files/Test/henrik_noder.txt", "src/Øving9/Files/Test/henrik_kanter.txt" );


        int fra = 0;
        int til = 4;


        // Tar utgangspunkt i en node og regner ut alle korteste distanser fra denne til de andre nodene
        d.findShortestDistance(d.getNodeFromList(fra));

        // Printer ut distansen fra startnoden øverst
        System.out.println("\nDistanse fra node " + fra + " til " + til  + " = " + d.getNodeFromList(til).getDistance());

        // printer ut path fra startnoden som man har definert øverst
        System.out.println(d.getShortestPath(d.getNodeFromList(til)));

        /*
        System.out.println();
        System.out.println("Distanse fra node 27872 til 34674: " + d.getNodeFromList(27873).getDistance());
        System.out.println();
        System.out.println(d.getShortestPath(d.getNodeFromList(34674)));



         */
    }
}