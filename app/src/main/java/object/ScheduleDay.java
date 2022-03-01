package object;

import java.util.ArrayList;

public class ScheduleDay {

    private String date_chosen;
    private int totalSession = 5;
    private int counter1 = 0;
    private int counterLegend = -1;
    String legendStatus;

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

        if(ob.getStatus()==1){
            counter1++;
        }
            counterLegend = counter1;

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
