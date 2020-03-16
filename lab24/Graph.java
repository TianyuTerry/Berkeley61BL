import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Iterator;
import java.util.Stack;
import java.util.ArrayList;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {

        LinkedList<Edge> list = adjLists[v1];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).to == v2) {
                list.remove(i);
                break;
            }
        }
        adjLists[v1].add(new Edge(v1, v2, weight));
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {

        LinkedList<Edge> list = adjLists[v1];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).to == v2) {
                list.remove(i);
                break;
            }
        }
        LinkedList<Edge> lst = adjLists[v2];
        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).to == v2) {
                lst.remove(i);
                break;
            }
        }
        adjLists[v1].add(new Edge(v1, v2, weight));
        adjLists[v2].add(new Edge(v2, v1, weight));
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {

        for (Edge edge : adjLists[from]) {
            if (edge.to == to) {
                return true;
            }
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public LinkedList<Integer> neighbors(int v) {
        LinkedList<Integer> toReturn = new LinkedList<>();
        for (Edge edge : adjLists[v]) {
            toReturn.add(edge.to);
        }
        return toReturn;
    }
    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        int toReturn = 0;
        for (int i = 0; i < adjLists.length; i++) {
            if (isAdjacent(i, v)) {
                toReturn++;
            }
        }
        return toReturn;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /* A class that iterates through the vertices of this graph, starting with
       vertex START. If the iteration from START has no path from START to some
       vertex v, then the iteration will not include v. */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        DFSIterator(int start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }

        public boolean hasNext() {
            for (int element : fringe) {
                if (!visited.contains(element)) {
                    return true;
                }
            }
            return false;
        }

        public Integer next() {
            int v = fringe.pop();
            if (!visited.contains(v)) {
                for (int neighbor: neighbors(v)) {
                    fringe.push(neighbor);
                }
                visited.add(v);
                return v;
            } else {
                return next();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }


        LinkedList<Integer> beginAgain(int start, int end,
                HashSet<Integer> visitedSet, LinkedList<Integer> path) {
            if (end == start) {
                return path;
            } else {
                for (int i : visitedSet) {
                    LinkedList<Integer> newList = new LinkedList<>();
                    if (isAdjacent(i, end)) {
                        newList.add(i);
                        newList.addAll(path);
                        HashSet<Integer> reducedSet = reduceSet(i, visitedSet);
                        LinkedList<Integer> returnedList
                                = beginAgain(start, i, reducedSet, newList);
                        if (!returnedList.isEmpty()) {
                            return returnedList;
                        }
                    }
                }
            }
            return new LinkedList<>();
        }

        HashSet<Integer> getVisited() {
            return visited;
        }



        private HashSet<Integer> reduceSet(int i, HashSet<Integer> largeSet) {
            HashSet<Integer> toReturn = new HashSet<>();
            for (int j : largeSet) {
                if (j != i) {
                    toReturn.add(j);
                }
            }
            return toReturn;
        }
    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        Iterator<Integer> iterator = new DFSIterator(start);
        while (iterator.hasNext()) {
            if (stop == iterator.next()) {
                return true;
            }
        }
        return false;
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        Boolean bl = false;
        DFSIterator iterator = new DFSIterator(start);
        while (iterator.hasNext()) {
            if (stop == iterator.next()) {
                bl = true;
                break;
            }
        }
        if (!bl) {
            return new LinkedList<>();
        }
        LinkedList<Integer> starter = new LinkedList<>();
        starter.add(stop);
        return iterator.beginAgain(start, stop, iterator.getVisited(), starter);
    }

    public List<Integer> pathLiuXiangYu(int start, int stop) {
        if (!pathExists(start, stop)) {
            return new ArrayList<>();
        }
        ArrayList<Integer> lst = new ArrayList<>();
        pather(start, stop, lst, new HashSet<Integer>());
        lst.add(0, start);
        return lst;
    }

    public boolean pather(int x, int y, List<Integer> lst, HashSet<Integer> set) {
        if (set.contains(x)) {
            return false;
        }
        if (x == y) {
            set.add(x);
            return true;
        } else {
            boolean ans;
            set.add(x);
            for (Edge e : adjLists[x]) {
                ans = pather(e.to, y, lst, set);
                if (ans) {
                    lst.add(0, e.to);
                    return ans;
                }
            }
        }
        return false;
    }

    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private int[] currentInDegree;

        TopologicalIterator() {
            fringe = new Stack<>();
            currentInDegree = new int[vertexCount];
            for (int i = 0; i < vertexCount; i++) {
                currentInDegree[i] = inDegree(i);
                if (currentInDegree[i] == 0) {
                    fringe.push(i);
                    currentInDegree[i] = -1;
                }
            }
        }

        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        public Integer next() {
            int toReturn = fringe.pop();
            for (int i : neighbors(toReturn)) {
                currentInDegree[i]--;
            }
            for (int j = 0; j < vertexCount; j++) {
                if (currentInDegree[j] == 0) {
                    fringe.push(j);
                    currentInDegree[j] = -1;
                }
            }
            return toReturn;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public List<Integer> shortestPath(int start, int stop) {

        Map<Integer, Integer> distances = new HashMap<>();
        for (int i : neighbors(start)) {
            distances.put(i, getWeight(start, i));
        }
        distances.put(start, 0);

        PriorityQueue<Entry> fringe = new PriorityQueue<>(
                11, (a, b) -> (a.distance - b.distance));
        for (int j : distances.keySet()) {
            fringe.add(new Entry(j, distances.get(j)));
        }

        HashSet<Integer> visited = new HashSet<>();

        Map<Integer, Integer> predecessor = new HashMap<>();
        for (int k : distances.keySet()) {
            predecessor.put(k, start);
        }


        while (!fringe.isEmpty()) {
            int v = fringe.poll().vertex;
            if (!visited.contains(v)) {
                if (v == stop) {
                    break;
                } else {
                    visited.add(v);
                    for (int w : neighbors(v)) {
                        if (!visited.contains(w)) {
                            if (!distances.containsKey(w)) {
                                distances.put(w, distances.get(v) + getWeight(v, w));
                                predecessor.put(w, v);
                            } else if (distances.get(v) + getWeight(v, w) < distances.get(w)) {
                                distances.remove(w);
                                distances.put(w, distances.get(v) + getWeight(v, w));
                                predecessor.remove(w);
                                predecessor.put(w, v);
                            }
                            fringe.add(new Entry(w, distances.get(w)));
                        }
                    }
                }
            }
        }

        if (!distances.containsKey(stop)) {
            return new LinkedList<>();
        } else {
            LinkedList<Integer> toReturn = new LinkedList<>();
            if (start == stop) {
                toReturn.add(stop);
                return toReturn;
            } else {
                int p = stop;
                toReturn.addFirst(stop);
                while (predecessor.get(p) != start) {
                    toReturn.addFirst(predecessor.get(p));
                    p = predecessor.get(p);
                }
                toReturn.addFirst(start);
                return toReturn;
            }
        }
    }

    private class Entry {

        int vertex;
        int distance;

        public Entry(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }


    private int getWeight(int u, int v) {
        for (Edge e : adjLists[u]) {
            if (e.to == v) {
                return e.weight;
            }
        }
        return -1;
    }


    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void generateG5() {
        addEdge(0, 1, 10);
        addEdge(0, 3, 30);
        addEdge(0, 4, 100);
        addEdge(1, 2, 50);
        addEdge(2, 4, 10);
        addEdge(3, 2, 20);
        addEdge(3, 4, 60);
    }

    private void generateG6() {
        addEdge(0, 1, 10);
        addEdge(0, 4, 5);
        addEdge(1, 2, 1);
        addEdge(1, 4, 2);
        addEdge(2, 3, 4);
        addEdge(3, 0, 7);
        addEdge(3, 2, 6);
        addEdge(4, 1, 3);
        addEdge(4, 2, 9);
        addEdge(4, 3, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {
        /*Graph g1 = new Graph(5);
        g1.generateG1();
        g1.printDFS(0);
        g1.printDFS(1);
        g1.printDFS(2);
        g1.printDFS(3);
        g1.printDFS(4);

        g1.printPath(0, 3);
        g1.printPath(0, 4);
        g1.printPath(1, 3);
        g1.printPath(1, 4);
        g1.printPath(4, 0);*/

        /*Graph g2 = new Graph(5);
        g2.generateG2();
        g2.printTopologicalSort();*/
        /*g2.printPath(0, 3);
        g2.printPath(0, 4);
        g2.printPath(1, 3);
        g2.printPath(1, 4);
        g2.printPath(4, 0);*/

        Graph g5 = new Graph(5);
        g5.generateG5();
        System.out.println(g5.shortestPath(0, 4));
        System.out.println(g5.shortestPath(0, 2));

        Graph g6 = new Graph(5);
        g6.generateG6();
        System.out.println(g6.shortestPath(0, 2));
        System.out.println(g6.shortestPath(0, 0));
        System.out.println(g6.shortestPath(0, 1));

    }
}
