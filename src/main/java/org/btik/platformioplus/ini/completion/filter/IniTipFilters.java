package org.btik.platformioplus.ini.completion.filter;

import com.intellij.openapi.util.text.StringUtilRt;
import com.intellij.psi.PsiElement;
import ini4idea.lang.psi.IniKey;
import ini4idea.lang.psi.IniProperty;
import ini4idea.lang.psi.IniValue;
import org.btik.platformioplus.ini.completion.PlatformioIniMetaFactory;
import org.btik.platformioplus.ini.completion.entity.PlatformRule;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Predicate;

import static org.btik.platformioplus.ini.completion.IniMetaXmlConstant.*;

/**
 * @author lustre
 * @since 2023/3/13 18:40
 */
public class IniTipFilters {
    private static final HashMap<String, Predicate<PsiElement>> PLATFORM_FRAMEWORK_MAP = new HashMap<>();

    private static final HashMap<String, String> PLATFORM_NAME_MAP = new HashMap<>();

    public static Predicate<PsiElement> getFilter(String platform, String framework) {
        boolean platformEmpty = StringUtilRt.isEmpty(platform);
        boolean frameworkEmpty = StringUtilRt.isEmpty(framework);
        if (platformEmpty && frameworkEmpty) {
            return null;
        }
        return PLATFORM_FRAMEWORK_MAP
                .computeIfAbsent(platform + "/" + framework, (mapKey) -> {
                    if (!platformEmpty && !frameworkEmpty) {
                        return getFilterByPlatformAndFramework(platform, framework);
                    }
                    String key = platformEmpty ? FRAMEWORK : PLATFORM;
                    String value = platformEmpty ? framework : platform;
                    return filterByKv(key, value);
                });

    }

    private static Predicate<PsiElement> filterByKv(String key, String value) {
        return (env) -> {
            @NotNull PsiElement[] siblingElements = env.getChildren();
            for (PsiElement siblingElement : siblingElements) {
                if (siblingElement instanceof IniProperty iniProperty) {
                    IniKey iniKey = iniProperty.getIniKey();
                    if (key.equals(iniKey.getText())) {
                        IniValue iniValue = iniProperty.getIniValue();
                        return iniValue != null && value.equals(iniValue.getText());
                    }
                }
            }
            return false;
        };
    }

    private static Predicate<PsiElement> getFilterByPlatformAndFramework(String confPlatform, String framework) {
        return (env) -> {
            String readPlatform = null;
            String framewk = null;
            @NotNull PsiElement[] siblingElements = env.getChildren();
            for (PsiElement siblingElement : siblingElements) {
                if (siblingElement instanceof IniProperty iniProperty) {
                    IniKey iniKey = iniProperty.getIniKey();
                    if (FRAMEWORK.equals(iniKey.getText())) {
                        IniValue iniValue = iniProperty.getIniValue();
                        if (iniValue != null) {
                            framewk = iniValue.getText();
                        }
                    }
                    if (PLATFORM.equals(iniKey.getText())) {
                        IniValue iniValue = iniProperty.getIniValue();
                        if (iniValue != null) {
                            readPlatform = iniValue.getText();
                        }
                    }
                }
            }
            if (!Objects.equals(framework, framewk)) {
                return false;
            }
            if (Objects.equals(confPlatform, readPlatform)) {
                return true;
            }
            if (Objects.equals(confPlatform, PLATFORM_NAME_MAP.get(readPlatform))) {
                return true;
            }
            if (readPlatform == null) {
                return false;
            }
            ArrayList<PlatformRule> platformRules = PlatformioIniMetaFactory.getPlatformRules();
            for (PlatformRule platformRule : platformRules) {
                if (platformRule.pattern().matcher(readPlatform).matches()){
                    PLATFORM_NAME_MAP.put(confPlatform, readPlatform);
                    return true;
                }
            }
            return false;
        };

    }

    public static void init() {
        ArrayList<PlatformRule> platformRules = PlatformioIniMetaFactory.getPlatformRules();
        for (PlatformRule platformRule : platformRules) {
            PLATFORM_NAME_MAP.put(platformRule.platform(), platformRule.platform());
        }
    }
}

