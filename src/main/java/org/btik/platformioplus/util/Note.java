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
    public static NotificationGroup NOTIFICATION_GROUP =
            NotificationGroupManager.getInstance().getNotificationGroup("PlatformIO Plus");

    public Note(@NotNull String pathToBundle) {
        super(pathToBundle);
    }

    public static String getMsg(String key) {
        return INSTANCE.getResourceBundle().getString(key);
    }

    public static String getMsgF(String key, Object... o) {
        return String.format(getMsg(key), o);
    }
}
