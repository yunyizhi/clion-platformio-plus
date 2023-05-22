package org.btik.platformioplus.service;

import org.btik.platformioplus.setting.cmake.build.type.BuildTypeChangeListener;

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

    void setCurrentCmakeBuildTypeListener(BuildTypeChangeListener buildTypeChangeListener);

    void updateBuildType();

    void enableUIComponent();

    String pioHomeUrl();

    String pioHomeUrl(String pioHomeUrl);

}
