package org.btik.platformioplus.service;

import org.btik.platformioplus.run.config.esp32.system.Esp32DebugSysConf;
import org.btik.platformioplus.util.ClassMetaUtils;

import java.util.List;

/**
 * @author lustre
 * @since 2025/4/26 18:07
 */
public interface SystemMetaService {
    List<ClassMetaUtils.PropOptMeta> getEsp32PropOptMetas();

    Esp32DebugSysConf getEsp32DebugSysConf();
}
