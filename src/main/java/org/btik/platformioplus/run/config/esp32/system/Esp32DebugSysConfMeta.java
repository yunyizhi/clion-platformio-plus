package org.btik.platformioplus.run.config.esp32.system;

/**
 * @author lustre
 * @since 2025/5/4 1:30
 */
public interface Esp32DebugSysConfMeta {

    String DEBUG_CFG_ROOT = "esp32-debug";

    String TARGET_CONF = "target-conf";
    String TARGET = "target";

    String TYPE = "type";

    String TOOLCHAIN = "toolchain";

    String GDB = "gdb";

    String DEFAULT_OPENOCD_CFG = "default-openocd-cfg";

    String OPENOCD_CONF = "openocd-conf";

    String DIR = "dir";

    String NAME = "name";

    String TARGET_DEFAULT = "default";
}
