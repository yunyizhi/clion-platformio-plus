package org.btik.platformioplus.ini.completion.entity;

import java.util.regex.Pattern;

/**
 * @author lustre
 * @since 2025/4/13 19:24
 */
public record PlatformRule(Pattern pattern, String platform) {
}
