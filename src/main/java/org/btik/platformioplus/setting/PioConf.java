package org.btik.platformioplus.setting;

import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.ide.BrowserUtil;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.util.SystemProperties;
import org.btik.platformioplus.util.Note;
import org.btik.platformioplus.util.SysConf;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

import static org.btik.platformioplus.util.Note.NOTIFICATION_GROUP;

/**
 * @author lustre
 * @since 2022/10/22 20:43
 */
public class PioConf {

    private static final String PIO_LOCATION_KEY = SysConf.get("pio.location.path");

    public static final String FILE_NAME = "platformio.ini";

    private static final String[][] DEFAULT_PIO_PATH = {
            {".platformio", "penv", "Scripts"},
            {".platformio", "penv", "bin"},
            {".pio", "penv", "Scripts"},
            {".pio", "penv", "bin"}
    };

    @NotNull
    public static String getPioLocation() {
        return PropertiesComponent.getInstance().getValue(PIO_LOCATION_KEY, "").trim();
    }

    public static String findPlatformio() {
        String result;
        Path path;
        // 读取settings
        String platformioLocation = getPioLocation();
        if (!platformioLocation.isEmpty()) {
            path = Path.of(platformioLocation, "penv", "Scripts");
            result = getExecutablePath(path);
            if (null != result) {
                return result;
            }
            return Path.of(platformioLocation, "penv", "bin").resolve("platformio").toString();
        }
        // 从PATH变量中找 platformio
        File pio = PathEnvironmentVariableUtil.findExecutableInPathOnAnyOS("platformio");
        if (pio != null) {
            return pio.toPath().toString();
        }
        // 从当前用户安装目录下的 .platformio 或者 .pio目录查找
        String userHome = SystemProperties.getUserHome();
        for (String[] folderPath : DEFAULT_PIO_PATH) {
            path = Path.of(userHome, folderPath);
            result = getExecutablePath(path);
            if (null != result) {
                return result;
            }
        }
        return null;
    }

    private static String getExecutablePath(Path path) {
        if (path.toFile().exists()) {
            return path.resolve("platformio").toString();
        }
        return null;
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
