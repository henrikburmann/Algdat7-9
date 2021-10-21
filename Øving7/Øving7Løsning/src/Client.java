
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Edge {

    int from, to, flow, capacity;
    public Edge reverseEdge;


    public Edge(int from, int to, int capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
    }
}

class Node {
    ArrayList<Edge> edges = new ArrayList<>();
}

class MaxFlow {

    ArrayList<Answer> answers = new ArrayList<>(); // økning og flytøkende vei for graf
    int maxFlow;

    /**
     * Leser graph
     */
    public static Node[] newGraph(String filePath) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int E = Integer.parseInt(st.nextToken());

        Node[] nodes = new Node[N];

        for (int i = 0; i < N ; i++) {
            nodes[i] = new Node();
        }

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());

            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int capacity = Integer.parseInt(st.nextToken());

            if (capacity == 0) throw new IllegalArgumentException("Forward edge capacity <= 0");

            Edge e1 = new Edge(from, to, capacity);
            Edge e2 = new Edge(to, from, 0);

            // reverse edge gets minus value of capacity (use later in code)
            e1.reverseEdge = e2;
            e2.reverseEdge = e1;

            nodes[from].edges.add(e1);
            nodes[to].edges.add(e2);
        }
        return nodes;
    }

    void edmondsKarp(int source, int sink, Node[] graph) {

        maxFlow = 0;

        while (true) {

            Edge[] flowPath = new Edge[graph.length];
            Queue<Node> queue = new ArrayDeque<>();
            queue.add(graph[source]);

            // breadt-first-search
            while (!queue.isEmpty()) {

                Node current = queue.poll(); // removes and returns first node in queue

                for (Edge e : current.edges) {
                    if (flowPath[e.to] == null && e.to != source && e.capacity > e.flow) {

                        // lagrer flowpath til bfs slik at man kan jobbe med det senere
                        flowPath[e.to] = e;

                        queue.add(graph[e.to]);
                    }
                }
            }

            if (flowPath[sink] == null) {
                break; // sink is not reached
            }

            // increase in flow
            int pushFlow = Integer.MAX_VALUE;

            // max flow
            for (Edge e = flowPath[sink]; e != null; e = flowPath[e.from])
                pushFlow = Math.min(pushFlow, e.capacity - e.flow);


            // Stores answer from one bfs search
            Answer answer = new Answer();
            answer.okning = pushFlow;
            answer.kanter = new LinkedList<>();
            answer.kanter.add(sink);

            // update flow for edges in flowpath
            for (Edge e = flowPath[sink]; e != null; e = flowPath[e.from]) {
                e.flow += pushFlow;
                e.reverseEdge.flow -= pushFlow;

                answer.kanter.addFirst(e.from);

            }

            answers.add(answer);
            maxFlow += pushFlow;

        }

    }

    void printAnswers() {
        for (int i = 0; i < answers.size() ; i++) {
            System.out.println(answers.get(i));
        }
    }

    /**
     * inner class that stores answer
     */
    static class Answer {
        int okning;
        LinkedList<Integer> kanter;

        @Override
        public String toString() {
            return "   " + okning + "    " + kanter;
        }
    }


    public static void main(String[] args) throws IOException {

        Node[] grap1 = MaxFlow.newGraph("Øving7/graphs/flytgraf1");
        Node[] graph2 = MaxFlow.newGraph("Øving7/graphs/flytgraf2");
        Node[] graph3 = MaxFlow.newGraph("Øving7/graphs/flytgraf3");

        MaxFlow mf = new MaxFlow();
        mf.edmondsKarp(0, 7, grap1);
        System.out.println("\t---Graf 1---\nØkning : Flytøkende vei");
        mf.printAnswers();
        System.out.println("Maximal flyt ble: " + mf.maxFlow);
        System.out.println();

        mf = new MaxFlow();
        mf.edmondsKarp(4, 15, graph2);
        System.out.println("\t---Graf 2---\nØkning : Flytøkende vei");
        mf.printAnswers();
        System.out.println("Maximal flyt ble: " + mf.maxFlow);
        System.out.println();

        mf = new MaxFlow();
        mf.edmondsKarp(3, 15, graph3);
        System.out.println("\t---Graf 3---\nØkning : Flytøkende vei");
        mf.printAnswers();
        System.out.println("Maximal flyt ble: " + mf.maxFlow);
    }
}