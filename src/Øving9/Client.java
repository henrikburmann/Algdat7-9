package Øving9;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {

        String nodePath = "src/Øving9/Files/Oppg/noder.txt";
        String edgePath = "src/Øving9/Files/Oppg/kanter.txt";
        String intressepktPath = "src/Øving9/Files/Oppg/interessepkt.txt";

        // Kjører ALT algoritme
        ALT alt = new ALT(nodePath, edgePath, intressepktPath);

        // preprocess filer, trenger ikke å kjøre disse hver gang
        //alt.generateFromNodeToLandmarkFile(5263302,2313313, 708400, 5486883);
        //alt.generateToNodeFromLandmarkFile(5263302,2313313, 708400, 5486883);

        int fra = 6861306; // Trondheim
        //int fra = 3447384; // Stavanger
        //int fra = 5379848; // Snåsa

        int til = 2518118; // Oslo
        //int til = 136963; // Tampere
        //int til = 2951840; // Mehamn

        // leser av preprosecced
        alt.preprocess();

        // printer svar for ALT
        printAnswer(fra, til, alt);


        //Kjører så Djikstra algoritme
        System.out.println("\n\n========== Dijkstra ==============");
        Djikstra d = new Djikstra(nodePath, edgePath);

        d.findShortestDistance(d.getNodeFromList(fra), d.getNodeFromList(til));
        // Printer ut distansen fra startnoden øverst

        System.out.println("\nDistanse fra node " + fra + " til " + til + " = " + d.getNodeFromList(til).getDistance());

        // printer ut rekkefølge av nodene som er kortest vei
        System.out.println("Korteste vei fra Trondheim til Oslo: " + d.getShortestPath(d.getNodeFromList(til)));

    }


    // printer ut svar for ALT på riktig format
    static void printAnswer(int start, int end, ALT alt) {
        Interessepkt startIntresse = alt.getInteressepkt(start);
        Interessepkt endInteresse = alt.getInteressepkt(end);

        int tid = alt.altSearch(start, end);
        String time = omregnTilTimer(tid);

        System.out.println("tur: " + startIntresse.getName() + "-" + endInteresse.getName()
                + ", startnode " + start + ", endnode " + end + ", Antall besøkte: " + alt.getAmountVisited() + ", " + time);

        // printer ut koordinatene til ruten
        /*
        System.out.println("Rutens koordinater: \n");
        List<String> koordinater = alt.getPath(alt.getNodeFromList(end));
        for (int i = 0; i < koordinater.size(); i++) {
            System.out.println(koordinater.get(i));
        }
        */
    }

    public static int omregnTilHundredel(int timer, int minutter, int sekunder) {
        int svar = 0;
        svar += sekunder * 100;
        svar += minutter * 60 * 100;
        svar += timer * 60 * 100 * 60;
        return svar;
    }

    public static String omregnTilTimer(long hundredeler) {
        int timer = (int) hundredeler / (100 * 60 * 60);
        int restTimer = (int) hundredeler % (100 * 60 * 60);

        int minutter = restTimer / (60 * 100);
        int restMinutter = restTimer % (60 * 100);

        int sekunder = restMinutter / 100;
        return "Timer: " + timer + ". Minutter: " + minutter + ". Sekunder: " + sekunder;
    }
}