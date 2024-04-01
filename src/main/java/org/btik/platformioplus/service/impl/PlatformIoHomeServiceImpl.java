package org.btik.platformioplus.service.impl;

import org.btik.platformioplus.service.PlatformIoHomeService;

/**
 * @author lustre
 * @since 2024/4/1 23:46
 */
public class PlatformIoHomeServiceImpl implements PlatformIoHomeService {
    private String pioHomeUrl;

    @Override
    public String pioHomeUrl() {
        return pioHomeUrl;
    }

    @Override
    public String pioHomeUrl(String pioHomeUrl) {
        this.pioHomeUrl = pioHomeUrl;
        return this.pioHomeUrl;
    }
}
