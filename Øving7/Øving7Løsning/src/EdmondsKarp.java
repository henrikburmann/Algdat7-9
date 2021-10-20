import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class EdmondsKarp {


    /**
     * inner class edge
     */
    static class Edge {

        public int from, to;
        public Edge residual;
        public long flow;
        public final long capacity;


        public Edge(int from, int to, long capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
        }

        // todo: lage metoder til edge

    }



    int N, E;
    List<Edge>[] graph;
    private int[] visited;


    public EdmondsKarp(String filePath) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        newGraph(br);
    }

    private void initializeEmptyFlowGraph(int n) {
        graph = new List[n];
        for (int i = 0; i < N; i++) {
            graph[i] = new ArrayList<Edge>();
        }
    }

    public void addEdge(int from, int to, long capacity) {
        if (capacity == 0) throw new IllegalArgumentException("Forward edge capacity <= 0");

        Edge e1 = new Edge(from, to, capacity);
        Edge e2 = new Edge(to, from, 0);

        e1.residual = e2;
        e2.residual = e1;

        graph[from].add(e1);
        graph[to].add(e2);
    }



    public void newGraph(BufferedReader br) throws IOException {

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());

        initializeEmptyFlowGraph(N);

        visited = new int[N];

        E = Integer.parseInt(st.nextToken());
        for (int i = 0; i < E; i++) {

            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int capacity = Integer.parseInt(st.nextToken());

            // addEdge(from, to, capacity);
        }
    }
}
