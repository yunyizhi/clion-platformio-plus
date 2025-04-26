package org.btik.platformioplus.run.config.esp32.debug.openocd;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.PtyCommandLine;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.BaseProcessHandler;
import com.intellij.execution.process.KillableProcessHandler;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.jetbrains.cidr.ArchitectureType;
import com.jetbrains.cidr.cpp.execution.debugger.backend.CLionGDBDriverConfiguration;
import com.jetbrains.cidr.cpp.toolchains.CPPToolchains;
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver;

import org.btik.platformioplus.icon.PlatformIoPlusIcon;
import org.btik.platformioplus.run.config.esp32.debug.Esp32ConsoleRunProfile;
import org.btik.platformioplus.run.config.esp32.debug.Esp32RunConfig;
import org.btik.platformioplus.util.SysConf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.btik.platformioplus.util.Note.$i18n;

public class Esp32OpenOcdGDBDriverConfig extends CLionGDBDriverConfiguration {
    private final Esp32RunConfig esp32RunConfig;

    private final Project project;

    private final PtyCommandLine openOcdCli = new PtyCommandLine();

    private final Esp32OpenOcdProcessListener openOcdProcessListener = new Esp32OpenOcdProcessListener();

    public Esp32OpenOcdGDBDriverConfig(@NotNull Project project, @Nullable CPPToolchains.Toolchain toolchain, Esp32RunConfig esp32RunConfig) {
        super(project, toolchain);
        this.project = project;
        this.esp32RunConfig = esp32RunConfig;
    }

    @NotNull
    @Override
    public BaseProcessHandler<?> createDebugProcessHandler(@NotNull GeneralCommandLine commandLine) throws ExecutionException {
        var idfOpenOcd = new Esp32ConsoleRunProfile($i18n("esp.idf.debug.openocd.run.title"), PlatformIoPlusIcon.PIOPLUS_13, openOcdCli);
        idfOpenOcd.addProcessListener(openOcdProcessListener);
        var environment = ExecutionEnvironmentBuilder.create(project, DefaultRunExecutor.getRunExecutorInstance(), idfOpenOcd).build();
        environment.setExecutionId(ExecutionEnvironment.getNextUnusedExecutionId());
        ApplicationManager.getApplication().invokeLater(() -> {
            try {
                environment.getRunner().execute(environment);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        KillableProcessHandler processHandler = new KillableProcessHandler(commandLine);
        processHandler.addProcessListener(new ProcessListener() {
            @Override
            public void processTerminated(@NotNull ProcessEvent event) {
                ProcessListener.super.processTerminated(event);
                openOcdProcessListener.destroy();
            }
        });

        return processHandler;
    }

    @Override
    public @NotNull GeneralCommandLine createDriverCommandLine(@NotNull DebuggerDriver driver, @NotNull ArchitectureType architectureType) {
        var configDataModel = esp32RunConfig.getConfigDataModel();
        Map<String, String> envs = new HashMap<>(configDataModel.getEnvData().getEnvs());

        openOcdCli.withInitialColumns(SysConf.getInt("esp.idf.pyt.cmd.cols", 120));
        openOcdCli.setExePath(configDataModel.getOpenOcdPath());
        openOcdCli.withConsoleMode(true);
        openOcdCli.setWorkDirectory(project.getBasePath());
        openOcdCli.setCharset(Charset.forName(System.getProperty("sun.jnu.encoding", "UTF-8")));
        String openOcdArguments = configDataModel.getOpenOcdArguments();
        if (StringUtil.isNotEmpty(openOcdArguments)) {
            openOcdCli.addParameters(openOcdArguments);
        }
        openOcdCli.withEnvironment(envs);
        GeneralCommandLine commandLine = new GeneralCommandLine()
                .withExePath(configDataModel.getGdbExe())
                .withWorkDirectory(project.getBasePath())
                .withCharset(Charset.forName(System.getProperty("sun.jnu.encoding", "UTF-8")))
                .withEnvironment(envs)
                .withRedirectErrorStream(true)
                .withParameters("--interpreter=mi2",
                        "-iex", "set mi-async",
                        "-iex", "set confirm off");

        String bootloaderElf = configDataModel.getBootloaderElf();
        if (checkElf(bootloaderElf)) {
            commandLine.addParameters("-iex", "add-symbol-file " + gdbConsolePath(bootloaderElf));
        }

        String appElf = configDataModel.getAppElf();
        if (checkElf(appElf)) {
            commandLine.addParameters("-iex", "file " + gdbConsolePath(appElf));
        }
        String[] connect = {
                "set confirm on",
                "set remotetimeout 10",
                "target remote :3333",
                "monitor reset halt",
                "maintenance flush register-cache",
        };
        for (String gdbCmd : connect) {
            commandLine.addParameters("-ex", gdbCmd);
        }
        return commandLine;
    }

    private boolean checkElf(String elfPath) {
        return elfPath != null && Files.exists(Path.of(elfPath));
    }

    private String gdbConsolePath(String path) {
        return path.replace('\\', '/');
    }
}
