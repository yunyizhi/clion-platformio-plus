package org.btik.platformioplus.run.config.esp32.system;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.system.OS;
import org.btik.platformioplus.ini.completion.PlatformioIniMetaFactory;
import org.btik.platformioplus.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.nio.file.Path;

import static org.btik.platformioplus.run.config.esp32.system.Esp32DebugSysConfMeta.*;
import static org.btik.platformioplus.service.PlatformIoPlusConst.PACKAGES_PATH;
import static org.btik.platformioplus.util.DomUtil.eachByTagName;
import static org.btik.platformioplus.util.PathUtils.getPioSubPath;

/**
 * @author lustre
 * @since 2025/5/4 1:38
 */
public class Esp32DebugSysConfFactory {
    private static final boolean IS_WINDOWS = OS.CURRENT == OS.Windows;

    private final static Logger log = Logger.getInstance(Esp32DebugSysConfFactory.class);
    public static Esp32DebugSysConf load() {
        Esp32DebugSysConf esp32DebugSysConf = new Esp32DebugSysConf();
        Element documentElement;
        try {
            Document treeConf = DomUtil.parse(PlatformioIniMetaFactory.class.getResourceAsStream("/pioplus/esp32/esp32_debug.xml"));
            documentElement = treeConf.getDocumentElement();
        } catch (Exception e) {
            log.error("Error loading esp32 debug configuration", e);
            return esp32DebugSysConf;
        }
        Path packAgePath = getPioSubPath(PACKAGES_PATH);
        boolean packNotNull = packAgePath != null;
        Element targetConf = DomUtil.getFirstElementByName(documentElement, TARGET_CONF);
        if (targetConf != null) {
            eachByTagName(targetConf,TARGET, (target) ->{
                var targetConfig = new Esp32DebugSysConf.Esp32DebugTargetConfig();
                String targetType= target.getAttribute(TYPE).trim();
                targetConfig.setTarget(targetType);
                String toolchain = target.getAttribute(TOOLCHAIN).trim();
                targetConfig.setToolchain(toolchain);
                String gdb = target.getAttribute(GDB).trim();
                targetConfig.setGdb(gdb);
                String defaultOpenocdCfg = target.getAttribute(DEFAULT_OPENOCD_CFG).trim();
                targetConfig.setOpenocdCfg(defaultOpenocdCfg);
                if (packNotNull) {
                    String gdbPath = packAgePath.resolve(toolchain).resolve(BIN).resolve(IS_WINDOWS ? gdb + WIN_EXE_SUFFIX : gdb).toString();
                    targetConfig.setGdbPath(gdbPath);
                }
                esp32DebugSysConf.putTargetConfig(targetType, targetConfig);
            });
        }
        Element openocdCfg = DomUtil.getFirstElementByName(documentElement, OPENOCD_CONF);
        if (openocdCfg == null || !packNotNull) {
            return esp32DebugSysConf;
        }
        String openocdDir = openocdCfg.getAttribute(DIR).trim();
        esp32DebugSysConf.setOpenocdFolder(openocdDir);
        String openocdBinName = openocdCfg.getAttribute(NAME).trim();
        esp32DebugSysConf.setOpenocdBinName(openocdBinName);
        String openocdPath = packAgePath.resolve(openocdDir).resolve(BIN).resolve(IS_WINDOWS ? openocdBinName + WIN_EXE_SUFFIX : openocdBinName).toString();
        esp32DebugSysConf.setOpenocdBinPath(openocdPath);
        return esp32DebugSysConf;
    }
}
