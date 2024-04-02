package org.btik.platformioplus.ui.home;

import com.intellij.ui.jcef.JBCefBrowser;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author lustre
 * @since 2022/10/15 10:02
 */
public class PioHomeToolWindow extends JPanel {

    private final JBCefBrowser jbCefBrowser;
    private String url;

    public PioHomeToolWindow() {
        super(new BorderLayout());
        jbCefBrowser = new JBCefBrowser();
        add(jbCefBrowser.getComponent(), BorderLayout.CENTER);
    }

    public void loadURL(String url) {
        if (Objects.equals(this.url, url)) {
            return;
        }
        this.url = url;
        jbCefBrowser.loadURL(url);
    }

    public void loadURL(String url, boolean forceLoad) {
        if (forceLoad) {
            this.url = url;
            jbCefBrowser.loadURL(url);
            return;
        }
        loadURL(url);
    }

}
