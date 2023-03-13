package org.btik.platformioplus.ini.completion.entity;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;

import java.util.function.Predicate;

/**
 * @param filter 用来判断是否显示
 * @author lustre
 * @since 2023/3/13 18:20
 */
public record PioIniItemBuilder(LookupElementBuilder elementBuilder, Predicate<PsiElement> filter) {

}
