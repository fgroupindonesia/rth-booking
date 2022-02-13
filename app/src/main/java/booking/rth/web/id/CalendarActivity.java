package booking.rth.web.id;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import helper.Navigator;
import helper.RespondHelper;
import helper.ScheduleCounter;
import helper.ShowDialog;
import helper.URLReference;
import helper.UserProfile;
import helper.WebRequest;
import object.Schedule;
import object.ScheduleDay;

public class CalendarActivity extends AppCompatActivity implements Navigator {

    TextView textViewTgl00, textViewTgl01, textViewTgl02, textViewTgl03,
            textViewTgl04, textViewTgl05, textViewTgl06;
    TableRow tableRow1, tableRow2, tableRow3, tableRow4, tableRow5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        tableRow1 = (TableRow) findViewById(R.id.tableRow1);
        tableRow2 = (TableRow) findViewById(R.id.tableRow2);
        tableRow3 = (TableRow) findViewById(R.id.tableRow3);
        tableRow4 = (TableRow) findViewById(R.id.tableRow4);
        tableRow5 = (TableRow) findViewById(R.id.tableRow5);

        obtainingAllData();
        creatingDefaultCalendar();

    }

    private void obtainingAllData(){
        WebRequest httpCall = new WebRequest(CalendarActivity.this,CalendarActivity.this);

        // posting to server with default english language instead of Indonesian
        //DateFormat sdf = new SimpleDateFormat("MMMM yyyy", new Locale("ID"));
        DateFormat sdf = new SimpleDateFormat("MMMM yyyy");

        String mYear = sdf.format(new Date());

        httpCall.addData("month_year", mYear);

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.ScheduleAll);
        httpCall.execute();


        ShowDialog.message(this, "Anda posting timing " + mYear);

    }

        private void setValue(int startingIndex, int limitNumber){

            limitNumberDay = limitNumber;

        // used for day numbering
        int number = 1;

        // used for indexing days (0-6)
        int i=startingIndex;
        int rowCount = 1;
        int lastDayIndex = 6;

        View v = null;
        TextView dummy;
        // i will iterate from 0 to 6
        // and number will iterate from 1 to 30 or 31 or even 29 depend upon the limit
        while(number<=limitNumber) {

            if(rowCount == 1) {
                v = tableRow1.getChildAt(i);
            } else if(rowCount == 2){
                v = tableRow2.getChildAt(i);
            } else if(rowCount == 3 ){
                v = tableRow3.getChildAt(i);
            } else if(rowCount == 4 ){
                v = tableRow4.getChildAt(i);
            }else if(rowCount == 5){
                v = tableRow5.getChildAt(i);
            }
            //do something with your child element
            dummy = (TextView) v;
            dummy.setText(String.valueOf(number));

            // when it reached the last x-order indexes
            if(i<lastDayIndex){
                i++;
            }else{
                i = 0;
                rowCount++;
            }

            number++;
        }
    }

    private boolean isEqual(String n, String m){
        return n.toLowerCase().equalsIgnoreCase(m);
    }

    int indexPos = -1;
    int limitNumberDay;

    private void creatingDefaultCalendar(){

        Calendar dateNow = Calendar.getInstance();
        // return back to 1st day
        dateNow.set(Calendar.DAY_OF_MONTH, 1);

        DateFormat sdf = new SimpleDateFormat("EEEE MMMM yyyy", new Locale("ID"));
        String date = sdf.format(dateNow.getTime());

        String namaHariAwalBulan = date.split(" ")[0].toLowerCase();
        int lastDayThisMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

        // matching index with day to calendar table
        if(isEqual(namaHariAwalBulan, "senin")){
            indexPos = 1;
        }else if(isEqual(namaHariAwalBulan, "selasa")){
            indexPos = 2;
        } else if(isEqual(namaHariAwalBulan, "rabu")){
            indexPos = 3;
        } else if(isEqual(namaHariAwalBulan, "kamis")){
            indexPos = 4;
        } else if(isEqual(namaHariAwalBulan, "jumat")){
            indexPos = 5;
        } else if(isEqual(namaHariAwalBulan, "sabtu")){
            indexPos = 6;
        } else if(isEqual(namaHariAwalBulan, "minggu")){
            indexPos = 0;
        }

        ShowDialog.message(this, "Membuat "+ namaHariAwalBulan + " " + indexPos + " dan " + lastDayThisMonth);
        setValue(indexPos, lastDayThisMonth);

    }

    public void chooseProfile(View v){

        Intent i = new Intent(this, UserProfileActivity.class);
        startActivity(i);

        finish();

    }


    // add the data into the pool
    ScheduleCounter machine = new ScheduleCounter();

    private void addingDataRow(Schedule objectIn){

        ShowDialog.message(this, "Adding into row... " + objectIn.getDate_chosen());

        // adding to the machine counter
        machine.addSchedule(objectIn);

        // now applying into the calendar UI
        int totalBox = 35;
        int currentBox = 1;
        int rowCount = 1;
        int i=0;
        int limitIndex = 6;

        View v = null;
        TextView dummy = null;

        boolean found = false;

        // matching into the calendar (UI)
        while(!found) {

            if(rowCount == 1) {
                v = tableRow1.getChildAt(i);
            } else if(rowCount == 2){
                v = tableRow2.getChildAt(i);
            } else if(rowCount == 3 ){
                v = tableRow3.getChildAt(i);
            } else if(rowCount == 4 ){
                v = tableRow4.getChildAt(i);
            }else if(rowCount == 5){
                v = tableRow5.getChildAt(i);
            }
            //do something with your child element
            dummy = (TextView) v;

            // check the text numerical with the date given from schedule
            // we ask machine ScheduleCounter to help us
           if(machine.isDateEqual(dummy, objectIn)){
               if(machine.getStatus(objectIn).equals(ScheduleDay.STATUS_GREEN)){
                   dummy.setBackgroundResource(R.drawable.circular_green);
               } else if(machine.getStatus(objectIn).equals(ScheduleDay.STATUS_ORANGE)){
                   dummy.setBackgroundResource(R.drawable.circular_orange);
               } else if(machine.getStatus(objectIn).equals(ScheduleDay.STATUS_RED)){
                   dummy.setBackgroundResource(R.drawable.circular_red);
               } else if(machine.getStatus(objectIn).equals(ScheduleDay.STATUS_WHITE)){
                   dummy.setBackgroundResource(R.drawable.circular_white);
               }

               found = true;
           }

           if(i==6){
               i = 0;
               rowCount++;
           }else{
               i++;
           }

           currentBox++;

        }

    }

    @Override
    public void nextActivity() {

    }

    @Override
    public void onSuccess(String urlTarget, String respond) {

        try {
            ShowDialog.message(this, "Returned  " + respond);

            Gson objectG = new Gson();

            if (RespondHelper.isValidRespond(respond)) {

                if (urlTarget.contains(URLReference.ScheduleAll)) {

                    JSONArray jsons = RespondHelper.getArray(respond, "multi_data");

                    JsonParser parser = new JsonParser();
                    JsonElement mJson =  parser.parse(jsons.toString());
                    Gson gson = new Gson();
                    Schedule object [] = gson.fromJson(mJson, Schedule[].class);

                    // this is for specific month only
                    // data returned is restricted by month of current year

                    for (Schedule single:object){
                        addingDataRow(single);
                    }



                }

            } else if (!RespondHelper.isValidRespond(respond)) {

                ShowDialog.message(this, "tidak ada data dari Server!");
                //finish();

            }
        } catch (Exception ex) {
            ShowDialog.message(this, "Error " + ex.getMessage());
            ex.printStackTrace();
        }


    }
}