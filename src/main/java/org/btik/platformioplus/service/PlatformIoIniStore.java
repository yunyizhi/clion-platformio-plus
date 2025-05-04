package org.btik.platformioplus.service;

import com.intellij.psi.PsiFile;
import org.btik.platformioplus.ini.PioIniSectionBean;

import java.util.List;

/**
 * @author lustre
 * @since 2025/5/3 17:30
 */
public interface PlatformIoIniStore extends PlatformIoPlusConst {

    PsiFile getPlatformIoIni();

    PioIniSectionBean getCurrentSection();

    List<PioIniSectionBean> getSections();
}
