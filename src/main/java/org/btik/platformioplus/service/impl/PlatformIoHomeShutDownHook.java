package org.btik.platformioplus.service.impl;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.application.ApplicationManager;
import org.btik.platformioplus.service.PlatformIoHomeService;

/**
 * @author lustre
 * @since 2024/4/2 23:34
 */
public class PlatformIoHomeShutDownHook implements AppLifecycleListener {
    @Override
    public void appWillBeClosed(boolean isRestart) {
        PlatformIoHomeService service = ApplicationManager.getApplication().getService(PlatformIoHomeService.class);
        service.shutDown();
    }
}
