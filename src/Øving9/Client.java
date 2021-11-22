package Øving9;

import java.io.File;
import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        File nodeEksempel = new File("src/Øving9/Files/Oppg/noder.txt");
        File edgeEksempel = new File("src/Øving9/Files/Oppg/kanter.txt");


        ALT lat = new ALT(
                "src/Øving9/Files/Oppg/noder.txt",
                "src/Øving9/Files/Oppg/kanter.txt",
                "src/Øving9/Files/Oppg/interessepkt.txt");

        //lat.generateFromNodeToLandmarkFile(5263302,2313313, 708400, 5486883);
        //lat.generateToNodeFromLandmarkFile(5263302,2313313, 708400, 5486883);

        lat.readFromLandmarks();
        lat.readToLandmarks();



        System.out.println("Beregnet tid er: " + lat.search(6861306, 2518118));
        System.out.println("Fasit er: " + omregn(5, 55, 33));




        int fra = 6861306; // Trondheim

        int til = 2518118; // Oslo

        Djikstra d = new Djikstra(nodeEksempel.toString(), edgeEksempel.toString() );

        // Tar utgangspunkt i en node og regner ut alle korteste distanser fra denne til de andre nodene
        d.findShortestDistance(d.getNodeFromList(fra), d.getNodeFromList(til));
        // Printer ut distansen fra startnoden øverst
        System.out.println("Fasit er: " + omregn(5, 55, 33));
        System.out.println("\nDistanse fra node " + fra + " til " + til  + " = " + d.getNodeFromList(til).getDistance());

        System.out.println(d.getShortestPath(d.getNodeFromList(5263302)));





        // ALT alt = new ALT(nodeEksempel.toString(), edgeEksempel.toString());
        // Djikstra d = new Djikstra(nodeEksempel.toString(), edgeEksempel.toString() );
        //d.findShortestDistanceToAll(d.getNodeFromList(0));
        // printer ut path fra startnoden som man har definert øverst
        // System.out.println(d.getShortestPath(d.getNodeFromList(til)));
        //d.generateToStartFromLandmarkFile(5263302,2313313, 4102960, 1089301);
    }
    public static int omregn(int timer, int minutter, int sekunder){
        int svar = 0;
        svar += sekunder * 100;
        svar += minutter * 60 * 100;
        svar += timer * 60 * 100 * 60;
        return svar;
    }
}