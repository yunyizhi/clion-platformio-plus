package org.btik.platformioplus.util;

import com.intellij.openapi.util.text.StringUtil;

/**
 * @author lustre
 * @since 2025/5/3 21:41
 */
public class IniUtils {

    /**
     * @param nameText section名称文本如： [env:xxx]
     * @return [text]中的内容 text
     * @throws NullPointerException 当nameText为空时
     */
    public static String getSectionName(String nameText) {
        if (StringUtil.isEmpty(nameText)) {
            return "";
        }
        return nameText.substring(1, nameText.length() - 1);
    }

    /**
     * @param nameText section名称文本如： [env:xxx]
     * @return [text]中的内容 text
     * @throws NullPointerException 当nameText为空时
     */
    public static String getEnvName(String nameText) {
        if (StringUtil.isEmpty(nameText) || nameText.length() <= 6) {
            return "";
        }
        return nameText.substring(5, nameText.length() - 1);
    }
}
