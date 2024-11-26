package com.railways.railways.Configuration;

/**
 * Represents the size of a map with width and height.
 * This class ensures that the width and height values are positive.
 */
public class MapSize {
    // Width of the map
    private  int width;
    // Height of the map
    private  int height;
    /**
     * Constructs a MapSize object with the specified width and height.
     *
     * @param width the width of the map, must be positive
     * @param height the height of the map, must be positive
     * @throws IllegalArgumentException if either width or height is non-positive
     */
    public MapSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.width = width;
        this.height = height;
    }
    /**
     * Returns the width of the map.
     *
     * @return the width of the map
     */
    public int getWidth() {
        return width;
    }
    /**
     * Returns the height of the map.
     *
     * @return the height of the map
     */
    public int getHeight() {
        return height;
    }
}
