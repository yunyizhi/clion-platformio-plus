package org.btik.platformioplus.ui.home;

import com.intellij.openapi.actionSystem.*;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

/**
 * @author lustre
 * @since 2022/10/15 10:02
 */
public class PioHomeOptionPanel extends JPanel {
    private static final String GROUP_ID = "pio.restartHome";

    JTextArea consoleArea;

    public PioHomeOptionPanel() {
        super(new BorderLayout());
        ActionManager actionManager = ActionManager.getInstance();
        AnAction action = actionManager.getAction(GROUP_ID);
        ActionGroup actionGroup = (ActionGroup) action;
        ActionToolbar actionToolbar = actionManager.createActionToolbar(ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
                actionGroup, true);
        actionToolbar.setTargetComponent(this);
        add(actionToolbar.getComponent(), BorderLayout.NORTH);
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        JBScrollPane scrollPane = new JBScrollPane(consoleArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void print(String text) {
        consoleArea.append(text);
    }

    public void clearConsole() {
        consoleArea.setText("");
    }
}
