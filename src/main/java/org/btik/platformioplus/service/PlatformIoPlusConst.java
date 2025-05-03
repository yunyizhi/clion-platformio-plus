package org.btik.platformioplus.service;


/**
 * @author lustre
 * @since 2022/10/23 15:42
 */
public interface PlatformIoPlusConst {
    String TASK_WINDOW = "Pio Plus Tasks";

    String HOME_WINDOW = "Pio Home";

    String[] WINDOW_ARRAY = new String[]{TASK_WINDOW, HOME_WINDOW};

    String PIO_HOME_CONTENT_ID = "Pio Home";

    String ENV_SECTION_PREFIX = "[env:";

    String PIO_HOME_OPT_CONTENT_ID = "Options";
    String PLATFORMS = "platforms";

    String PLATFORM_IO = ".platformio";

    String PIO = ".pio";

    String PACKAGES = "packages";

    String[][] PLATFORMS_PATH = {
            {PLATFORM_IO, PLATFORMS},
            {PIO, PLATFORMS},
    };

    String[][] PACKAGES_PATH = {
            {PLATFORM_IO, PACKAGES},
            {PIO, PACKAGES},
    };
}
