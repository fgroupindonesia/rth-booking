package object;

public class Ruqyah {

    private String date_chosen;
    private int status;
    private int gender_therapist;
    private int id;

    public String getDate_chosen() {
        return date_chosen;
    }

    public void setDate_chosen(String date_chosen) {
        this.date_chosen = date_chosen;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGender_therapist() {
        return gender_therapist;
    }

    public void setGender_therapist(int gender_therapist) {
        this.gender_therapist = gender_therapist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
