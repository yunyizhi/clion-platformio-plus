package org.btik.platformioplus.ui.model;

import org.btik.platformioplus.ui.action.OpenPioHome;
import org.btik.platformioplus.ui.action.TreeNodeAction;

/**
 * @author lustre
 * @since 2022/10/15 9:17
 */
public class PioHomeNode extends CommandNode {

    private final TreeNodeAction open;

    public PioHomeNode(String displayName, String command) {
        super(displayName, command);
        this.open = new OpenPioHome();
    }

    public void run() {
        this.open.run();
    }

}
