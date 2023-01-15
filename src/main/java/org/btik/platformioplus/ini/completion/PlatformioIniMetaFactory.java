package org.btik.platformioplus.ini.completion;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.notification.NotificationType;
import org.btik.platformioplus.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

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
    private final List<LookupElementBuilder> keys = new ArrayList<>();

    private final List<LookupElementBuilder> sections = new ArrayList<>();

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
            String name = section.getAttribute(NAME);
            sections.add(LookupElementBuilder.create(name));
            eachByTagName(section, KEY, (key) -> keys.add(LookupElementBuilder
                    .create(key.getTextContent())
                    .withTypeText('[' + name + ']', true)
                    .bold()));
        });

    }

    public static List<LookupElementBuilder> getKeys() {
        return INSTANCE.keys;
    }

    public static List<LookupElementBuilder> getSections() {
        return INSTANCE.sections;
    }
}
