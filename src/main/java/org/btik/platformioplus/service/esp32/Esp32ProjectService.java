package org.btik.platformioplus.service.esp32;

import org.btik.platformioplus.run.config.esp32.debug.build.Esp32BuildTarget;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author lustre
 * @since 2025/4/26 18:10
 */
public interface Esp32ProjectService {

    @NotNull List<Esp32BuildTarget> getBuildTargets();
}
