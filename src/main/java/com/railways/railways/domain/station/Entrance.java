package com.railways.railways.domain.station;

import java.awt.*;

public class Entrance {
    private int id;
    private Segment segment;

    public Entrance(int id, Segment segment) {
        this.id = id;
        this.segment = segment;
    }

    public int getId() {
        return id;
    }

    public Segment getSegment() {
        return segment;
    }

    // New method to get the starting point of the segment
    public Point getStartPoint() {
        return segment.start;
    }
}