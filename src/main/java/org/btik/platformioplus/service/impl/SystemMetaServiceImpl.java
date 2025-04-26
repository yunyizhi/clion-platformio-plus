package org.btik.platformioplus.service.impl;

import org.btik.platformioplus.service.SystemMetaService;
import org.btik.platformioplus.util.ClassMetaUtils;

import java.util.List;

/**
 * @author lustre
 * @since 2025/4/26 18:07
 */
public class SystemMetaServiceImpl implements SystemMetaService {
    @Override
    public List<ClassMetaUtils.PropOptMeta> getEsp32PropOptMetas() {
        return List.of();
    }
}
