package org.btik.platformioplus.ini.completion;


/**
 * @author lustre
 * @since 2022/12/16 17:21
 */
public interface IniMetaXmlConstant {
    /*tag*/
    String INI_META = "IniMeta";

    String SECTION = "Section";

    String KEY = "Key";

    /*attr*/
    String NAME = "name";

    String DESC = "desc";

    String VALUE = "value";

    String FILTER = "filter";
// key tip
    String ENV_SECTION_PREFIX = "[env:";

    String ENV_SECTION_GROUP_NAME = "[env:]";

    String KEY_TIP_SUFFIX = " = ";

    int KEY_TIP_SKIP_LEN = KEY_TIP_SUFFIX.length();

    String PLATFORM = "platform";

    String ESPRESSIF_32 = "espressif32";

    String ESPRESSIF_8266 = "espressif8266";
}
