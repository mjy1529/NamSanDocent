package docent.namsanhanok.Notice;

import java.io.Serializable;

public class NoticeData implements Serializable {
    private int notice_id;
    private String notice_title;
    private String notice_content;
    private String notice_time;

    public int getNotice_id() {
        return notice_id;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public String getNotice_content() {
        return notice_content;
    }

    public String getNotice_time() {
        return notice_time;
    }

    public void setNotice_id(int notice_id) {
        this.notice_id = notice_id;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public void setNotice_content(String notice_content) {
        this.notice_content = notice_content;
    }

    public void setNotice_time(String notice_time) {
        this.notice_time = notice_time;
    }
}
