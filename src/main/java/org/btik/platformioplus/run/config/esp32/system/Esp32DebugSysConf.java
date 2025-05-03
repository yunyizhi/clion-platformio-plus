package org.btik.platformioplus.run.config.esp32.system;

import java.util.HashMap;

/**
 * @author lustre
 * @since 2025/5/4 1:36
 */
public class Esp32DebugSysConf {
    private HashMap<String, Esp32DebugTargetConfig> targets = new HashMap<String, Esp32DebugTargetConfig>();

    private String openocdFolder;
    private String openocdBinName;
    private String openocdBinPath;
    public HashMap<String, Esp32DebugTargetConfig> getTargets() {
        return targets;
    }

    public void setTargets(HashMap<String, Esp32DebugTargetConfig> targets) {
        this.targets = targets;
    }

    public Esp32DebugTargetConfig getTargetConfig(String target) {
        return targets.get(target);
    }

    public String getOpenocdFolder() {
        return openocdFolder;
    }

    public void setOpenocdFolder(String openocdFolder) {
        this.openocdFolder = openocdFolder;
    }

    public String getOpenocdBinName() {
        return openocdBinName;
    }

    public void setOpenocdBinName(String openocdBinName) {
        this.openocdBinName = openocdBinName;
    }

    public String getOpenocdBinPath() {
        return openocdBinPath;
    }

    public void setOpenocdBinPath(String openocdBinPath) {
        this.openocdBinPath = openocdBinPath;
    }

    public static class Esp32DebugTargetConfig {
        private String target;
        private String toolchain;
        private String openocdCfg;
        private String gdb;
        private String gdbPath;

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getToolchain() {
            return toolchain;
        }

        public void setToolchain(String toolchain) {
            this.toolchain = toolchain;
        }

        public String getOpenocdCfg() {
            return openocdCfg;
        }

        public void setOpenocdCfg(String openocdCfg) {
            this.openocdCfg = openocdCfg;
        }

        public String getGdb() {
            return gdb;
        }

        public void setGdb(String gdb) {
            this.gdb = gdb;
        }

        public String getGdbPath() {
            return gdbPath;
        }

        public void setGdbPath(String gdbPath) {
            this.gdbPath = gdbPath;
        }
    }

}
