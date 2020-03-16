import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Kevin Lowe, Antares Chen, Kevin Lin
 */
public class GraphDB {
    /**
     * This constructor creates and starts an XML parser, cleans the nodes, and prepares the
     * data structures for processing. Modify this constructor to initialize your data structures.
     * @param dbPath Path to the XML file to be parsed.
     */

    /**
     * This data type represent a particular position on the map
     */
    HashMap<Long, Vertex> allNodes = new HashMap<>();
    HashMap<Long, HashSet<Long>> neighbors = new HashMap<>();
    HashSet<Road> roads = new HashSet<>();
    KDTree kdTree;

    private static final double INF = Math.pow(2, 30);


    public static class Vertex {
        private long id;
        private double lon;
        private double lat;
        private double x;
        private double y;
        Vertex left;
        Vertex right;
        /**
         * this pre field is particularly work for A* algorithm
         */
        Vertex prev = null;
        private HashMap<String, String> extraInfo = new HashMap<>();

        Vertex(long id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
            this.x = projectToX(lon, lat);
            this.y = projectToY(lon, lat);
            //System.out.println("id = " + id + ", x = " + x + ", y = " + y);
        }

        public void putInformation(String k, String v) {
            this.extraInfo.put(k, v);
        }

        public long getId() {
            return id;
        }

        public double getLon() {
            return lon;
        }

        public double getLat() {
            return lat;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public HashMap<String, String> getExtraInfo() {
            return this.extraInfo;
        }
    }

    public static class Road {
        long id;
        ArrayList<Long> passBy;
        boolean valid = false;
        HashMap<String, String> extraInfo = new HashMap<>();

        Road() {
        }

        Road(long id) {
            id = id;
        }

        public void setPassBy(ArrayList<Long> p) {
            passBy = p;
        }

        public void shutDown() {
            valid = false;
        }

        public void open() {
            valid = true;
        }

        public int size() {
            return passBy.size();
        }

        public long get(int index) {
            return passBy.get(index);
        }
    }

    public GraphDB(String dbPath) {
        File inputFile = new File(dbPath);
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputStream, new GraphBuildingHandler(this));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        setNeighbors();
        clean();
    }

    private static int compare(double x, double y) {
        if (x > y) {
            return 1;
        } else if (x < y) {
            return -1;
        } else {
            return 0;
        }
    }

    private double h(Vertex u, Vertex v) {
        return distance(u.getId(), v.getId());
    }

    public ArrayList<Long> finder(double stlon, double stlat,
                                  double destlon, double destlat) {
        Vertex start = allNodes.get(closest(stlon, stlat));
        Vertex end = allNodes.get(closest(destlon, destlat));
        return aStarSearch(start, end);
    }

    public ArrayList<Long> aStarSearch(Vertex start, Vertex end) {
        Vertex ptr;
        HashSet<Vertex> visited = new HashSet<>();
        ArrayList<Long> ans = new ArrayList<>();
        HashMap<Vertex, Double> dis = new HashMap<>();
        PriorityQueue<Vertex> pq;
        pq = new PriorityQueue<>(100, (v1, v2)
            -> (compare(dis.get(v1) + h(v1, end), dis.get(v2) + h(v2, end))));

        for (Vertex v : this.allNodes.values()) {
            dis.put(v, INF);
        }
        dis.remove(start);
        dis.put(start, 0.0);

        pq.add(start);
        while (!pq.isEmpty()) {
            Vertex curr = pq.poll();
            if (visited.contains(curr)) {
                continue;
            }
            if (curr == end) {
                break;
            }
            visited.add(curr);
            long currId = curr.getId();
            for (long l : neighbors.get(currId)) {
                Vertex v = allNodes.get(l);
                if (distance(l, currId) + dis.get(curr) < dis.get(v)) {
                    dis.remove(v);
                    dis.put(v, distance(l, currId) + dis.get(curr));
                    pq.add(v);
                    v.prev = curr;
                }
            }
        }

        ptr = end;
        if (ptr.prev == null) {
            return ans;
        }
        while (ptr != start) {
            ans.add(0, ptr.getId());
            ptr = ptr.prev;
        }
        ans.add(0, start.getId());
        return ans;
    }

    @Override
    public String toString() {
        return "allNode:\n"
                + allNodes + "\n"
                + "neighbors:\n"
                + neighbors + "\n"
                + "roads:\n"
                + roads;

    }


    private void addNeighbor(long v, long w) {
        if (!neighbors.containsKey(v) || !neighbors.containsKey(w)) {
            return;
        }
        neighbors.get(v).add(w);
        neighbors.get(w).add(v);
    }


