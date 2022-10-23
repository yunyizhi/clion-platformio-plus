package org.btik.platformioplus.setting;

import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.ide.BrowserUtil;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.util.SystemInfo;
import org.btik.platformioplus.util.Note;
import org.btik.platformioplus.util.SysConf;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static org.btik.platformioplus.util.Note.NOTIFICATION_GROUP;

/**
 * @author lustre
 * @since 2022/10/22 20:43
 */
public class PioConf {

    private static final String PIO_LOCATION_KEY = SysConf.get("pio.location.path");

    public static final String FILE_NAME = "platformio.ini";

    @NotNull
    public static String getPioLocation() {
        return PropertiesComponent.getInstance().getValue(PIO_LOCATION_KEY, "").trim();
    }

    public static String findPlatformio() {
        String platformioLocation = getPioLocation();
        if (!platformioLocation.isEmpty()) {
            return new File(platformioLocation).canExecute() ? platformioLocation : null;
        }
        if (SystemInfo.isWindows) {
            platformioLocation = PathEnvironmentVariableUtil.findExecutableInWindowsPath("platformio", null);
        } else {
            File file = PathEnvironmentVariableUtil.findInPath("platformio");
            platformioLocation = file == null ? null : file.getAbsolutePath();
        }
        return platformioLocation;
    }

    public static void openInstallGuide() {
        BrowserUtil.browse(SysConf.get("install.guide.link"));
    }

    public static void notifyPlatformioNotFound() {
        NOTIFICATION_GROUP
                .createNotification(Note.getMsg("pio.location.not.found"),
                        Note.getMsg("please.check.system.path"), NotificationType.ERROR)
                .addAction(NotificationAction.createSimple(Note.getMsg("install.guide"), PioConf::openInstallGuide))
                .notify(null);
    }

}
