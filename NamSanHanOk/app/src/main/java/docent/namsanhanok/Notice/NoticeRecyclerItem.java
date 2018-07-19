package docent.namsanhanok.Notice;

public class NoticeRecyclerItem {
    private String title;
    private String date;
    private int read_cnt;

    public NoticeRecyclerItem(String title, String date, int read_cnt) {
        this.title = title;
        this.date = date;
        this.read_cnt = read_cnt;
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
}
