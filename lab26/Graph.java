import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.Random;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.ArrayList;

/* A mutable and finite Graph object. Edge labels are stored via a HashMap
   where labels are mapped to a key calculated by the following. The graph is
   undirected (whenever an Edge is added, the dual Edge is also added). Vertices
   are numbered starting from 0. */
public class Graph {

    /* Maps vertices to a list of its neighboring vertices. */
    private HashMap<Integer, Set<Integer>> neighbors = new HashMap<>();
    /* Maps vertices to a list of its connected edges. */
    private HashMap<Integer, Set<Edge>> edges = new HashMap<>();
    /* A sorted set of all edges. */
    private TreeSet<Edge> allEdges = new TreeSet<>();

    /* Returns the vertices that neighbor V. */
    public TreeSet<Integer> getNeighbors(int v) {
        return new TreeSet<Integer>(neighbors.get(v));
    }

    /* Returns all edges adjacent to V. */
    public TreeSet<Edge> getEdges(int v) {
        return new TreeSet<Edge>(edges.get(v));
    }

    /* Returns a sorted list of all vertices. */
    public TreeSet<Integer> getAllVertices() {
        return new TreeSet<Integer>(neighbors.keySet());
    }

    /* Returns a sorted list of all edges. */
    public TreeSet<Edge> getAllEdges() {
        return new TreeSet<Edge>(allEdges);
    }

    /* Adds vertex V to the graph. */
    public void addVertex(Integer v) {
        if (neighbors.get(v) == null) {
            neighbors.put(v, new HashSet<Integer>());
            edges.put(v, new HashSet<Edge>());
        }
    }

    /* Adds Edge E to the graph. */
    public void addEdge(Edge e) {
        addEdgeHelper(e.getSource(), e.getDest(), e.getWeight());
    }

    /* Creates an Edge between V1 and V2 with no weight. */
    public void addEdge(int v1, int v2) {
        addEdgeHelper(v1, v2, 0);
    }

    /* Creates an Edge between V1 and V2 with weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        addEdgeHelper(v1, v2, weight);
    }

    /* Returns true if V1 and V2 are connected by an edge. */
    public boolean isNeighbor(int v1, int v2) {
        return neighbors.get(v1).contains(v2) && neighbors.get(v2).contains(v1);
    }

    /* Returns true if the graph contains V as a vertex. */
    public boolean containsVertex(int v) {
        return neighbors.get(v) != null;
    }

    /* Returns true if the graph contains the edge E. */
    public boolean containsEdge(Edge e) {
        return allEdges.contains(e);
    }

    /* Returns if this graph spans G. */
    public boolean spans(Graph g) {
        TreeSet<Integer> all = getAllVertices();
        if (all.size() != g.getAllVertices().size()) {
            return false;
        }
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> vertices = new ArrayDeque<>();
        Integer curr;

        vertices.add(all.first());
        while ((curr = vertices.poll()) != null) {
            if (!visited.contains(curr)) {
                visited.add(curr);
                for (int n : getNeighbors(curr)) {
                    vertices.add(n);
                }
            }
        }
        return visited.size() == g.getAllVertices().size();
    }

    /* Overrides objects equals method. */
    public boolean equals(Object o) {
        if (!(o instanceof Graph)) {
            return false;
        }
        Graph other = (Graph) o;
        return neighbors.equals(other.neighbors) && edges.equals(other.edges);
    }

    /* A helper function that adds a new edge from V1 to V2 with WEIGHT as the
       label. */
    private void addEdgeHelper(int v1, int v2, int weight) {
        addVertex(v1);
        addVertex(v2);

        neighbors.get(v1).add(v2);
        neighbors.get(v2).add(v1);

        Edge e1 = new Edge(v1, v2, weight);
        Edge e2 = new Edge(v2, v1, weight);
        edges.get(v1).add(e1);
        edges.get(v2).add(e2);
        allEdges.add(e1);
    }

    public Graph primsAgain(int start) {
        Graph T = new Graph();
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        T.addVertex(start);
        for (Edge e : getEdges(start)) {
            pq.add(e);
        }
        while (T.getAllVertices().size() < this.getAllVertices().size()) {
            Edge minimum = pq.poll();
            if (minimum == null) {
                break;
            }
            if (T.containsVertex(minimum.getSource()) && !T.containsVertex(minimum.getDest())) {
                T.addEdge(minimum);
                for (Edge e : getEdges(minimum.getDest())) {
                    pq.add(e);
                }
            }
        }
        if (T.spans(this)) {
            return T;
        }
        return null;
    }

    public Graph prims(int start) {
        Graph T = new Graph();
        HashMap<Integer, Edge> distFromTree = new HashMap<>();
        PriorityQueue<Integer> heap = new PriorityQueue<>(11, (a, b) -> {
            int difference = distFromTree.get(a).getWeight() - distFromTree.get(b).getWeight();
            return difference == 0 ? 1 : difference;
        });
        T.addVertex(start);

        for (Edge e : getEdges(start)) {
            distFromTree.put(e.getDest(), e);
            heap.add(e.getDest());
        }
        while (T.getAllVertices().size() < this.getAllVertices().size()) {
            Integer minimum = heap.poll();
            if (minimum == null) {
                break;
            }
            if (!T.containsVertex(minimum)) {
                T.addEdge(distFromTree.get(minimum));
                for (Edge e : getEdges(minimum)) {
                    if (!distFromTree.containsKey(e.getDest())) {
                        distFromTree.put(e.getDest(), e);
                        heap.add(e.getDest());
                    } else {
                        if (e.getWeight() < distFromTree.get(e.getDest()).getWeight()) {
                            distFromTree.remove(e.getDest());
                            distFromTree.put(e.getDest(), e);
                            heap.add(e.getDest());
                        }
                    }
                }
            }
        }
        if (T.spans(this)) {
            return T;
        }
        return null;
    }

