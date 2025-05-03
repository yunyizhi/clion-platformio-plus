package org.btik.platformioplus.ini;

/**
 * @author lustre
 * @since 2025/5/3 23:00
 */
public class BoardConf {
    private String boardId;
    private String boardName;
    private String mcu;

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getMcu() {
        return mcu;
    }

    public void setMcu(String mcu) {
        this.mcu = mcu;
    }
}
