package model;

/**
 * Created by Sunghyun on 2017. 7. 12..
 * Email 정보
 */

public class EmailInfo {
    private String mailType;
    private String FilePath;
    private String userEmail;

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

}
