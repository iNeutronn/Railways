package com.railways.railways.domain.station;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a segment in the railway map. A segment is defined by its start and end points.
 * It provides a method to get all the points that lie on the segment.
 */
public class Segment {
    /** The starting point of the segment. */
    public Point start;
    /** The ending point of the segment. */
    public Point end;

    /**
     * Default constructor required for Jackson deserialization.
     * (Private to prevent direct instantiation, used only for deserialization.)
     */
    private Segment() {} // required for jackson

    /**
     * Constructs a new Segment with the specified start and end points.
     *
     * @param start the starting point of the segment
     * @param end the ending point of the segment
     */
    public Segment(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns a set of all points between the start and end points, inclusive.
     * The points are added in a rectangular grid between the start and end points.
     *
     * @return a set of all points that lie on the segment
     */
    public Set<Point> getAllPoints() {
        Set<Point> points = new HashSet<>();

        for (int x = this.start.x; x <= this.end.x; x++) {
            for (int y = this.start.y; y <= this.end.y; y++) {
                points.add(new Point(x, y));
            }
        }

        return points;
    }
}
