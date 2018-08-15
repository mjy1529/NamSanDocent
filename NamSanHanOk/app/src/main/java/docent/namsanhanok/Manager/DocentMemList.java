package docent.namsanhanok.Manager;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

import docent.namsanhanok.Category.CategoryData;
import docent.namsanhanok.Docent.DocentData;
import uk.co.senab.photoview.log.LoggerDefault;

public class DocentMemList {

    public HashMap<String, CategoryData> categorylist_;
    //public HashMap<String, DocentData> docentlist_;

    public static DocentMemList instance = new DocentMemList();

    public static DocentMemList getInstance() {
//        instance = new DocentMemList();
        return instance;
    }

    public void initialize(){
        categorylist_ = new HashMap<>();
        //docentlist_ = new HashMap<>();
    }

    //왜 하나하나 지웠을까? 한번에 안지우고
    public void clear_all(){
        try {
            Iterator<String> keys = categorylist_.keySet().iterator();
            while( keys.hasNext() ){
                String key = keys.next();
                categorylist_.get(key).docentlist.clear();
            }
            categorylist_.clear();
            //docentlist_.clear();
        }catch (Exception e)
        {
            e.printStackTrace();
            return ;
        }
    }

    public boolean put_category_info(CategoryData data) {
        try {
            Iterator<String> keys = categorylist_.keySet().iterator();
            boolean found = false;
            while( keys.hasNext() ){
                String key = keys.next();
                if (data.category_id.compareTo(categorylist_.get(key).category_id) == 0) {
                    found = true;
                    break;
                }
            }

            if (found == false) {
                categorylist_.put(data.category_id, data);
                return true;
            }
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean put_docent_info(DocentData data) {

        try {
//        if (docentlist_.size() == 0)
//            return false;

            boolean found = false;
            if (categorylist_.get(data.category_id).docentlist !=null) {
                Iterator<String> keys = categorylist_.get(data.category_id).docentlist.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    //비교했을시 0이라 함은..?
                    if (data.docent_id.compareTo(categorylist_.get(data.category_id).docentlist.get(key).docent_id) == 0) {
                        found = true;
                        break;
                    }
                }
            }
            //        if (found == false)
            //            return false;
            //
            //        docentlist_.put(data.docent_id, data);
            //        return true;
            if (found == false) {
                //docentlist_.put(data.docent_id, data);

                if (categorylist_.get(data.category_id).docentlist == null) {
                    categorylist_.get(data.category_id).docentlist = new HashMap<>();
                }
                categorylist_.get(data.category_id).docentlist.put(data.docent_id, data);
                return true;
            }
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean get_category_info(String id, CategoryData data){
        try {
            if (categorylist_.size() == 0)
                return false;

            //data = categorylist_.get(id);
            data.category_id = categorylist_.get(id).category_id;
            data.category_title = categorylist_.get(id).category_title;
            data.category_image_url = categorylist_.get(id).category_image_url;
            data.category_detail_info = categorylist_.get(id).category_detail_info;
            data.beacon_number = categorylist_.get(id).beacon_number;
            data.soundqr_number = categorylist_.get(id).soundqr_number;
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean get_docent_info(String id, DocentData data){
//        if (docentlist_.size() == 0)
//            return false;
//
//        data = docentlist_.get(id);
//        return true;
//    }

    public boolean get_docent_info(String category_id, HashMap<String, DocentData> map) {
        try {
            if (categorylist_.get(category_id).docentlist.size() == 0)
                return false;

            map.putAll(categorylist_.get(category_id).docentlist);
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean check_beacon_number(String bc_num)
    {
        try {
            if (categorylist_.size() == 0)
                return false;

            Iterator<String> keys = categorylist_.keySet().iterator();
            boolean category_found = false;
            boolean docent_found = false;
            while( keys.hasNext() ){
                String key = keys.next();
                if (bc_num.compareTo(categorylist_.get(key).beacon_number) == 0) {
                    category_found = true;
                    break;
                }

                if (categorylist_.get(key).docentlist == null) {
                    continue;
                }

                Iterator<String> docent_keys = categorylist_.get(key).docentlist.keySet().iterator();
                while( docent_keys.hasNext() ) {
                    String docent_key = keys.next();
                    Log.d("key", key);
                    Log.d("manager1", categorylist_.get(key).toString());

                    Log.d("manager2", categorylist_.get(key).docentlist.get(docent_key).toString());
                    Log.d("manager3", bc_num);
                    Log.d("manager4", docent_key);

                    Log.d("result5", categorylist_.get("1").docentlist.get("2").beacon_number);

                    if (bc_num.compareTo(categorylist_.get(key).docentlist.get(docent_key).beacon_number) == 0) {
                        docent_found = true;
                        Log.d("docent_found", docent_found+"");
                        break;
                    }
                }

                if (docent_found == true)
                    break;
            }

            return category_found || docent_found;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean check_soundqr_number(String sq_num)
    {
        try {
            if (categorylist_.size() == 0)
                return false;

            Iterator<String> keys = categorylist_.keySet().iterator();
            boolean category_found = false;
            boolean docent_found = false;
            while( keys.hasNext() ){
                String key = keys.next();
                if (sq_num.compareTo(categorylist_.get(key).soundqr_number) == 0) {
                    category_found = true;
                    break;
                }

                if (categorylist_.get(key).docentlist == null) {
                    continue;
                }

                Iterator<String> docent_keys = categorylist_.get(key).docentlist.keySet().iterator();
                while( docent_keys.hasNext() ) {
                    String docent_key = keys.next();
                    if (sq_num.compareTo(categorylist_.get(key).docentlist.get(docent_key).soundqr_number) == 0) {
                        docent_found = true;
                        break;
                    }
                }

                if (docent_found == true)
                    break;

            }
            return category_found || docent_found;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
