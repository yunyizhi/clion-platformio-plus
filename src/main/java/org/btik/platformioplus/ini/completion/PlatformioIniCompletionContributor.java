package org.btik.platformioplus.ini.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import ini4idea.lang.IniTokenTypes;
import org.jetbrains.annotations.NotNull;

/**
 * @author lustre
 * @since 2022/12/15 19:50
 */
public class PlatformioIniCompletionContributor extends CompletionContributor {

    public PlatformioIniCompletionContributor() {

        extend(CompletionType.BASIC, PlatformPatterns.psiElement(IniTokenTypes.KEY_NAME),
                new CompletionProvider<>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        for (LookupElementBuilder key : PlatformioIniMetaFactory.getKeys()) {
                            resultSet.addElement(key);
                        }
                    }
                }
        );

        extend(CompletionType.BASIC, PlatformPatterns.psiElement(IniTokenTypes.SECTION_NAME),
                new CompletionProvider<>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        for (LookupElementBuilder section : PlatformioIniMetaFactory.getSections()) {
                            resultSet.addElement(section);
                        }
                    }
                }
        );
    }

}
