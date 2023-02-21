package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

/**
 * @author lustre
 * @since 2022/12/18 13:40
 */
public interface PioIniChangeHandler {

    void update(CharSequence charSequence);

    void saveChangeAndHide();


    void register(FloatingToolbarComponent component);

    /**
     *用于更新环境列表，需要返回原始引用
     */
    List<String> getEnvs();

    void bindEnvNode(DefaultMutableTreeNode envNode, Runnable updateUI);

    void loadEnvInFile( @NotNull PsiElement[] children);
}
