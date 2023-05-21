package org.btik.platformioplus.run.config;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableProcessHandler;
import com.intellij.execution.process.NopProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import org.btik.platformioplus.setting.PioConf;
import org.jetbrains.annotations.NotNull;

/**
 * @author lustre
 * @since 2023/5/18 11:25
 */
public class PioPlusRunTaskProfileState extends CommandLineState {

    private final PioPlusRunConfig pioPlusRunConfig;

    public PioPlusRunTaskProfileState(PioPlusRunConfig pioPlusRunConfig, @NotNull ExecutionEnvironment environment) {
        super(environment);
        this.pioPlusRunConfig = pioPlusRunConfig;
    }

    @Override
    protected @NotNull ProcessHandler startProcess() throws ExecutionException {
        String platformioLocation = PioConf.findPlatformio();
        if (platformioLocation == null) {
            PioConf.notifyPlatformioNotFound();
            return new NopProcessHandler();
        }
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.withEnvironment(pioPlusRunConfig.getEnvData().getEnvs());
        commandLine.setExePath(platformioLocation);
        commandLine.setWorkDirectory(getEnvironment().getProject().getBasePath());
        commandLine.addParameters(pioPlusRunConfig.getArguments().split("\\s"));
        return new KillableProcessHandler(commandLine);
    }
}
