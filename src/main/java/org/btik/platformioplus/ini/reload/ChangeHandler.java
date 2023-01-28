package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.project.Project;

/**
 * @author lustre
 * @since 2022/12/18 11:18
 */
public interface ChangeHandler {
    String RE_INIT_DO = "re-init.do";

    void update(CharSequence charSequence);

    Project getProject();


}
