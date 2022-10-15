package org.btik.platformioplus;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;

import javax.swing.*;
import java.awt.*;

/**
 * @author lustre
 * @since 2022/10/15 10:02
 */
public class PioHomeToolWindow {
    private JPanel base;

    public PioHomeToolWindow(ToolWindow toolWindow) {

    }


    public JPanel getContent() {
        if (!JBCefApp.isSupported()) {
            base.add(new JLabel("your ide not support JCEF", SwingConstants.CENTER));
            return base;
        }
        JBCefBrowser myBrowser = new JBCefBrowser();

        base.add(myBrowser.getComponent(), BorderLayout.CENTER);
        // 暂时还没有执行命令打开控制台。需要手动执行 pio home --port 8008 --no-open
        myBrowser.loadURL("http://127.0.0.1:8008/");
        return base;
    }
}
