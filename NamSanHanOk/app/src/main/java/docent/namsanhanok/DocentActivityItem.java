package docent.namsanhanok;

import android.widget.ImageView;

public class DocentActivityItem {

    private String title;
    private String info;
    private int image;

    //ArrayList add할 때
    public DocentActivityItem(String title, int image) {
        this.title = title;
//      this.info = info;
        this.image = image;
    }


//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//    public void setInfo(String info){
//        this.info = info;
//    }
//    public void setImage(int image){
//        this.image = image;
//    }

    public String getTitle() {
        return title;
    }
    public String getInfo() {
        return info;
    }
    public int getImage(){
        return image;
    }


}
