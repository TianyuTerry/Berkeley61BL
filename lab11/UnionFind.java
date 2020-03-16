
public class UnionFind {

    /* TODO: Add instance variables here. */
    private int[] disjointSet;

    /* Creates a UnionFind data structure holding N vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int N) {
        // TODO: YOUR CODE HERE
        disjointSet = new int[N];
        for (int i = 0; i < N; i++) {
            disjointSet[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        // TODO: YOUR CODE HERE
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
        // TODO: YOUR CODE HERE
        return disjointSet[v];
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO: YOUR CODE HERE
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid vertices are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v >= disjointSet.length) {
            throw new IllegalArgumentException();
        }

        /*List<Integer> list = new ArrayList<>();

        while (disjointSet[v] >= 0) {
            list.add(v);
            v = parent(v);
        }

        for (int i : list) {
            disjointSet[i] = v;
        }
        // TODO: YOUR CODE HERE
        return v;*/
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
        // TODO: YOUR CODE HERE

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
            }
            else {
                childRoot = root1;
                parentRoot = root2;
            }
            disjointSet[parentRoot] += disjointSet[childRoot];
            disjointSet[childRoot] = parentRoot;
        }
    }

    public String toString() {
        String answer = "";
        for (int i : disjointSet) {
            answer += Integer.toString(i) + ' ';
        }
        return answer;
    }

    public static void main(String[] args) {
        UnionFind uf = new UnionFind(5);
        System.out.println(uf);
        uf.union(0,1);
        uf.union(2,3);
        System.out.println(uf);
        uf.union(0,2);
        System.out.println(uf);
    }
}
