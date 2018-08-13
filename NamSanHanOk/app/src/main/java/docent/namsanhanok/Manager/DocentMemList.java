package docent.namsanhanok.Manager;

import java.util.HashMap;
import java.util.Iterator;

import docent.namsanhanok.Category.CategoryData;
import docent.namsanhanok.Docent.DocentData;

public class DocentMemList {

    public HashMap<String, CategoryData> categorylist_;
    public HashMap<String, DocentData> docentlist_;

    public static DocentMemList instance = new DocentMemList();

    public static DocentMemList getInstance() {
        return instance;
    }

    public void initialize(){
        categorylist_ = new HashMap<>();
        docentlist_ = new HashMap<>();
    }

    public void clear_all(){
        categorylist_.clear();
        docentlist_.clear();
    }

    public boolean put_category_info(CategoryData data) {
        Iterator<String> keys = categorylist_.keySet().iterator();
        boolean found = false;
        while( keys.hasNext() ){
            String key = keys.next();
            if (data.category_id.compareTo(categorylist_.get(key).category_id) == 0) {
                found = true;
            }
        }

        if (found == false) {
            categorylist_.put(data.category_id, data);
            return true;
        }
        return false;
    }

    public boolean put_docent_info(DocentData data) {
//        if (docentlist_.size() == 0)
//            return false;
        Iterator<String> keys = docentlist_.keySet().iterator();
        boolean found = false;
        while( keys.hasNext() ){
            String key = keys.next();
            if (data.docent_id.compareTo(docentlist_.get(key).docent_id) == 0) {
                found = true;
            }
        }
//        if (found == false)
//            return false;
//
//        docentlist_.put(data.docent_id, data);
//        return true;
        if (found == false) {
            docentlist_.put(data.docent_id, data);
            return true;
        }
        return false;
    }

    public boolean get_category_info(String id, CategoryData data){
        if (categorylist_.size() == 0)
            return false;

        data.category_id = categorylist_.get(id).category_id;
        data.category_title = categorylist_.get(id).category_title;
        data.category_image_url = categorylist_.get(id).category_image_url;
        data.category_detail_info = categorylist_.get(id).category_detail_info;
        data.beacon_number = categorylist_.get(id).beacon_number;
        data.soundqr_number = categorylist_.get(id).soundqr_number;

//        data = categorylist_.get(id);
        return true;
    }

    public boolean get_docent_info(String id, DocentData data){
        if (docentlist_.size() == 0)
            return false;

        data.docent_id = docentlist_.get(id).docent_id;
        data.category_id = docentlist_.get(id).category_id;
        data.docent_title = docentlist_.get(id).docent_title;
        data.docent_content_info = docentlist_.get(id).docent_content_info;
        data.docent_vod_url = docentlist_.get(id).docent_vod_url;
        data.docent_audio_url = docentlist_.get(id).docent_audio_url;
        data.docent_image_url = docentlist_.get(id).docent_image_url;
        data.docent_location = docentlist_.get(id).docent_location;
        data.beacon_number = docentlist_.get(id).beacon_number;
        data.soundqr_number = docentlist_.get(id).soundqr_number;

//        data = docentlist_.get(id);
        return true;
    }

    private boolean check_beacon_number_in_category(String bc_num) {
        if (categorylist_.size() == 0)
            return false;

        Iterator<String> keys = categorylist_.keySet().iterator();
        boolean found = false;
        while( keys.hasNext() ){
            String key = keys.next();
            if (bc_num.compareTo(categorylist_.get(key).beacon_number) == 0) {
                found = true;
            }
        }

        return found;
    }

    private boolean check_soundqr_number_in_category(String sq_num) {
        if (categorylist_.size() == 0)
            return false;

        Iterator<String> keys = categorylist_.keySet().iterator();
        boolean found = false;
        while( keys.hasNext() ){
            String key = keys.next();
            if (sq_num.compareTo(categorylist_.get(key).soundqr_number) == 0) {
                found = true;
            }
        }

        return found;
    }

    private boolean check_beacon_number_in_docent(String bc_num) {
        if (docentlist_.size() == 0)
            return false;

        Iterator<String> keys = docentlist_.keySet().iterator();
        boolean found = false;
        while( keys.hasNext() ){
            String key = keys.next();
            if (bc_num.compareTo(docentlist_.get(key).beacon_number) == 0) {
                found = true;
            }
        }

        return found;
    }

    private boolean check_soundqr_number_in_docent(String sq_num) {
        if (docentlist_.size() == 0)
            return false;

        Iterator<String> keys = docentlist_.keySet().iterator();
        boolean found = false;
        while( keys.hasNext() ){
            String key = keys.next();
            if (sq_num.compareTo(docentlist_.get(key).soundqr_number) == 0) {
                found = true;
            }
        }

        return found;
    }

    public boolean check_beacon_number(String bc_num)
    {
        if (check_beacon_number_in_category(bc_num) == true || check_beacon_number_in_docent(bc_num) == true) {
            return true;
        }
        return false;
    }

    public boolean check_soundqr_number(String sq_num)
    {
        if (check_soundqr_number_in_category(sq_num) == true || check_soundqr_number_in_docent(sq_num) == true) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "DocentMemList{" +
                "categorylist_=" + categorylist_ +
                '}';
    }
}
