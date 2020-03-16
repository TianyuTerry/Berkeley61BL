//import edu.princeton.cs.algs4.QuickFindUF;
//import edu.princeton.cs.algs4.QuickUnionUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF grid;
    private int size;
    private int count;
    private boolean[] booleanGrid;
    // TODO: Add instance variables here.

    /* Creates an N-by-N grid with all sites initially blocked. */
    public Percolation(int N) {
        // TODO: YOUR CODE HERE
        if (N <= 0 ) {
            throw new IllegalArgumentException();
        }
        grid = new WeightedQuickUnionUF(N * N + 2);
        size =  N;
        booleanGrid = new boolean[N * N + 2];
        booleanGrid[N * N + 1] = true;
        booleanGrid[N * N] = true;
        count = 0;
    }

    /* Opens the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        // TODO: YOUR CODE HERE
        if (!valid(row, col)) {
            throw new IndexOutOfBoundsException();
        } else {
            if (!isOpen(row, col)) {
                int index = xyTo1D(row, col);
                booleanGrid[index] = true;

                if (valid(row + 1, col)) {
                    if (isOpen(row + 1, col)) {
                        grid.union(index, index + size);
                    }
                }

                if (valid(row - 1, col)) {
                    if (isOpen(row - 1, col)) {
                        grid.union(index, index - size);
                    }
                }

                if (valid(row, col + 1)) {
                    if (isOpen(row, col + 1)) {
                        grid.union(index, index + 1);
                    }
                }

                if (valid(row, col - 1)) {
                    if (isOpen(row, col - 1)) {
                        grid.union(index, index - 1);
                    }
                }

                count++;

                if (row == 0) {
                    grid.union(size * size + 1, index);
                }

                if (row == size - 1) {
                    grid.union(size * size, index);
                }
            }
        }


    }

    /* Returns true if the site at (row, col) is open. */
    public boolean isOpen(int row, int col) {
        // TODO: YOUR CODE HERE
        if (!valid(row, col)) {
            throw new IndexOutOfBoundsException();
        } else {
            return booleanGrid[xyTo1D(row, col)];
        }
    }

    /* Returns true if the site (row, col) is full. */
    public boolean isFull(int row, int col) {
        // TODO: YOUR CODE HERE
        /*if (row == 0) {
            return isOpen(row, col);
        } else {
            if (!valid(row, col)) {
                throw new IndexOutOfBoundsException();
            } else {
                for (int i = 0; i < size; i++) {
                    if (grid.connected(i, xyTo1D(row, col))) {
                        return true;
                    }
                }
            }
            return false;
        }*/
        return grid.connected(size * size + 1, xyTo1D(row, col));
    }

    /* Returns the number of open sites. */
    public int numberOfOpenSites() {
        // TODO: YOUR CODE HERE
        return count;
    }

    /* Returns true if the system percolates. */
    public boolean percolates() {
        // TODO: YOUR CODE HERE
        /*for (int i = size * (size - 1); i < size * size; i++) {
            if (isFull(i / size, i % size)) {
                return true;
            }
        }
        return false;*/
        return grid.connected(size * size + 1, size * size);
    }

    /* Converts row and column coordinates into a number. This will be helpful
       when trying to tie in the disjoint sets into our NxN grid of sites. */
    private int xyTo1D(int row, int col) {
        // TODO: YOUR CODE HERE
        return row * size + col;
    }
    /* Returns true if (row, col) site exists in the NxN grid of sites.
       Otherwise, return false. */
    private boolean valid(int row, int col) {
        // TODO: YOUR CODE HERE
        return row <= size - 1 && row >= 0 && col <= size - 1 && col >= 0;
    }
}
