package org.btik.platformioplus.run.config.esp32.debug;

/**
 * @author lustre
 * @since 2025/5/4 0:12
 */
public interface Esp32Const {

    String[] OPENOCD_DIR = {"tool-openocd-esp32", "bin"};

    String[] ESP32_TOOL_CHAIN_DIR = {"toolchain-xtensa-esp32", "bin"};

    String[] ESP32S2_TOOL_CHAIN_DIR = {"toolchain-xtensa-esp32s2", "bin"};

    String[] ESP32S3_TOOL_CHAIN_DIR = {"toolchain-xtensa-esp32s3", "bin"};

    String[] ESP32_RISCV_TOOL_CHAIN_DIR = {"toolchain-riscv32-esp", "bin"};


}
