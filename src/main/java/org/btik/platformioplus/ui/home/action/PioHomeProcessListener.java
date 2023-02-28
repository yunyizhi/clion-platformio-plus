package org.btik.platformioplus.ui.home.action;

import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import org.btik.platformioplus.service.PlatformIoPlusService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author lustre
 * @since 2022/10/23 10:41
 */
public class PioHomeProcessListener implements ProcessListener {
    private JBCefBrowser myBrowser;

    private final JComponent component;

    private ProcessHandler processHandler;

    private PlatformIoPlusService platformIoPlusService;

    private static final String SHUTDOWN_HOOK_ID = "PioHome";

    @Override
    public void startNotified(@NotNull ProcessEvent processEvent) {

        if (!JBCefApp.isSupported()) {
            component.add(new JLabel("your ide not support JCEF", SwingConstants.CENTER));
        }
        if (myBrowser == null) {
            myBrowser = new JBCefBrowser();
            component.add(myBrowser.getComponent(), BorderLayout.CENTER);
            this.processHandler = processEvent.getProcessHandler();
        }


    }

    @Override
    public void processTerminated(@NotNull ProcessEvent processEvent) {
        if (myBrowser != null) {
            myBrowser.dispose();
        }
        if (platformIoPlusService != null) {
            platformIoPlusService.deregister(SHUTDOWN_HOOK_ID);
            platformIoPlusService.pioHomeUrl(null);
        }

    }

    @Override
    public void onTextAvailable(@NotNull ProcessEvent processEvent, @NotNull Key key) {
        String text = processEvent.getText();
        System.out.print(text);
        if (!text.contains(" URL => http://")) {
            return;
        }
        String[] split = text.split("=>");
        String url = split[1].trim();
        platformIoPlusService.pioHomeUrl(url);
        myBrowser.loadURL(url);
    }


    public PioHomeProcessListener(JComponent component, @Nullable Project project) {
        this.component = component;
        if (project != null) {
            platformIoPlusService = project.getService(PlatformIoPlusService.class);
            platformIoPlusService.registerShutdownHook(SHUTDOWN_HOOK_ID, this::shutDown);
        }

    }

    public void shutDown() {
        if (processHandler == null) {
            return;
        }
        if (!isAlive()) {
            return;
        }
        processHandler.destroyProcess();
        if (isAlive()) {
            if (processHandler instanceof OSProcessHandler osProcessHandler) {
                Process process = osProcessHandler.getProcess();
                process.destroy();
                if (isAlive()) {
                    process.destroyForcibly();
                }
            }
        }


    }

    public boolean isAlive() {
        if (processHandler == null) {
            return false;
        }
        if (processHandler instanceof OSProcessHandler) {
            return ((OSProcessHandler) processHandler).getProcess().isAlive();
        }
        return processHandler.getExitCode() != null;
    }

}
