package Øving9;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        int fra = 6861306; // Trondheim
        //int fra = 3447384; // Stavanger
        //int fra = 5379848; // Snåsa

        int til = 2518118; // Oslo
        //int til = 136963; // Tampere
        //int til = 2951840; // Mehamn

        //Kjører først ALT-metoden
        ALT alt = new ALT(
                "src/Øving9/Files/Oppg/noder.txt",
                "src/Øving9/Files/Oppg/kanter.txt",
                "src/Øving9/Files/Oppg/interessepkt.txt");

        alt.readFromLandmarks();
        alt.readToLandmarks();

        printAnswer(fra, til, alt);




        //alt.generateFromNodeToLandmarkFile(5263302,2313313, 708400, 5486883);
        //alt.generateToNodeFromLandmarkFile(5263302,2313313, 708400, 5486883);


        //
        //System.out.println("Beregnet tid er: " + omregnTilTimer(alt.search(fra, til)) + "\n\n");
        //System.out.println("Koordinater: \n");
        //Node n = alt.getNodeFromList(til);
/*
        List<String> koordinater = alt.getPath(n);
        for (int i = 0; i < koordinater.size(); i++) {
            System.out.println(koordinater.get(i));
        }

 */
        //System.out.println("Fasit er: 17 timer, 39 minutter, 43 sekunder");
        }


        /*File nodeEksempel = new File("src/Øving9/Files/Oppg/noder.txt");
        File edgeEksempel = new File("src/Øving9/Files/Oppg/kanter.txt");

        //Kjører så Djikstra
        Djikstra d = new Djikstra(nodeEksempel.toString(), edgeEksempel.toString() );

        // Tar utgangspunkt i en node og regner ut alle korteste distanser fra denne til de andre nodene
        d.findShortestDistance(d.getNodeFromList(fra), d.getNodeFromList(til));
        // Printer ut distansen fra startnoden øverst

        System.out.println("\nDistanse fra node " + fra + " til " + til  + " = " + d.getNodeFromList(til).getDistance());
        System.out.println("Korteste vei fra Trondheim til Oslo: " + d.getShortestPath(d.getNodeFromList(til)));*/



        // Djikstra d = new Djikstra(nodeEksempel.toString(), edgeEksempel.toString() );
    //d.findShortestDistanceToAll(d.getNodeFromList(0));
    // printer ut path fra startnoden som man har definert øverst
    // System.out.println(d.getShortestPath(d.getNodeFromList(til)));
    //d.generateToStartFromLandmarkFile(5263302,2313313, 4102960, 1089301);
    //}

    // ALT alt = new ALT(nodeEksempel.toString(), edgeEksempel.toString());

    static void printAnswer(int start, int end, ALT alt) {
        Interessepkt startIntresse = alt.getInteressepkt(start);
        Interessepkt endInteresse = alt.getInteressepkt(end);

        int tid = alt.search(start, end);
        String time = omregnTilTimer((long) tid);

        System.out.println("tur: " + startIntresse.getName() + "-" + endInteresse.getName()
                + ", startnode " + start + ", endnode " + end + ", Antall besøkte: " + alt.getAmountVisited() +  ", " + time);
        /*
        System.out.println("Rutens koordinater: \n");
        List<String> koordinater = alt.getPath(alt.getNodeFromList(end));
        for (int i = 0; i < koordinater.size(); i++) {
            System.out.println(koordinater.get(i));
        }

         */

    }


    public static int omregnTilHundredel(int timer, int minutter, int sekunder){
        int svar = 0;
        svar += sekunder * 100;
        svar += minutter * 60 * 100;
        svar += timer * 60 * 100 * 60;
        return svar;
    }
    public static String omregnTilTimer(long hundredeler){
        int timer = (int) hundredeler / (100 * 60 * 60);
        int restTimer = (int) hundredeler % (100 * 60 * 60);

        int minutter = restTimer / (60 * 100);
        int restMinutter = restTimer % (60 * 100);

        int sekunder = restMinutter / 100;
        return "Timer: " + timer + ". Minutter: " + minutter + ". Sekunder: " + sekunder;
    }

}