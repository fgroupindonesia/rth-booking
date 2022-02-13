package object;

public class ScheduleDay {

    private String date_chosen;
    private int totalSession = 5;
    private int counter1 = 0, counter0 = 0;
    private int counterLegend = 0;
    String legendStatus;

    public static String STATUS_GREEN = "green", STATUS_YELLOW = "yellow",
    STATUS_ORANGE = "orange", STATUS_RED = "red", STATUS_WHITE = "white";

    private void applyLegend(){

        if(counterLegend == 0){
            legendStatus = "green";
            // semua available
        }else if(counterLegend <= 4 && counterLegend >= 1 ){
            legendStatus = "orange";
            // terisi beberapa
        }else if(counterLegend == 5){
            legendStatus = "red";
            // terisi penuh full 5
        }else {
            legendStatus = "white";
            // belum terisi sama-sekali
        }
    }

    public String getLegendStatus(){
        return legendStatus;
    }

    public void updateCounter(Schedule ob){

        if(ob.getStatus()==0){
            counter0++;
            if(counter1!=0) {
                counter1--;
            }
        }else {
          counter1++;
          if(counter0!=0) {
              counter0--;
          }
        }

        if(counter1==5 || counter1==0) {
            counterLegend = counter1;
        }else if(counter1 > counter0) {
            counterLegend = totalSession - counter1;
        } else if (counter0 > 0){
            counterLegend = totalSession - counter0;
        }

        applyLegend();

    }

    public ScheduleDay(Schedule sb){
        this.setDate_chosen(sb.getDate_chosen());
        updateCounter(sb);

    }


    public String getDate_chosen() {
        return date_chosen;
    }

    public void setDate_chosen(String date_chosen) {
        this.date_chosen = date_chosen;
    }

}
