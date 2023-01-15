package org.btik.platformioplus.ui.model;

/**
 * 锁命令
 * @author lustre
 * @since 2023/1/16 0:44
 */
public class LockCommandNode extends CommandNode{
    private final String lock;
    public LockCommandNode(String displayName, String command, String lock) {
        super(displayName, command);
        this.lock = lock;
    }

    public String getLock() {
        return lock;
    }
}
