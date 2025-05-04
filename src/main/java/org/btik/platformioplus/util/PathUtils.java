package org.btik.platformioplus.util;

import com.intellij.util.SystemProperties;

import java.nio.file.Files;
import java.nio.file.Path;



/**
 * @author lustre
 * @since 2025/5/4 0:03
 */
public class PathUtils {
    public static Path getPioSubPath(String[][] paths) {
        String userHome = SystemProperties.getUserHome();
        Path result = null;
        for (String[] platformPaths : paths) {
            Path path = Path.of(userHome, platformPaths);
            if (Files.exists(path)) {
                result = path;
                break;
            }
        }
        return result;
    }
}
