package org.btik.platformioplus.ini;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import ini4idea.lang.psi.IniKey;
import ini4idea.lang.psi.IniProperty;
import ini4idea.lang.psi.IniSection;
import ini4idea.lang.psi.IniValue;
import org.btik.platformioplus.ini.reload.PioIniChangeHandler;
import org.btik.platformioplus.service.PlatformIoIniStore;
import org.btik.platformioplus.setting.PioConf;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.btik.platformioplus.ini.PioIniBoradMetaConst.*;
import static org.btik.platformioplus.util.IniUtils.getEnvName;
import static org.btik.platformioplus.util.IniUtils.getSectionName;
import static org.btik.platformioplus.util.PathUtils.getPioSubPath;

/**
 * @author lustre
 * @since 2025/5/3 17:31
 */
public class PlatformIoIniStoreImpl implements PlatformIoIniStore {
    private final static Logger log = Logger.getInstance(PlatformIoIniStoreImpl.class);
    private final Project project;
    private PsiFile pioIniPsiFile;

    private final HashMap<String, PioIniSectionBean> sectionMap = new HashMap<>();

    private final HashMap<String, BoardConf> boardConfMap = new HashMap<>();

    private PioIniSectionBean defaultEnv = null;

    private Path platformsPath = null;

    private File platformsDir = null;

    private int iniVersion = 0;
    private final List<PioIniSectionBean> sections = new ArrayList<>();

    private final Gson gson = new Gson();

    public PlatformIoIniStoreImpl(Project project) {
        this.project = project;
        VirtualFile[] contentRoots = ProjectRootManager.getInstance(project).getContentRoots();
        for (VirtualFile contentRoot : contentRoots) {
            VirtualFile pioIni = contentRoot.findChild(PioConf.FILE_NAME);
            if (pioIni != null) {
                pioIniPsiFile = PsiUtilCore.getPsiFile(project, pioIni);
                break;
            }
        }
    }

    private void parsePioIni() {
        PioIniChangeHandler iniChangeHandler = project.getService(PioIniChangeHandler.class);
        int currentIniVersion = iniChangeHandler.iniVersion();
        if (currentIniVersion == iniVersion) {
            return;
        }
        platformsPath = getPioSubPath(PLATFORMS_PATH);
        if (platformsPath == null) {
            return;
        }
        platformsDir = platformsPath.toFile();
        iniVersion = currentIniVersion;

        @NotNull PsiElement[] children = pioIniPsiFile.getChildren();
        for (PsiElement child : children) {
            if (child instanceof IniSection section) {
                PioIniSectionBean bean = new PioIniSectionBean();
                sections.add(bean);
                String nameText = section.getNameText();
                String sectionName = getSectionName(nameText);
                if (Objects.equals(ENV, sectionName)) {
                    defaultEnv = bean;
                }
                if (nameText != null && nameText.startsWith(ENV_SECTION_PREFIX)) {
                    bean.setEnvName(getEnvName(nameText));
                }
                bean.setSection(sectionName);
                HashMap<String, String> propResult = parseProp(section);
                bean.setProperties(propResult);
                bean.setPlatform(propResult.get(PLATFORM));
                bean.setBoard(propResult.get(BOARD));
                sectionMap.put(sectionName, bean);
            }
        }
        parseSections(sections);
    }

    private void parseSections(List<PioIniSectionBean> sections) {

        for (PioIniSectionBean section : sections) {
            String platform = section.getPlatform();
            String board = section.getBoard();
            if (StringUtil.isEmpty(platform)) {
                platform = getValueFormExtendOrCommon(PLATFORM, section);
            }
            if (StringUtil.isEmpty(board)) {
                board = getValueFormExtendOrCommon(BOARD, section);
            }
            if (StringUtil.isEmpty(platform) || StringUtil.isEmpty(board)) {
                continue;
            }
            BoardConf boardConf = boardConfMap.get(platform + '/' + board);
            if (boardConf != null) {
                section.put(MCU, boardConf.getMcu());
                continue;
            }
            if (platform.startsWith("http")) {
                doWithFindBoard(platform, board, section);
                continue;
            }
            String platformPath = platform;
            if (platform.contains("@")) {
                platformPath = platform.replaceAll("\\s", "");
            }
            Path boardPath = platformsPath.resolve(platformPath).resolve(BOARD_DIR).resolve(board + BOARD_SUFFIX);
            if (!Files.exists(boardPath)) {
                doWithFindBoard(platform, board, section);
                continue;
            }
            boardConf = parseBoard(boardPath, platform, board);
            if (boardConf != null) {
                section.put(MCU, boardConf.getMcu());
            }
        }

    }

