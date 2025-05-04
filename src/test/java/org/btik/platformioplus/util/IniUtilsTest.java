package org.btik.platformioplus.util;

import junit.framework.TestCase;

public class IniUtilsTest extends TestCase {

    public void testGetSectionName() {
        String sectionName = IniUtils.getSectionName("[env111]");
        assertEquals("env111", sectionName);
    }

    public void testGetEnvName() {
        String sectionName = IniUtils.getEnvName("[env:111]");
        assertEquals("111", sectionName);
        sectionName = IniUtils.getEnvName("[env:]");
        assertEquals("", sectionName);
        sectionName = IniUtils.getEnvName("[env:1]");
        assertEquals("1", sectionName);
    }
}