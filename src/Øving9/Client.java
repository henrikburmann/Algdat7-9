package Øving9;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        Djikstra d = new Djikstra();
        d.readFile("src/Øving9/Files/nodeSmall", "src/Øving9/Files/edgeSmall");
    }
}
