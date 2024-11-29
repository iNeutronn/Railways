package com.railways.railways;

import com.railways.railways.domain.client.PrivilegeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrivilegeEnumTest {

    @Test
    void testGetPriority() {
        assertEquals(1, PrivilegeEnum.DISABLED.getPriority());
        assertEquals(3, PrivilegeEnum.WITHCHILD.getPriority());
        assertEquals(2, PrivilegeEnum.WARVETERAN.getPriority());
        assertEquals(0, PrivilegeEnum.DEFAULT.getPriority());
    }
}