    public Graph kruskals() {
        Graph T = new Graph();
        for (int i : getAllVertices()) {
            T.addVertex(i);
        }
        UnionFind uf = new UnionFind(getAllVertices().size());
        ArrayList<Edge> edgesList = new ArrayList<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        for (Edge e : getAllEdges()) {
            pq.add(e);
        }
        while (pq.peek() != null) {
            edgesList.add(pq.poll());
        }
        for (int j = 0; j < edgesList.size(); j++) {
            if (!uf.connected(edgesList.get(j).getSource(), edgesList.get(j).getDest())) {
                T.addEdge(edgesList.get(j));
                uf.union(edgesList.get(j).getSource(), edgesList.get(j).getDest());
            }
        }
        if (T.spans(this)) {
            return T;
        }
        return null;
    }

    /* Returns a randomly generated graph with VERTICES number of vertices and
       EDGES number of edges with max weight WEIGHT. */
    public static Graph randomGraph(int vertices, int edges, int weight) {
        Graph g = new Graph();
        Random rng = new Random();
        for (int i = 0; i < vertices; i += 1) {
            g.addVertex(i);
        }
        for (int i = 0; i < edges; i += 1) {
            Edge e = new Edge(rng.nextInt(vertices), rng.nextInt(vertices), rng.nextInt(weight));
            g.addEdge(e);
        }
        return g;
    }

    /* Returns a Graph object with integer edge weights as parsed from
       FILENAME. Talk about the setup of this file. */
    public static Graph loadFromText(String filename) {
        Charset cs = Charset.forName("US-ASCII");
        try (BufferedReader r = Files.newBufferedReader(Paths.get(filename), cs)) {
            Graph g = new Graph();
            String line;
            while ((line = r.readLine()) != null) {
                String[] fields = line.split(", ");
                if (fields.length == 3) {
                    int from = Integer.parseInt(fields[0]);
                    int to = Integer.parseInt(fields[1]);
                    int weight = Integer.parseInt(fields[2]);
                    g.addEdge(from, to, weight);
                } else if (fields.length == 1) {
                    g.addVertex(Integer.parseInt(fields[0]));
                } else {
                    throw new IllegalArgumentException("Bad input file!");
                }
            }
            return g;
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    public String toString() {
        String toReturn = "";
        for (Edge e : allEdges) {
            toReturn += e.getSource() + " " + e.getDest() + " " + e.getWeight() + "\n";
        }
        return toReturn;
    }

    public class UnionFind {

        private int[] disjointSet;

        /* Creates a UnionFind data structure holding N vertices. Initially, all
           vertices are in disjoint sets. */
        public UnionFind(int N) {
            disjointSet = new int[N];
            for (int i = 0; i < N; i++) {
                disjointSet[i] = -1;
            }
        }

        /* Returns the size of the set V belongs to. */
        public int sizeOf(int v) {
        /*if (disjointSet[v] < 0) {
            return disjointSet[v];
        }
        return sizeOf(parent(v));*/
            while (disjointSet[v] >= 0) {
                v = disjointSet[v];
            }
            return -disjointSet[v];
        }

        /* Returns the parent of V. If V is the root of a tree, returns the
           negative size of the tree for which V is the root. */
        public int parent(int v) {
            return disjointSet[v];
        }

        /* Returns true if nodes V1 and V2 are connected. */
        public boolean connected(int v1, int v2) {
            return find(v1) == find(v2);
        }

        /* Returns the root of the set V belongs to. Path-compression is employed
           allowing for fast search-time. If invalid vertices are passed into this
           function, throw an IllegalArgumentException. */
        public int find(int v) {
            if (v >= disjointSet.length) {
                throw new IllegalArgumentException();
            }
            if (disjointSet[v] >= 0) {
                disjointSet[v] = find(disjointSet[v]);
                return disjointSet[v];
            } else {
                return v;
            }
        }

        /* Connects two elements V1 and V2 together. V1 and V2 can be any element,
           and a union-by-size heuristic is used. If the sizes of the sets are
           equal, tie break by connecting V1's root to V2's root. Union-ing a vertex
           with itself or vertices that are already connected should not change the
           structure. */
        public void union(int v1, int v2) {

            int parentRoot;
            int childRoot;
            int root1 = find(v1);
            int root2 = find(v2);

            if (root1 != root2) {

                int size1 = -parent(root1);
                int size2 = -parent(root2);

                if (size1 > size2) {
                    childRoot = root2;
                    parentRoot = root1;
                } else {
                    childRoot = root1;
                    parentRoot = root2;
                }
                disjointSet[parentRoot] += disjointSet[childRoot];
                disjointSet[childRoot] = parentRoot;
            }
        }
    }

    public static void main(String[] args) {
        /*HashMap<Integer, Set<Integer>> neighbors = new HashMap<>();
        System.out.println(neighbors.get(0) == null);*/
        Graph g1 = loadFromText("./inputs/graphTestNormal.in");
        Graph g1Prim = g1.primsAgain(0);
        Graph g1Kruskal = g1.kruskals();
        System.out.println(g1Kruskal);
        System.out.println(g1Prim);
        Graph g2 = loadFromText("./inputs/graphTestMultiEdge.in");
        Graph g2Prim = g2.primsAgain(0);
        Graph g2Kruskal = g2.kruskals();
        System.out.println(g2Kruskal);
        System.out.println(g2Prim);
        Graph g3 = loadFromText("./inputs/graphTestSomeDisjoint.in");
        Graph g3Prim = g3.prims(0);
        Graph g3Kruskal = g3.kruskals();
        System.out.println(g3Prim == null);
        System.out.println(g3Kruskal == null);
    }
}
