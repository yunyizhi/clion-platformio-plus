package org.btik.platformioplus.util;

import com.intellij.DynamicBundle;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author lustre
 * @since 2022/10/21 1:15
 */
public class Note extends DynamicBundle {
    private static final Note INSTANCE = new Note("messages.PlatformioPlusBundle");

    /**
     * 按需获取通知组，避免在类静态初始化阶段请求服务
     * （2026.2 起平台禁止 <clinit> 依赖服务）
     */
    public static NotificationGroup notificationGroup() {
        return NotificationGroupManager.getInstance().getNotificationGroup("PlatformIO Plus");
    }

    public Note(@NotNull String pathToBundle) {
        super(pathToBundle);
    }

    public static String getMsg(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        return INSTANCE.messageOrDefault(key, key);
    }

    public static String $i18n(String key) {
        return getMsg(key);
    }

    public static String $note(String key) {
        return getMsg(key);
    }

    public static String getMsgF(String key, Object... o) {
        return String.format(getMsg(key), o);
    }
}
