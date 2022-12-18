package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;



import java.util.HashSet;

/**
 * @author lustre
 * @since 2022/12/18 13:40
 */
public class ToolBarStatusImpl implements ToolBarStatus {

    private final HashSet<FloatingToolbarComponent> floatingToolbarComponents = new HashSet<>();

    private boolean isVisible = true;

    @Override
    public void setVisible(boolean visible) {
        if (visible == isVisible) {
            return;
        }
        isVisible = visible;
        if (visible) {
            for (FloatingToolbarComponent floatingToolbarComponent : floatingToolbarComponents) {
                floatingToolbarComponent.scheduleShow();
            }
            return;
        }

        for (FloatingToolbarComponent floatingToolbarComponent : floatingToolbarComponents) {
            floatingToolbarComponent.scheduleHide();
        }
    }

    @Override
    public void register(FloatingToolbarComponent component) {
        floatingToolbarComponents.add(component);
        component.scheduleShow();
    }

}
