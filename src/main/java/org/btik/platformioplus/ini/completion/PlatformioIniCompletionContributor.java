package org.btik.platformioplus.ini.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import ini4idea.lang.IniTokenTypes;
import ini4idea.lang.psi.IniProperty;
import ini4idea.lang.psi.IniSection;
import org.btik.platformioplus.ini.completion.entity.PioIniItemBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;

import static org.btik.platformioplus.ini.completion.IniMetaXmlConstant.ENV_SECTION_GROUP_NAME;
import static org.btik.platformioplus.ini.completion.IniMetaXmlConstant.ENV_SECTION_PREFIX;

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
                        PsiElement tempParent;
                        if ((tempParent = parameters.getPosition().getParent()) == null
                                || (tempParent = tempParent.getParent()) == null
                                || (tempParent = tempParent.getParent()) == null
                                || (tempParent = tempParent.getParent()) == null) {
                            return;
                        }

                        if (!(tempParent instanceof IniSection section)) {
                            return;
                        }

                        String text = section.getNameText();
                        if (text == null) {
                            return;
                        }
                        if (text.startsWith(ENV_SECTION_PREFIX)) {
                            text = ENV_SECTION_GROUP_NAME;
                        }
                        Set<PioIniItemBuilder> pioIniItemBuilders = PlatformioIniMetaFactory.getKeys().get(text);
                        if (pioIniItemBuilders == null) {
                            return;
                        }
                        for (PioIniItemBuilder key : pioIniItemBuilders) {
                            if (key.filter() == null || key.filter().test(section)) {
                                resultSet.addElement(key.elementBuilder());
                            }
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

        extend(CompletionType.BASIC, PlatformPatterns.psiElement(IniTokenTypes.MULTILINE_VALUE_PART), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                HashMap<String, Set<PioIniItemBuilder>> values = PlatformioIniMetaFactory.getValues();
                PsiElement tempParent;
                if ((tempParent = parameters.getPosition().getParent()) == null
                        || (tempParent = tempParent.getParent()) == null) {
                    return;
                }
                PsiElement envPropertyElement = tempParent;
                if (!(envPropertyElement instanceof IniProperty iniProperty)) {
                    return;
                }

                String text = iniProperty.getIniKey().getText();
                Set<PioIniItemBuilder> pioIniItemBuilders = values.get(text);
                if (pioIniItemBuilders == null) {
                    return;
                }
                tempParent = tempParent.getParent();
                if (!(tempParent instanceof IniSection section)) {
                    return;
                }
                for (PioIniItemBuilder value : pioIniItemBuilders) {
                    if (value.filter() == null || value.filter().test(section)) {
                        result.addElement(value.elementBuilder());
                    }
                }
            }
        });
    }

}