    private void setNeighbors() {
        for (Road r : roads) {
            if (!r.valid) {
                continue;
            }
            for (int i = 0; i < r.size() - 1; i++) {
                addNeighbor(r.get(i), r.get(i + 1));
            }
        }
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */

    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TODO
        ArrayList<Long> needTo1 = new ArrayList<>();
        //ArrayList<Long> needTo2 = new ArrayList<>();
        for (long node : neighbors.keySet()) {
            if (neighbors.get(node).isEmpty()) {
                needTo1.add(node);
            }
            /*
            if (neighbors.get(node).size() == 1) {
                needTo2.add(node);
            }
            */
        }
        for (long node : needTo1) {
            neighbors.remove(node);
            allNodes.remove(node);
        }
        /*
        for (long node: needTo2) {
            for (long y : neighbors.get(node)) {
                neighbors.get(y).remove(node);
            }
            neighbors.remove(node);
            allNodes.remove(node);
        }
        */

        Vertex[] vertexArray = new Vertex[allNodes.size()];
        int i = 0;
        for (long node : allNodes.keySet()) {
            vertexArray[i] = allNodes.get(node);
            i++;
        }
        this.kdTree = new KDTree(vertexArray);
    }

    /**
     * Returns the longitude of vertex <code>v</code>.
     *
     * @param v The ID of a vertex in the graph.
     * @return The longitude of that vertex, or 0.0 if the vertex is not in the graph.
     */
    double lon(long v) {
        // TODO
        return allNodes.get(v).getLon();
    }

