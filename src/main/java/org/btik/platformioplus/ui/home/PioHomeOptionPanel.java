package org.btik.platformioplus.ui.home;

import com.intellij.openapi.actionSystem.*;
import com.intellij.ui.jcef.JBCefBrowser;

import javax.swing.*;
import java.awt.*;

/**
 * @author lustre
 * @since 2022/10/15 10:02
 */
public class PioHomeOptionPanel extends JPanel {
    private static final String GROUP_ID = "pio.restartHome";


    public PioHomeOptionPanel() {
        super(new BorderLayout());
        ActionManager actionManager = ActionManager.getInstance();
        AnAction action = actionManager.getAction(GROUP_ID);
        ActionToolbar actionToolbar = actionManager.createActionToolbar(ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
                (ActionGroup) action, true);

        actionToolbar.setTargetComponent(this);
        add(actionToolbar.getComponent(), BorderLayout.NORTH);
    }

    public void print(String text) {

    }
}
