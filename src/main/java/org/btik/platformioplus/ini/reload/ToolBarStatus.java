package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;

/**
 * @author lustre
 * @since 2022/12/18 13:40
 */
public interface ToolBarStatus {

    void update(CharSequence charSequence);
    void saveChangeAndHide();


    void register(FloatingToolbarComponent component);
}
