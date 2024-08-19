package org.btik.platformioplus.util;


import org.junit.Test;

public class SysConfTest {
    @Test
    public void format() {
        String f = SysConf.get("pio.home.parameters");
        assert !f.isEmpty();
    }
}