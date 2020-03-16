/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    /**
     * The max image depth level.
     */
    public static final int MAX_DEPTH = 7;

    /**
     * Takes a user query and finds the grid of images that best matches the query. These images
     * will be combined into one big image (rastered) by the front end. The grid of images must obey
     * the following properties, where image in the grid is referred to as a "tile".
     * <ul>
     * <li>The tiles collected must cover the most longitudinal distance per pixel (LonDPP)
     * possible, while still covering less than or equal to the amount of longitudinal distance
     * per pixel in the query box for the user viewport size.</li>
     * <li>Contains all tiles that intersect the query bounding box that fulfill the above
     * condition.</li>
     * <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params The RasterRequestParams containing coordinates of the query box and the browser
     *               viewport width and height.
     * @return A valid RasterResultParams containing the computed results.
     */
    public RasterResultParams getMapRaster(RasterRequestParams params) {
        /*System.out.println(
                "Since you haven't implemented getMapRaster,
                nothing is displayed in the browser.");*/

        /*
         * Hint: Define additional classes to make it easier to pass around multiple values, and
         * define additional methods to make it easier to test and reason about code. */
        //System.out.println(params);

        int depth = 0;
        double lonDPP = lonDPP(params.lrlon, params.ullon, params.w);
        while (lonDPP(MapServer.ROOT_LRLON, MapServer.ROOT_ULLON, 256 * Math.pow(2, depth)) > lonDPP
                && depth < 7) {
            depth++;
        }

        System.out.println("depth" + depth);

        double lonDPI = lonDPI(depth);
        double latDPI = latDPI(depth);

        int initialX = getInitialX(params.ullon, MapServer.ROOT_ULLON, lonDPI);
        int initialY = getInitialY(params.ullat, MapServer.ROOT_ULLAT, latDPI);

        double ullontoReturn = MapServer.ROOT_ULLON + initialX * lonDPI;
        double ullattoReturn = MapServer.ROOT_ULLAT - initialY * latDPI;


        int xCounts = (int) Math.floor((params.lrlon - ullontoReturn) / lonDPI) + 1;
        int yCounts = (int) Math.floor((ullattoReturn - params.lrlat) / latDPI) + 1;

        int[] trimmed = trim(initialX, initialY, xCounts, yCounts, depth);

        if (trimmed == null) {
            return RasterResultParams.queryFailed();
        }

        initialX = trimmed[0];
        initialY = trimmed[2];

        xCounts = trimmed[1] + 1 - initialX;
        yCounts = trimmed[3] + 1 - initialY;

        ullontoReturn = MapServer.ROOT_ULLON + initialX * lonDPI;
        ullattoReturn = MapServer.ROOT_ULLAT - initialY * latDPI;

        double lrlontoReturn = ullontoReturn + xCounts * lonDPI;
        double lrlattoReturn = ullattoReturn - yCounts * latDPI;

        String[][] renderGrid = new String[yCounts][xCounts];

        for (int i = 0; i < yCounts; i++) {
            String[] temp = new String[xCounts];
            for (int j = 0; j < xCounts; j++) {
                temp[j] = "d" + Integer.toString(depth)
                        + "_x" + Integer.toString(j + initialX) + "_y"
                        + Integer.toString(i + initialY) + ".png";
            }
            renderGrid[i] = temp;
        }

        for (int i = 0; i < yCounts; i++) {
            for (int j = 0; j < xCounts; j++) {
                System.out.println(renderGrid[i][j]);
            }
        }
        RasterResultParams.Builder b = new RasterResultParams.Builder();
        b.setDepth(depth);
        b.setQuerySuccess(true);
        b.setRasterLrLat(lrlattoReturn);
        b.setRasterLrLon(lrlontoReturn);
        b.setRasterUlLat(ullattoReturn);
        b.setRasterUlLon(ullontoReturn);
        b.setRenderGrid(renderGrid);

        return b.create();
    }

    /**
     * Calculates the lonDPP of an image or query box
     *
     * @param lrlon Lower right longitudinal value of the image or query box
     * @param ullon Upper left longitudinal value of the image or query box
     * @param width Width of the query box or image
     * @return lonDPP
     */
    private double lonDPP(double lrlon, double ullon, double width) {
        return (lrlon - ullon) / width;
    }

    private double lonDPI(int depth) {
        return (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
    }

    private double latDPI(int depth) {
        return (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
    }

    private int getInitialX(double ullon, double rootUllon, double lonDPI) {
        return (int) Math.floor((ullon - rootUllon) / lonDPI);
    }

    private int getInitialY(double ullat, double rootUllat, double latDPI) {
        return (int) Math.floor((rootUllat - ullat) / latDPI);
    }

    //toReturn : initialX, finalX, initialY, finalY
    private int[] trim(int initialX, int initialY, int xCount, int yCount, int depth) {
        int[] toReturn = new int[4];
        int upperBound = (int) Math.pow(2, depth) - 1;
        int finalY = initialY + yCount - 1;
        int finalX = initialX + xCount - 1;

        if ((initialX < 0 && finalX < 0) || (initialX > upperBound && finalX > upperBound)) {
            return null;
        } else if (initialX < 0 && finalX <= upperBound) {
            toReturn[0] = 0;
            toReturn[1] = finalX;
        } else if (initialX < 0) {
            toReturn[0] = 0;
            toReturn[1] = upperBound;
        } else if (initialX <= upperBound && finalX <= upperBound) {
            toReturn[0] = initialX;
            toReturn[1] = finalX;
        } else {
            toReturn[0] = initialX;
            toReturn[1] = upperBound;
        }
        if ((initialY < 0 && finalY < 0) || (initialY > upperBound && finalY > upperBound)) {
            return null;
        } else if (initialY < 0 && finalY <= upperBound) {
            toReturn[2] = 0;
            toReturn[3] = finalY;
        } else if (initialY < 0) {
            toReturn[2] = 0;
            toReturn[3] = upperBound;
        } else if (initialY <= upperBound && finalY <= upperBound) {
            toReturn[2] = initialY;
            toReturn[3] = finalY;
        } else {
            toReturn[2] = initialY;
            toReturn[3] = upperBound;
        }
        return toReturn;
    }

    public static void main(String[] args) {
        /*double lrlon=-122.24053369025242;
        double ullon=-122.24163047377972;
        double w=892.0;
        double h=875.0;
        double ullat=37.87655856892288;
        double lrlat=37.87548268822065;


        RasterRequestParams.Builder b = new RasterRequestParams.Builder();
        b.setH(h);
        b.setLrlat(lrlat);
        b.setLrlon(lrlon);
        b.setUllat(ullat);
        b.setUllon(ullon);
        b.setW(w);
        Rasterer r = new Rasterer();
        System.out.println(r.getMapRaster(b.create()));*/
    }
}