    private BoardConf parseBoard(Path boardPath, String platform, String board) {
        if (boardPath == null) {
            return null;
        }
        BoardConf boardConf = new BoardConf();
        boardConf.setBoardId(board);
        try (JsonReader reader = new JsonReader(new FileReader(boardPath.toFile()))) {
            JsonElement jsonElement = gson.fromJson(reader, JsonElement.class);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement boardName = jsonObject.get(NAME);
            if (boardName != null) {
                boardConf.setBoardName(boardName.getAsString());
            }
            JsonElement boardBuild = jsonObject.get(BOARD_BUILD);
            if (boardBuild != null) {
                JsonObject boardBuildObj = boardBuild.getAsJsonObject();
                JsonElement boardBuildMcu = boardBuildObj.get(MCU);
                if (boardBuildMcu != null) {
                    boardConf.setMcu(boardBuildMcu.getAsString());
                }
            }
            boardConfMap.put(platform + '/' + board, boardConf);
            boardConfMap.put(board, boardConf);
        } catch (Exception e) {
            log.error("Error reading board configuration", e);
        }

        return boardConf;
    }

    private void doWithFindBoard(String platform, String board, PioIniSectionBean section) {
        // 先尝试通过boardId获取
        BoardConf boardConf = boardConfMap.get(board);
        if (boardConf != null) {
            section.put(MCU, boardConf.getMcu());
            return;
        }
        // 再尝试查找
        String[] platforms = platformsDir.list();
        if (platforms == null) {
            return;
        }
        for (String aPlatform : platforms) {
            Path boardPath = platformsPath.resolve(aPlatform).resolve(BOARD_DIR).resolve(board + BOARD_SUFFIX);
            if (!Files.exists(boardPath)) {
                continue;
            }
            boardConf = parseBoard(boardPath, platform, board);
            if (boardConf != null) {
                section.put(MCU, boardConf.getMcu());
                break;
            }
        }
    }

    private String getValueFormExtendOrCommon(String key, PioIniSectionBean section) {
        String extends_ = section.getProperties().get(EXTENDS);
        String value = getParentValue(key, extends_);
        if (!StringUtil.isEmpty(value)) {
            section.put(key, value);
            return value;
        }
        if (defaultEnv != null) {
            value = defaultEnv.getProperties().get(key);
            if (StringUtil.isEmpty(value)) {
                return "";
            }
            section.put(key, value);
            return value;
        }
        return "";
    }

    private String getParentValue(String key, String parent) {
        PioIniSectionBean pioIniSectionBean = sectionMap.get(parent);
        if (pioIniSectionBean == null) {
            return "";
        }
        String value = pioIniSectionBean.getProperties().get(key);
        if (StringUtil.isEmpty(value)) {
            return getParentValue(key, parent);
        }
        return value;
    }

    private static @NotNull HashMap<String, String> parseProp(IniSection section) {
        HashMap<String, String> propResult = new HashMap<>();
        @NotNull PsiElement[] properties = section.getChildren();
        for (PsiElement property : properties) {
            if (property instanceof IniProperty prop) {
                IniKey iniKey = prop.getIniKey();
                IniValue iniValue = prop.getIniValue();
                if (iniValue != null) {
                    propResult.put(iniKey.getText(), iniValue.getText());
                }
            }
        }
        return propResult;
    }

    @Override
    public PsiFile getPlatformIoIni() {
        return pioIniPsiFile;
    }

    @Override
    public PioIniSectionBean getCurrentSection() {
        parsePioIni();
        return sections.isEmpty() ? null : sections.get(0);
    }

    @Override
    public List<PioIniSectionBean> getSections() {
        parsePioIni();
        return sections;
    }
}
