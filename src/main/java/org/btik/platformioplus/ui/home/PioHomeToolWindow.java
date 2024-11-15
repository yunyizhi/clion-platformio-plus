package org.btik.platformioplus.ui.home;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import org.btik.platformioplus.ui.home.action.PioHomeProcessListener;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author lustre
 * @since 2022/10/15 10:02
 */
public class PioHomeToolWindow extends JPanel {
    private static final Logger LOG = Logger.getInstance(PioHomeToolWindow.class);
    private final Consumer<String> urlLoader;
    private String url;

    public PioHomeToolWindow() {
        super(new BorderLayout());
        if (JBCefApp.isSupported()) {
            var jbCefBrowser = new JBCefBrowser();
            add(jbCefBrowser.getComponent(), BorderLayout.CENTER);
            urlLoader = jbCefBrowser::loadURL;
        } else {
            add(new JLabel("not Support JBCef"), BorderLayout.CENTER);
            urlLoader = url -> LOG.warn("not loader url");
        }

    }

    public void loadURL(String url) {
        if (Objects.equals(this.url, url)) {
            return;
        }
        this.url = url;
        urlLoader.accept(url);
    }

    public void loadURL(String url, boolean forceLoad) {
        if (forceLoad) {
            this.url = url;
            urlLoader.accept(url);
            return;
        }
        loadURL(url);
    }

}