    /**
     * Returns the latitude of vertex <code>v</code>.
     *
     * @param v The ID of a vertex in the graph.
     * @return The latitude of that vertex, or 0.0 if the vertex is not in the graph.
     */
    double lat(long v) {
        // TODO
        return allNodes.get(v).getLat();
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of all vertex IDs in the graph.
     */
    Iterable<Long> vertices() {
        // TODO
        return allNodes.keySet();
    }

    /**
     * Returns an iterable over the IDs of all vertices adjacent to <code>v</code>.
     *
     * @param v The ID for any vertex in the graph.
     * @return An iterable over the IDs of all vertices adjacent to <code>v</code>, or an empty
     * iterable if the vertex is not in the graph.
     */
    Iterable<Long> adjacent(long v) {
        // TODO
        return neighbors.get(v);
    }

    /**
     * Returns the great-circle distance between two vertices, v and w, in miles.
     * Assumes the lon/lat methods are implemented properly.
     *
     * @param v The ID for the first vertex.
     * @param w The ID for the second vertex.
     * @return The great-circle distance between vertices and w.
     * @source https://www.movable-type.co.uk/scripts/latlong.html
     */
    public double distance(long v, long w) {
        double phi1 = Math.toRadians(lat(v));
        double phi2 = Math.toRadians(lat(w));
        double dphi = Math.toRadians(lat(w) - lat(v));
        double dlambda = Math.toRadians(lon(w) - lon(v));

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public static double distance(double u, double v, double x, double y) {
        double phi1 = Math.toRadians(u);
        double phi2 = Math.toRadians(x);
        double dphi = Math.toRadians(x - u);
        double dlambda = Math.toRadians(y - v);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Returns the ID of the vertex closest to the given longitude and latitude.
     *
     * @param lon The given longitude.
     * @param lat The given latitude.
     * @return The ID for the vertex closest to the <code>lon</code> and <code>lat</code>.
     */
    public long closest(double lon, double lat) {
        // TODO

        double x = projectToX(lon, lat);
        double y = projectToY(lon, lat);
        Vertex v = this.kdTree.nearest(x, y);

        return v.getId();
    }

    /**
     * Return the Euclidean x-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     *
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean x-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToX(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double b = Math.sin(dlon) * Math.cos(phi);
        return (K0 / 2) * Math.log((1 + b) / (1 - b));
    }

    /**
     * Return the Euclidean y-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     *
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean y-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToY(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double con = Math.atan(Math.tan(phi) / Math.cos(dlon));
        return K0 * (con - Math.toRadians(ROOT_LAT));
    }

    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     *
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return Collections.emptyList();
    }

    /**
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     *
     * @param locationName A full name of a location searched for.
     * @return A <code>List</code> of <code>LocationParams</code> whose cleaned name matches the
     * cleaned <code>locationName</code>
     */
    public List<LocationParams> getLocations(String locationName) {
        return Collections.emptyList();
    }

    /**
     * Returns the initial bearing between vertices <code>v</code> and <code>w</code> in degrees.
     * The initial bearing is the angle that, if followed in a straight line along a great-circle
     * arc from the starting point, would take you to the end point.
     * Assumes the lon/lat methods are implemented properly.
     *
     * @param v The ID for the first vertex.
     * @param w The ID for the second vertex.
     * @return The bearing between <code>v</code> and <code>w</code> in degrees.
     * @source https://www.movable-type.co.uk/scripts/latlong.html
     */
    double bearing(long v, long w) {
        double phi1 = Math.toRadians(lat(v));
        double phi2 = Math.toRadians(lat(w));
        double lambda1 = Math.toRadians(lon(v));
        double lambda2 = Math.toRadians(lon(w));

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Radius of the Earth in miles.
     */
    private static final int R = 3963;
    /**
     * Latitude centered on Berkeley.
     */
    private static final double ROOT_LAT = (MapServer.ROOT_ULLAT + MapServer.ROOT_LRLAT) / 2;
    /**
     * Longitude centered on Berkeley.
     */
    private static final double ROOT_LON = (MapServer.ROOT_ULLON + MapServer.ROOT_LRLON) / 2;
    /**
     * Scale factor at the natural origin, Berkeley. Prefer to use 1 instead of 0.9996 as in UTM.
     *
     * @source https://gis.stackexchange.com/a/7298
     */
    private static final double K0 = 1.0;


    class KDTree {
        int size;
        Vertex root;

        public KDTree(Vertex[] vertexArray) {
            root = kDTreeHelper(vertexArray, 0, vertexArray.length - 1, 0);
            size = vertexArray.length;
        }

        private Vertex kDTreeHelper(Vertex[] vertexArray, int p, int q, int depth) {
            if (p > q) {
                return null;
            }
            if (depth % 2 == 0) {
                Arrays.sort(vertexArray, p, q + 1,
                    (o1, o2) -> compare(o1.getX(), o2.getX()));
            } else {
                Arrays.sort(vertexArray, p, q + 1,
                    (o1, o2) -> compare(o1.getY(), o2.getY()));

            }
            int m = (p + q) / 2;
            GraphDB.Vertex mid = vertexArray[m];
            mid.left = kDTreeHelper(vertexArray, p, m - 1, depth + 1);
            mid.right = kDTreeHelper(vertexArray, m + 1, q, depth + 1);
            return mid;
        }

        public GraphDB.Vertex nearest(double x, double y) {
            double xLeft = projectToX(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT);
            double yUp = projectToY(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT);
            double xRight = projectToX(MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT);
            double yDown = projectToY(MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT);
            Boundary b = new Boundary(xLeft, xRight, yDown, yUp);
            return closeHelper(x, y, root, b, root, 0);
        }

        private Vertex closeHelper(double xq, double yq,
                                Vertex r, Boundary b, Vertex bestVertex, int depth) {
            if (r == null) {
                return bestVertex;
            } else {
                if (euclideanDistance(xq, r.x, yq, r.y)
                        < euclideanDistance(xq, bestVertex.x, yq, bestVertex.y)) {
                    bestVertex = r;
                }
                if (depth % 2 == 0) {
                    Boundary newBoundaryLeft = new Boundary(b.xLeft, r.x, b.yDown, b.yUp);
                    Boundary newBoundaryRight = new Boundary(r.x, b.xRight, b.yDown, b.yUp);
                    if (xq < r.x) {
                        if (minimumDistance(xq, yq, newBoundaryLeft)
                                < euclideanDistance(xq, bestVertex.x, yq, bestVertex.y)) {
                            bestVertex = closeHelper(xq, yq, r.left,
                                newBoundaryLeft, bestVertex, depth + 1);
                        }
                        if (minimumDistance(xq, yq, newBoundaryRight)
                                < euclideanDistance(xq, bestVertex.x, yq, bestVertex.y)) {
                            bestVertex = closeHelper(xq, yq, r.right,
                                newBoundaryRight, bestVertex, depth + 1);
                        }
                    } else {
                        if (minimumDistance(xq, yq, newBoundaryRight)
                                < euclideanDistance(xq, bestVertex.x, yq, bestVertex.y)) {
                            bestVertex = closeHelper(xq, yq, r.right,
                                newBoundaryRight, bestVertex, depth + 1);
                        }
                        if (minimumDistance(xq, yq, newBoundaryLeft)
                                < euclideanDistance(xq, bestVertex.x, yq, bestVertex.y)) {
                            bestVertex = closeHelper(xq, yq, r.left,
                                newBoundaryLeft, bestVertex, depth + 1);
                        }
                    }
                } else {
                    Boundary newBoundaryUp = new Boundary(b.xLeft, b.xRight, r.y, b.yUp);
                    Boundary newBoundaryDown = new Boundary(b.xLeft, b.xRight, b.yDown, r.y);
                    if (yq > r.y) {
                        if (minimumDistance(xq, yq, newBoundaryUp)
                                < euclideanDistance(xq, bestVertex.x, yq, bestVertex.y)) {
                            bestVertex = closeHelper(xq, yq, r.right,
                                newBoundaryUp, bestVertex, depth + 1);
                        }
                        if (minimumDistance(xq, yq, newBoundaryDown)
                                < euclideanDistance(xq, bestVertex.x, yq, bestVertex.y)) {
                            bestVertex = closeHelper(xq, yq, r.left,
                                newBoundaryDown, bestVertex, depth + 1);
                        }
                    } else {
                        if (minimumDistance(xq, yq, newBoundaryDown)
                                < euclideanDistance(xq, bestVertex.x, yq, bestVertex.y)) {
                            bestVertex = closeHelper(xq, yq, r.left,
                                newBoundaryDown, bestVertex, depth + 1);
                        }
                        if (minimumDistance(xq, yq, newBoundaryUp)
                                < euclideanDistance(xq, bestVertex.x, yq, bestVertex.y)) {
                            bestVertex = closeHelper(xq, yq, r.right,
                                newBoundaryUp, bestVertex, depth + 1);
                        }
                    }
                }
            }
            return bestVertex;
        }

        private double minimumDistance(double xq, double yq, Boundary b) {
            if (xq > b.xLeft && xq < b.xRight && yq < b.yUp && yq > b.yDown) {
                return minimumAmongFour(xq - b.xLeft, b.xRight - xq, b.yUp - yq, yq - b.yDown);
            } else if (xq < b.xLeft && yq < b.yUp && yq > b.yDown) {
                return minimumAmongThree(b.xLeft - xq, b.yUp - yq, yq - b.yDown);
            } else if (b.xRight < xq && yq < b.yUp && yq > b.yDown) {
                return minimumAmongThree(xq - b.xRight, b.yUp - yq, yq - b.yDown);
            } else if (xq > b.xLeft && xq < b.xRight && yq < b.yDown) {
                return minimumAmongThree(xq - b.xLeft, b.xRight - xq, b.yDown - yq);
            } else if (xq > b.xLeft && xq < b.xRight && yq > b.yUp) {
                return minimumAmongThree(xq - b.xLeft, b.xRight - xq, yq - b.yUp);
            } else {
                double distance1 = euclideanDistance(xq, b.xLeft, yq, b.yUp);
                double distance2 = euclideanDistance(xq, b.xLeft, yq, b.yDown);
                double distance3 = euclideanDistance(xq, b.xRight, yq, b.yDown);
                double distance4 = euclideanDistance(xq, b.xRight, yq, b.yUp);
                return minimumAmongFour(distance1, distance2, distance3, distance4);
            }
        }

        private double euclideanDistance(double x1, double x2, double y1, double y2) {
            return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        }

        private double minimumAmongFour(double x1, double x2, double x3, double x4) {
            return Math.min(Math.min(Math.min(x1, x2), x3), x4);
        }

        private double minimumAmongThree(double x1, double x2, double x3) {
            return Math.min(Math.min(x1, x2), x3);
        }

        private class Boundary {
            double xLeft, xRight, yDown, yUp;

            public Boundary(double xLeft, double xRight, double yDown, double yUp) {
                this.xLeft = xLeft;
                this.xRight = xRight;
                this.yDown = yDown;
                this.yUp = yUp;
            }
        }
    }


    public static void main(String[] args) {
        /*double point1Lon = -122.28;
        double point1Lat = 37.88;
        double point2Lon = -122.22;
        double point2Lat = 37.83;
        System.out.println(projectToX(point1Lon, point1Lat));
        System.out.println(projectToY(point1Lon, point1Lat));
        System.out.println(projectToX(point2Lon, point2Lat));
        System.out.println(projectToY(point2Lon, point2Lat));
        System.out.println(projectToX(point1Lon, point2Lat));
        System.out.println(projectToY(point1Lon, point2Lat));
        double xLeft1 = projectToX(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT);
        double yUp1 = projectToY(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT);
        double xRight1 = projectToX(MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT);
        double yDown1 = projectToY(MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT);
        double xLeft2 = projectToX(MapServer.ROOT_ULLON, MapServer.ROOT_LRLAT);
        double yUp2 = projectToY(MapServer.ROOT_ULLON, MapServer.ROOT_LRLAT);
        double xRight2 = projectToX(MapServer.ROOT_LRLON, MapServer.ROOT_ULLAT);
        double yDown2 = projectToY(MapServer.ROOT_LRLON, MapServer.ROOT_ULLAT);
        System.out.println(xLeft1);
        System.out.println(xLeft2);
        System.out.println(yDown1);
        System.out.println(yDown2);*/
    }
}



