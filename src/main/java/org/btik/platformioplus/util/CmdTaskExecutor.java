package org.btik.platformioplus.util;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.openapi.project.Project;
import org.btik.platformioplus.run.config.PioConsoleRunProfile;
import org.jetbrains.annotations.NotNull;

/**
 * @author lustre
 * @since 2024/2/18 9:16
 */
public class CmdTaskExecutor {
    public static void execute(@NotNull Project project, @NotNull PioConsoleRunProfile runProfile) throws ExecutionException {
        ExecutionEnvironment environment = ExecutionEnvironmentBuilder.create(
                project, DefaultRunExecutor.getRunExecutorInstance(), runProfile).build();
        environment.setExecutionId(ExecutionEnvironment.getNextUnusedExecutionId());
        environment.getRunner().execute(environment);
    }
}
