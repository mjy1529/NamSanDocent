package docent.namsanhanok.Category;

import java.io.Serializable;
import java.util.HashMap;

import docent.namsanhanok.Docent.DocentData;

public class CategoryData implements Serializable{
    public String category_id;
    public String category_title;
    public String category_image_url;
    public String category_detail_info;
    public String beacon_number;
    public String soundqr_number;

    public HashMap<String, DocentData> docentlist; // category id, docent data

    CategoryData() {
        category_id = "";
        category_title = "";
        category_image_url = "";
        category_detail_info = "";
        beacon_number = "";
        soundqr_number ="";
        docentlist = null;
    }
}
