package org.btik.platformioplus.ini.completion;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.editor.Editor;
import org.btik.platformioplus.ini.completion.entity.PioIniItemBuilder;
import org.btik.platformioplus.ini.completion.filter.IniTipFilters;
import org.btik.platformioplus.util.DomUtil;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.btik.platformioplus.ini.completion.IniMetaXmlConstant.*;
import static org.btik.platformioplus.util.DomUtil.eachByTagName;
import static org.btik.platformioplus.util.Note.*;
import static org.btik.platformioplus.util.Note.getMsg;

/**
 * platformio.ini提示key
 *
 * @author lustre
 * @since 2022/12/16 17:07
 */
public class PlatformioIniMetaFactory {
    private PlatformioIniMetaFactory() {
    }

    public static PlatformioIniMetaFactory INSTANCE = new PlatformioIniMetaFactory();
    private final HashMap<String, Set<LookupElementBuilder>> keys = new HashMap<>();

    private final Set<LookupElementBuilder> sections = new HashSet<>();

    private final HashMap<String, Set<PioIniItemBuilder>> values = new HashMap<>();

    public void load() {
        Element documentElement;
        try {
            Document treeConf = DomUtil.parse(PlatformioIniMetaFactory.class.getResourceAsStream("/pioplus/platformioIniMeta.xml"));
            documentElement = treeConf.getDocumentElement();

        } catch (Exception e) {
            NOTIFICATION_GROUP.createNotification(getMsg("notification.group.platformio-plus"),
                    getMsgF("platformio.ini.meta.load.failed", e.getMessage()), NotificationType.ERROR).notify(null);
            return;
        }
        eachByTagName(documentElement, SECTION, (section) -> {
            String sectionName = section.getAttribute(NAME);
            // 创建section名称的提示
            sections.add(LookupElementBuilder.create(sectionName));
            String desc = section.getAttribute(DESC);
            String sectionKey = '[' + sectionName + ']';
            eachByTagName(section, KEY, (key) -> {
                String keyName = key.getAttribute(NAME);

                keys.computeIfAbsent(sectionKey, (sectionName_) -> new HashSet<>())
                        // 创建 key的提示 并补全等号  "key ="
                        .add(LookupElementBuilder
                                .create(keyName).withInsertHandler(PlatformioIniMetaFactory::fixKeyTipSuffix)
                                .withTypeText(desc, true)
                                .bold());

                eachByTagName(key, VALUE, (value) -> {
                    String valueName = value.getTextContent().trim();
                    String filter = value.getAttribute(FILTER);
                    values.computeIfAbsent(keyName, (keyName1) -> new HashSet<>())
                            .add(new PioIniItemBuilder(LookupElementBuilder
                                    .create(valueName)
                                    .withTypeText(keyName)
                                    .bold(), IniTipFilters.getFilter(filter)));
                });
            });
        });

    }

    /**
     * 补全空格等号
     */
    private static void fixKeyTipSuffix(@NotNull InsertionContext context, @NotNull LookupElement item) {
        int tailOffset = context.getTailOffset();
        Editor editor = context.getEditor();
        editor.getDocument().insertString(tailOffset, KEY_TIP_SUFFIX);
        editor.getCaretModel().moveToOffset(tailOffset + KEY_TIP_SKIP_LEN);
    }

    public static HashMap<String, Set<LookupElementBuilder>> getKeys() {
        return INSTANCE.keys;
    }

    public static Set<LookupElementBuilder> getSections() {
        return INSTANCE.sections;
    }

    public static HashMap<String, Set<PioIniItemBuilder>> getValues() {
        return INSTANCE.values;
    }
}
