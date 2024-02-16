package org.btik.platformioplus.service;

import java.util.function.Consumer;

/**
 * @author lustre
 * @since 2022/10/23 15:42
 */
public interface PlatformIoPlusService {
   String TASK_WINDOW = "Pio Plus Tasks";

   String HOME_WINDOW = "Pio Home";

   String[] WINDOW_ARRAY = new String[] {TASK_WINDOW, HOME_WINDOW};


    void registerShutdownHook(String id, Runnable shutdownHook);

    void deregister(String id);

    void setEnable(boolean enable);

    boolean isEnable();

    void registerUIComponent(Consumer<Boolean> setEnable);

    void enableUIComponent();

    String pioHomeUrl();

    String pioHomeUrl(String pioHomeUrl);

}
