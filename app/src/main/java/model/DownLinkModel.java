package model;

/**
 * Created by NAVER on 2017-07-21.
 * 앱관리 -> 다운로드 링크 모델
 */

public class DownLinkModel {
    private int no;
    private String linkUrl;
    private String linkTitle;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }
}
