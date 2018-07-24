package docent.namsanhanok.Notice;

import java.io.Serializable;

public class NoticeRecyclerItem implements Serializable {
    private String title;
    private String date;
    private String content;

    public NoticeRecyclerItem() {

    }

    public NoticeRecyclerItem(String title, String date, int read_cnt) {
        this.title = title;
        this.date = date;
    }

    public NoticeRecyclerItem(String title, String date, int read_cnt, String content) {
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
