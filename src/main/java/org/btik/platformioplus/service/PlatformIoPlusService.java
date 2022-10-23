package org.btik.platformioplus.service;

import java.util.function.Consumer;

/**
 * @author lustre
 * @since 2022/10/23 15:42
 */
public interface PlatformIoPlusService {

    void registerShutdownHook(String id, Runnable shutdownHook);

    void deregister(String id);

    void setEnable(boolean enable);

    boolean isEnable();

    void registerUIComponent(Consumer<Boolean> setEnable);

    void enableUIComponent();

}
