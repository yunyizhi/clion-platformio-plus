package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;

/**
 * @author lustre
 * @since 2022/12/18 13:40
 */
public interface ToolBarStatus {

    void setVisible(boolean visible);


    void register(FloatingToolbarComponent component);
}
