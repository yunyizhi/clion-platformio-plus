package org.btik.platformioplus.icon;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author lustre
 * @since 2025/4/26 15:46
 */
public class PlatformIoPlusIcon {
    private static @NotNull Icon load(@NotNull String path) {
        return IconLoader.getIcon(path, PlatformIoPlusIcon.class);
    }

    public static final @NotNull Icon PIOPLUS = load("/pioplus/platformio.svg");

    public static final @NotNull Icon PIOPLUS_13 = load("/pioplus/platformio_13.svg");

    public static final @NotNull Icon ESP32_16 = load("/pioplus/esp3216_16.svg");
}
