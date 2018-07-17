package docent.namsanhanok.Category;

public class CategoryListActivityItem {

     private String title;
        private int image;

        //ArrayList add할 때
        public CategoryListActivityItem(String title, int image) {
            this.title = title;
            this.image = image;
        }



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
        public int getImage(){
            return image;
        }

}
