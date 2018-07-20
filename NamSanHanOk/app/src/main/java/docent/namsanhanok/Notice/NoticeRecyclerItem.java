package docent.namsanhanok.Notice;

import java.io.Serializable;

public class NoticeRecyclerItem implements Serializable {
    private String title;
    private String date;
    private int read_cnt;
    private String content;

    public NoticeRecyclerItem() {

    }

    public NoticeRecyclerItem(String title, String date, int read_cnt) {
        this.title = title;
        this.date = date;
        this.read_cnt = read_cnt;
    }

    public NoticeRecyclerItem(String title, String date, int read_cnt, String content) {
        this.title = title;
        this.date = date;
        this.read_cnt = read_cnt;
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

    public int getRead_cnt() {
        return read_cnt;
    }

    public void setRead_cnt(int read_cnt) {
        this.read_cnt = read_cnt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
