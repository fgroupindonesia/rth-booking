package object;

public class Schedule {

    String specific_hour;
    // status 1 : tersedia
    // status 0 : tidak tersedia
    int status;
    private int id;
    private String date_chosen;
    private int gender_therapist;
    private String date_created;
    private String description;

    @Override
    public String toString(){
        return this.getDate_chosen() + " " + this.getSpecific_hour();
    }

    public String getSpecific_hour() {
        return specific_hour;
    }

    public void setSpecific_hour(String specific_hour) {
        this.specific_hour = specific_hour;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_chosen() {
        return date_chosen;
    }

    public void setDate_chosen(String date_chosen) {
        this.date_chosen = date_chosen;
    }

    public int getGender_therapist() {
        return gender_therapist;
    }

    public void setGender_therapist(int gender_therapist) {
        this.gender_therapist = gender_therapist;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
