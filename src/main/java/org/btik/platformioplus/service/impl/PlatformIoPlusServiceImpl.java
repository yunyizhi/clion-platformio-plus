package org.btik.platformioplus.service.impl;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.Disposable;
import org.btik.platformioplus.service.PlatformIoPlusService;

import java.awt.*;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static org.btik.platformioplus.util.Note.NOTIFICATION_GROUP;
import static org.btik.platformioplus.util.Note.getMsg;

/**
 * @author lustre
 * @since 2022/10/23 15:35
 */
public class PlatformIoPlusServiceImpl implements Disposable, PlatformIoPlusService {
    private final ConcurrentHashMap<String, Runnable> shutdownCallback = new ConcurrentHashMap<>();

    private final Set<Consumer<Boolean>> UI_ENABLE_FUN = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private boolean isEnable;

    private static String pioHomeUrl;

    @Override
    public void dispose() {

        shutdownCallback.forEach((key, value) -> {
            try {
                value.run();
            } catch (Exception e) {
                NOTIFICATION_GROUP.createNotification(getMsg("notification.group.platformio-plus"),
                        getMsg("unexpected.exception"), NotificationType.WARNING).notify(null);
            }
        });
        shutdownCallback.clear();

    }


    @Override
    public void registerShutdownHook(String id, Runnable shutdownHook) {
        shutdownCallback.put(id, shutdownHook);
    }

    @Override
    public void deregister(String id) {
        shutdownCallback.remove(id);
    }

    @Override
    public void setEnable(boolean enable) {
        this.isEnable = enable;
        EventQueue.invokeLater(() -> {
            for (Consumer<Boolean> booleanConsumer : UI_ENABLE_FUN) {
                try {
                    booleanConsumer.accept(enable);
                } catch (Exception e) {
                    NOTIFICATION_GROUP.createNotification(getMsg("notification.group.platformio-plus"),
                            getMsg("load.pio.plus.failed"), NotificationType.WARNING).notify(null);
                }
            }
        });

    }

    @Override
    public boolean isEnable() {
        return isEnable;
    }

    @Override
    public void registerUIComponent(Consumer<Boolean> setEnable) {
        UI_ENABLE_FUN.add(setEnable);
    }

    @Override
    public void enableUIComponent() {
        for (Consumer<Boolean> booleanConsumer : UI_ENABLE_FUN) {
            try {
                booleanConsumer.accept(true);
            } catch (Exception e) {
                NOTIFICATION_GROUP.createNotification(getMsg("notification.group.platformio-plus"),
                        getMsg("load.pio.plus.failed"), NotificationType.WARNING).notify(null);
            }
        }
    }

    @Override
    public String pioHomeUrl() {
        return pioHomeUrl;
    }

    @Override
    public String pioHomeUrl(String pioHomeUrl) {
        PlatformIoPlusServiceImpl.pioHomeUrl = pioHomeUrl;
        return PlatformIoPlusServiceImpl.pioHomeUrl;
    }
}
