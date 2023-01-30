package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;


import java.util.HashSet;
import java.util.Objects;


/**
 * @author lustre
 * @since 2022/12/18 13:40
 */
public class ToolBarStatusImpl implements ToolBarStatus {

    private final HashSet<FloatingToolbarComponent> floatingToolbarComponents = new HashSet<>();

    private boolean isVisible = false;


    private CharSequence lastSaveCharSequence = "";

    private CharSequence lastUnSaveCharSequence = "";


    private volatile boolean fileChange;



    private void setVisible(boolean visible) {
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
        component.scheduleHide();
    }

    @Override
    public void update(CharSequence charSequence) {
        lastUnSaveCharSequence = charSequence;

        // 文件回退则隐藏，否则依然存在差异则显示
        fileChange = !Objects.equals(lastSaveCharSequence, charSequence);
        setVisible(fileChange);
    }

    @Override
    public void saveChangeAndHide() {
        lastSaveCharSequence = lastUnSaveCharSequence;
        fileChange = false;
        setVisible(false);
    }

}
