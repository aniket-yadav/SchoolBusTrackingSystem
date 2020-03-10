package sbts.dmw.com.sbtrackingsystem.model;

public class Student {

    private String name;
    private String roll_no;
    private String division;
    private String s_class;
    private String photo;

    public Student() {
    }

    public Student(String name, String roll_no, String division, String s_class, String photo) {

        this.name = name;
        this.roll_no = roll_no;
        this.division = division;
        this.s_class = s_class;
        this.photo = photo;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getS_class() {
        return s_class;
    }

    public void setS_class(String s_class) {
        this.s_class = s_class;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
