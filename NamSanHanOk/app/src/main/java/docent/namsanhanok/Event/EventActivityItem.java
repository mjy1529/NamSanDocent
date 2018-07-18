package docent.namsanhanok.Event;

public class EventActivityItem {

    private String title;
    private String start_date;
    private String end_date;
    private String place;
    private String complete;
    private int image;

        //ArrayList add할 때
        public EventActivityItem(String title, int image, String start_date, String end_date, String place, String complete) {
            this.title = title;
            this.image = image;
            this.start_date = start_date;
            this.end_date = end_date;
            this.place = place;
            this.complete = complete;
        }



//    public void setTitle(String title) {
//        this.title = title;
//    }
//    public void setImage(int image){
//        this.image = image;
//    }
//    public void setStartDate(String start_date){
//            this.start_date = start_date;
//    }
//    public void setEndDate(String end_date){
//            this.end_date = end_date;
//    }
//    public void setPlace(String place){
//            this.place = place;
//    }
//    public void setComplete(String complete){
//            this.complete = complete;
//    }

    public String getTitle() { return title;  }
    public int getImage(){
        return image;
}
    public String getStartDate() {
        return start_date;
    }
    public String getEndDate(){ return  end_date; }
    public String getPlace(){ return  place; }
    public String getComplete(){ return complete; }



}
