package org.btik.platformioplus.run.config;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author lustre
 * @since 2023/5/18 11:25
 */
public class PioPlusRunTaskProfile implements RunProfileState {

    private final PioPlusRunConfig pioPlusRunConfig;

    public PioPlusRunTaskProfile(Project project, PioPlusRunConfig pioPlusRunConfig) {
        this.pioPlusRunConfig = pioPlusRunConfig;
    }

    @Override
    public @Nullable ExecutionResult execute(Executor executor, @NotNull ProgramRunner<?> runner) throws ExecutionException {
        return null;
    }
}
