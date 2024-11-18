package com.railways.railways.simulator.src.hall;

import java.awt.*;

public class Segment {
    private final Point topLeft;
    private final Point bottomRight;

    public Segment(Point topLeft, Point bottomRight) {
        // Ensure that topLeft is actually the top-left point
        // and bottomRight is the bottom-right point, even if input points are swapped
        this.topLeft = new Point(Math.min(topLeft.x, bottomRight.x), Math.min(topLeft.y, bottomRight.y));
        this.bottomRight = new Point(Math.max(topLeft.x, bottomRight.x), Math.max(topLeft.y, bottomRight.y));
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public boolean contains(Point point) {
        return isPointWithinRectangle(point, topLeft, bottomRight);
    }

    public static boolean isPointWithinRectangle(Point point, Point topLeft, Point bottomRight) {
        return point.x >= topLeft.x && point.x <= bottomRight.x &&
                point.y >= topLeft.y && point.y <= bottomRight.y;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "topLeft=" + topLeft +
                ", bottomRight=" + bottomRight +
                '}';
    }
}
