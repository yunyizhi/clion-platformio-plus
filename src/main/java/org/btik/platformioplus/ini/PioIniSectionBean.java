package org.btik.platformioplus.ini;

import java.util.HashMap;
import java.util.Objects;

import static org.btik.platformioplus.ini.PioIniMetaConst.PLATFORM;

/**
 * @author lustre
 * @since 2025/5/3 18:02
 */
public class PioIniSectionBean {
    private String section;
    private String envName;
    private String platform;
    private String board;

    private HashMap<String, String> properties;


    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public void put(String key, String value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
        if (Objects.equals(key, PLATFORM)) {
            setPlatform(value);
        } else if (Objects.equals(key, board)) {
            setBoard(value);
        }
    }
}
