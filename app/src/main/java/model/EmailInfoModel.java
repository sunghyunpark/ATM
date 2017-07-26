package model;

import java.io.File;

/**
 * Created by Sunghyun on 2017. 7. 12..
 * Email 정보
 */

public class EmailInfoModel {
    private String mailType;
    private String FilePath;
    private String userEmail;
    private File file;

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
