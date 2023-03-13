package org.btik.platformioplus.ini.completion.filter;

import com.intellij.psi.PsiElement;
import ini4idea.lang.psi.IniProperty;
import ini4idea.lang.psi.IniValue;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Predicate;

import static org.btik.platformioplus.ini.completion.IniMetaXmlConstant.*;

/**
 * @author lustre
 * @since 2023/3/13 18:40
 */
public class IniTipFilters {
    private static final HashMap<String, Predicate<PsiElement>> FILTER_MAP = new HashMap<>();

    static {
        FILTER_MAP.put("esp8266Platform", IniTipFilters::esp8266Platform);
        FILTER_MAP.put("esp32Platform", IniTipFilters::esp32Platform);
    }

    public static Predicate<PsiElement> getFilter(String name) {
        return FILTER_MAP.get(name);
    }

    /**
     * @param envPropertyElement 为env下的属性
     */
    public static boolean esp8266Platform(PsiElement envPropertyElement) {
        @NotNull PsiElement[] siblingElements = envPropertyElement.getParent().getChildren();
        for (PsiElement siblingElement : siblingElements) {
            if (siblingElement instanceof IniProperty iniProperty) {
                String keyName = iniProperty.getIniKey().getText();
                if (PLATFORM.equals(keyName)) {
                    IniValue iniValue = iniProperty.getIniValue();
                    return iniValue != null && ESPRESSIF_8266.equals(iniValue.getText());
                }
            }
        }
        return false;
    }

    /**
     * @param envPropertyElement 为env下的属性
     */
    public static boolean esp32Platform(PsiElement envPropertyElement) {
        @NotNull PsiElement[] siblingElements = envPropertyElement.getParent().getChildren();
        for (PsiElement siblingElement : siblingElements) {
            if (siblingElement instanceof IniProperty iniProperty) {
                String keyName = iniProperty.getIniKey().getText();
                if (PLATFORM.equals(keyName)) {
                    IniValue iniValue = iniProperty.getIniValue();
                    return iniValue != null && ESPRESSIF_32.equals(iniValue.getText());
                }
            }
        }
        return false;
    }


}
