package org.btik.platformioplus.service.impl;

import org.btik.platformioplus.run.config.esp32.debug.model.DebugConfigModel;
import org.btik.platformioplus.run.config.esp32.debug.model.Serial;
import org.btik.platformioplus.service.SystemMetaService;
import org.btik.platformioplus.util.ClassMetaUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.btik.platformioplus.util.ClassMetaUtils.isMod;

/**
 * @author lustre
 * @since 2025/4/26 18:07
 */
public class SystemMetaServiceImpl implements SystemMetaService {

    private List<ClassMetaUtils.PropOptMeta> propOptMetas;

    public SystemMetaServiceImpl() {
        parseDebugModelSerialMeta();
    }

    private void parseDebugModelSerialMeta() {
        propOptMetas = ClassMetaUtils.parseFieldsByAnnotation(DebugConfigModel.class, Serial.class);
        for (ClassMetaUtils.PropOptMeta propOptMeta : propOptMetas) {
            Method getter = propOptMeta.getter();
            Method setter = propOptMeta.setter();
            if (getter == null || !isMod(getter, Modifier.PUBLIC)
                    || setter == null || !isMod(setter, Modifier.PUBLIC)) {
                propOptMeta.field().setAccessible(true);
            }
        }
    }

    @Override
    public List<ClassMetaUtils.PropOptMeta> getEsp32PropOptMetas() {
        return propOptMetas;
    }
}
