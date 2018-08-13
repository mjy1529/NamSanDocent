package docent.namsanhanok.Manager;

import java.util.HashMap;
import java.util.Iterator;

public class DocentMemList {

    public HashMap<String, CategoryItem> categorylist_;
    public HashMap<String, DocentItem> docentlist_;

    public void initialize(){
        categorylist_ = new HashMap<String, CategoryItem>();
        docentlist_ = new HashMap<String, DocentItem>();
    }

    public void clear_all(){
        categorylist_.clear();
        docentlist_.clear();
    }

    public boolean put_category_info(CategoryItem item) {

        Iterator<String> keys = categorylist_.keySet().iterator();
        boolean found = false;
        while( keys.hasNext() ){
            String key = keys.next();
            if (item.category_id.compareTo(categorylist_.get(key).category_id) == 0) {
                found = true;
            }
        }

        if (found == false) {
            categorylist_.put(item.category_id, item);
        }

        return found;
    }

    public boolean put_docent_info(DocentItem item) {
        if (categorylist_.size() == 0)
            return false;

        Iterator<String> keys = categorylist_.keySet().iterator();
        boolean found = false;
        while( keys.hasNext() ){
            String key = keys.next();
            if (item.category_id.compareTo(categorylist_.get(key).category_id) == 0) {
                found = true;
            }
        }

        if (found == false)
            return false;

        docentlist_.put(item.docent_id, item);
        return true;
    }

    public boolean get_category_info(String id, CategoryItem item){
        if (categorylist_.size() == 0)
            return false;

        item = categorylist_.get(id);
        return true;
    }

    public boolean get_docent_info(String id, DocentItem item){
        if (docentlist_.size() == 0)
            return false;

        item = docentlist_.get(id);
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
}
