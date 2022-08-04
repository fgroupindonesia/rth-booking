package object;


import com.google.gson.Gson;

public class FamilyMember {
    private String nama;
    private String kelamin;
    private String hubungan;
    private String date_ordered;
    private String treatment;
    Gson ghelp = new Gson();

    public FamilyMember(){
       
    }



    public String toString(){
        return ghelp.toJson(this);
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKelamin() {
        return kelamin;
    }

    public void setKelamin(String kelamin) {
        this.kelamin = kelamin;
    }

    public String getHubungan() {
        return hubungan;
    }

    public void setHubungan(String hubungan) {
        this.hubungan = hubungan;
    }

    public String getDate_ordered() {
        return date_ordered;
    }

    public void setDate_ordered(String date_ordered) {

        this.date_ordered = date_ordered;

    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
}
