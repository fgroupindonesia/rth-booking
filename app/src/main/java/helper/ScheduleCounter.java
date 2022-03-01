package helper;

import android.widget.TextView;

import java.util.ArrayList;

import object.Schedule;
import object.ScheduleDay;

public class ScheduleCounter {

    public boolean isDateEqual(TextView txt, Schedule obj){

        boolean valid = false;

        // the date from the TextView is actually a single numerical
        // ie: 1 not 01
        String val = txt.getText().toString();
        if(!val.isEmpty()) {
            int num = Integer.parseInt(val);

            // the date is using yyyy-mm-dd format
           int day = Integer.parseInt(obj.getDate_chosen().split("-")[2]);

           if(num == day){
               valid = true;
           }
        }

        return valid;

    }

    ArrayList <ScheduleDay> list = new ArrayList<ScheduleDay>();

    // countNA = not availability - has many 1
    // countA = availability - has many 0

    public void addSchedule(Schedule ob){

        boolean  found = false;

        for(ScheduleDay obInner : list){
            if(obInner.getDate_chosen().equals(ob.getDate_chosen())){
                obInner.updateCounter(ob);
                found = true;
                break;
            }
        }

        if(!found){
        list.add(new ScheduleDay(ob));
        }

    }

    public String getStatus(Schedule obj){

        String status = null;

        for(ScheduleDay obInner: list){
            if(obInner.getDate_chosen().equals(obj.getDate_chosen())){
               status = obInner.getLegendStatus();
               break;
            }
        }               

        return status;

    }

}
