package org.btik.platformioplus.util;


import org.junit.Test;

public class SysConfTest {
    @Test
    public void format() {
        String f = SysConf.getF("pio.home.parameters", "2121");
        assert f.endsWith("--session-id 2121");
    }
}