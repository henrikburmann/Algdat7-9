package Øving9;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        Djikstra d = new Djikstra();
        // bruker island kartet for å teste
        d.readFile("src/Øving9/Files/Test/noder.txt", "src/Øving9/Files/Test/kanter.txt");
    }
}
