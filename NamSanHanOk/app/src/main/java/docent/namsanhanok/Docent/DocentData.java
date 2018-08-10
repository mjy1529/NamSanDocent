package docent.namsanhanok.Docent;

public class DocentData {
    public int docent_id;
    public int category_id;
    public String docent_title;
    public String docent_content_info;
    public String docent_vod_url;
    public String docent_audio_url;
    public String docent_image_url;
    public String docent_location;
    public String beacon_number;
    public String soundqr_number;

    @Override
    public String toString() {
        return "DocentData{" +
                "docent_id=" + docent_id +
                ", category_id=" + category_id +
                ", docent_title='" + docent_title + '\'' +
                ", docent_content_info='" + docent_content_info + '\'' +
                ", docent_vod_url='" + docent_vod_url + '\'' +
                ", docent_audio_url='" + docent_audio_url + '\'' +
                ", docent_image_url='" + docent_image_url + '\'' +
                ", docent_location='" + docent_location + '\'' +
                ", beacon_number='" + beacon_number + '\'' +
                ", soundqr_number='" + soundqr_number + '\'' +
                '}';
    }
}
