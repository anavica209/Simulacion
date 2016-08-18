package ar.edu.itba.simul.group8;

import java.util.ArrayList;
import java.util.List;

public class Matrix<T> {

    private final int xmax;
    private final int ymax;
    private final List<T> content;
    private boolean wraparound = false;

    public Matrix(int xmax, int ymax) {
        this.xmax = xmax;
        this.ymax = ymax;
        content = new ArrayList<>(xmax * ymax);

        // Initialize list with null
        for (int i = 0; i < xmax * ymax; i++) {
            content.add(null);
        }
    }

    public void setWraparound(boolean wraparound)  {
        this.wraparound = wraparound;
    }

    private int getIndex(int x, int y) {
        if (wraparound) {
            x = x % xmax;
            y = y % ymax;
        }
        return x * xmax + y;
    }

    public void put(int x, int y, T val) {
        checkBoundaries(x, y);
        content.set(getIndex(x, y), val);
    }

    public T get(int x, int y) {
        checkBoundaries(x, y);
        return content.get(getIndex(x, y));
    }

    public void checkBoundaries(int x, int y) {
        if (x < 0 || x > xmax || y < 0 || y > ymax) {
            throw new IndexOutOfBoundsException();
        }
    }
}
