package com.railways.railways;

import com.railways.railways.domain.station.Entrance;
import com.railways.railways.domain.station.Segment;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntranceTest {

    @Test
    public void testGetStartPoint() {
        Point start = new Point(0, 0);
        Point end = new Point(10, 10);
        Segment segment = new Segment(start, end);
        Entrance entrance = new Entrance(1, segment);

        assertEquals(start, entrance.getStartPoint());
    }

    @Test
    public void testGetId() {
        Segment segment = new Segment(new Point(0, 0), new Point(10, 10));
        Entrance entrance = new Entrance(5, segment);

        assertEquals(5, entrance.getId());
    }
}
