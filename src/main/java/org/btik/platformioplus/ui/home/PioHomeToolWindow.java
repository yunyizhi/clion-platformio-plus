package org.btik.platformioplus.ui.home;

import com.intellij.ui.jcef.JBCefBrowser;

import javax.swing.*;
import java.awt.*;

/**
 * @author lustre
 * @since 2022/10/15 10:02
 */
public class PioHomeToolWindow extends JPanel {

    JBCefBrowser jbCefBrowser;

    public PioHomeToolWindow() {
        super(new BorderLayout());
        jbCefBrowser = new JBCefBrowser();
        add(jbCefBrowser.getComponent(), BorderLayout.CENTER);
    }

    public void loadURL(String url) {
        jbCefBrowser.loadURL(url);
    }

}
