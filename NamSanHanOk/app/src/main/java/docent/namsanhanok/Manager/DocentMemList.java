package docent.namsanhanok.Manager;

import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import docent.namsanhanok.Category.CategoryData;
import docent.namsanhanok.Docent.DocentData;

public class DocentMemList {

    private HashMap<String, CategoryData> categorylist_;
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

    public HashMap<String, CategoryData> getCategorylist() {
        return categorylist_;
    }

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

//    public boolean get_docent_info(String bc_num, CategoryData categoryData, DocentData docentData){
//        IDInfoData idInfoData = new IDInfoData();
//        if (check_beacon_number(bc_num, idInfoData) == false) {
//            return false;
//        }
//
//        categoryData = (CategoryData)deepClone(categorylist_.get(idInfoData.category_id));
//        //categoryData = SerializationUtils.clone(categorylist_.get(idInfoData.category_id));
//        //categoryData = categorylist_.get(idInfoData.category_id);
//        if (idInfoData.docent_id.length() > 0) {
//            //docentData = categorylist_.get(idInfoData.category_id).docentlist.get(idInfoData.docent_id);
//            //docentData = SerializationUtils.clone(categorylist_.get(idInfoData.category_id).docentlist.get(idInfoData.docent_id));
//            docentData = (DocentData)deepClone(categorylist_.get(idInfoData.category_id).docentlist.get(idInfoData.docent_id));
//        }
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

    public boolean check_beacon_number(String bc_num, IDInfoData idInfoData)
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
                    idInfoData.category_id = key;
                    break;
                }

                if (categorylist_.get(key).docentlist == null) {
                    continue;
                }

                Iterator<String> docent_keys = categorylist_.get(key).docentlist.keySet().iterator();
                while( docent_keys.hasNext() ) {
                    String docent_key = docent_keys.next();
                    String becon_number = categorylist_.get(key).docentlist.get(docent_key).beacon_number;
                    if (bc_num.compareTo(becon_number) == 0) {
                        docent_found = true;
                        idInfoData.category_id = key;
                        idInfoData.docent_id = docent_key;
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

    public boolean check_soundqr_number(String sq_num, IDInfoData idInfoData)
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
                    idInfoData.category_id = key;
                    break;
                }

                if (categorylist_.get(key).docentlist == null) {
                    continue;
                }

                Iterator<String> docent_keys = categorylist_.get(key).docentlist.keySet().iterator();
                while( docent_keys.hasNext() ) {
                    String docent_key = docent_keys.next();
                    String soundqr_number = categorylist_.get(key).docentlist.get(docent_key).soundqr_number;
                    if (sq_num.compareTo(soundqr_number) == 0) {
                        docent_found = true;
                        idInfoData.category_id = key;
                        idInfoData.docent_id = docent_key;
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
