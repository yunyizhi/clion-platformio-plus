package org.btik.platformioplus.ui.model;

/**
 * 任务树用户对象
 *
 * @author lustre
 * @since 2022/10/11 23:00
 */
public class PioTaskTreeNode {

    private final String displayName;


    @Override
    public String toString() {
        return displayName;
    }

    public PioTaskTreeNode(String displayName) {
        this.displayName = displayName;
    }
}
