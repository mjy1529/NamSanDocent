package docent.namsanhanok.Question;

public class QuestionData {
    private String question_title;
    private String question_content;
    private String question_email;
    private String question_phone;
    private String question_username;
    private String question_time;

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public void setQuestion_email(String question_email) {
        this.question_email = question_email;
    }

    public void setQuestion_phone(String question_phone) {
        this.question_phone = question_phone;
    }

    public void setQuestion_username(String question_username) {
        this.question_username = question_username;
    }

    public void setQuestion_time(String question_time){
        this.question_time = question_time;
    }

    public String getQuestion_title() {
        return question_title;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public String getQuestion_email() {
        return question_email;
    }

    public String getQuestion_phone() {
        return question_phone;
    }

    public String getQuestion_username() {
        return question_username;
    }

    public String getQuestion_time(){ return question_time; }
}
