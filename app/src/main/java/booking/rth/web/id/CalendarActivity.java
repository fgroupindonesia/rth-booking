package booking.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import object.Keys;
import object.Schedule;
import object.ScheduleDay;

public class CalendarActivity extends AppCompatActivity implements Navigator {

    TextView textViewNamaBulan;
    TableRow tableRow1, tableRow2, tableRow3, tableRow4, tableRow5;
    ImageView imageViewUserProfile, imageViewPreviousMonth, imageViewNextMonth;
    int currentMonth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        tableRow1 = (TableRow) findViewById(R.id.tableRow1);
        tableRow2 = (TableRow) findViewById(R.id.tableRow2);
        tableRow3 = (TableRow) findViewById(R.id.tableRow3);
        tableRow4 = (TableRow) findViewById(R.id.tableRow4);
        tableRow5 = (TableRow) findViewById(R.id.tableRow5);

        imageViewPreviousMonth = (ImageView) findViewById(R.id.imageViewPreviousMonth);
        imageViewNextMonth = (ImageView) findViewById(R.id.imageViewNextMonth);

        // bulan with Tahun
        textViewNamaBulan = (TextView) findViewById(R.id.textViewNamaBulan);

        imageViewUserProfile = (ImageView) findViewById(R.id.imageViewUserProfile);

        centerTitleApp();
        creatingDefaultCalendar();

        updateUserProfile();

    }

    private void centerTitleApp(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    public void nextMonth(View v){

        if(currentMonth!=12) {
            currentMonth++;
            creatingDefaultCalendar();
        }

        visibilityOfNavigation();

    }

    public void prevMonth(View v){
        if(currentMonth!=1) {
            currentMonth--;
            creatingDefaultCalendar();
        }

        visibilityOfNavigation();

    }

    private void visibilityOfNavigation(){
        if(currentMonth==12){
            imageViewNextMonth.setVisibility(View.INVISIBLE);
        } else if(currentMonth>1){
            imageViewPreviousMonth.setVisibility(View.VISIBLE);
        } else if(currentMonth==1){
            imageViewPreviousMonth.setVisibility(View.INVISIBLE);
        }
    }

    private void updateUserProfile(){

        int  modeUser = UserProfile.USER_GENDER;

        if(modeUser==Keys.MODE_IKHWAN){
            imageViewUserProfile.setImageResource(R.drawable.ikhwan_logo);
        }else if(modeUser==Keys.MODE_AKHWAT){
            imageViewUserProfile.setImageResource(R.drawable.akhwat_logo);
        }


    }

    private void obtainingAllData(String bulanTahunIndo, String bulanTahunEnglish){
        WebRequest httpCall = new WebRequest(CalendarActivity.this,CalendarActivity.this);

        // posting to server with default english language instead of Indonesian
        //DateFormat sdfIndo = new SimpleDateFormat("MMMM yyyy", new Locale("ID"));
        //DateFormat sdf = new SimpleDateFormat("MMMM yyyy");

        //String mYear = sdf.format(new Date());
        //String mYearIndo = sdfIndo.format(new Date());

        String mYear = bulanTahunEnglish;
        String mYearIndo = bulanTahunIndo;

        textViewNamaBulan.setText(mYearIndo);

        // get the schedule for this specific gender on
        // this specific month of the year
        httpCall.addData("month_year", mYear);
        httpCall.addData("gender", String.valueOf(UserProfile.USER_GENDER));

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.ScheduleAll);
        httpCall.execute();

       // ShowDialog.message(this, "Anda posting timing " + mYear);

    }

    private void openDateSpecific(Object objMasuk){
        // format n bulan (MMMM) tahun (yyyy)

        // objMasuk is tagging with several values:
        // LEGEND;Date (yyyy-mm-dd)
        // for example:
        // white;2022-02-01

        // known as new data for 1 february 2022
        String dataTag [] = objMasuk.toString().split(";");

        Intent i = new Intent(this, DateSpecificActivity.class);

      //  ShowDialog.shortMessage(this, " yg didpat ialah." + objMasuk);
        // we then post the legend status as String
        i.putExtra(Keys.LEGEND, dataTag[0] );
        // along with the date
        i.putExtra(Keys.DATE_CHOSEN, dataTag[1]);


        startActivity(i);

        // we closed the calender at the moment
        this.finish();

    }

    private String numberToString(int n){
        String x = null;

        if(n<10){
            x = "0" + n;
        }else{
            x = String.valueOf(n);
        }

        return x;
    }

    public String generateTagValue(String legend, String tahunBulan42digit, int valDate){
        // combining legend with dateIn
        // for example : white;yyyy-mm-dd
        // DateFormat sdf = new SimpleDateFormat("yyyy-MM");

        //String computerFormat = sdf.format(new Date());
        String computerFormat = tahunBulan42digit;

        String dateIn = computerFormat + "-" + numberToString(valDate);

        return legend + ";" + dateIn;
    }

        private void setValue(int startingIndex, int limitNumber, String tahunBulan42Digit){

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

            // giving a tag of White (default) for future usage when clicked
            dummy.setTag(generateTagValue(Keys.LEGEND_WHITE, tahunBulan42Digit, number));

            dummy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ketika di click
                    // ambil data tag
                    TextView txt  = (TextView ) v;
                    Object tag = txt.getTag();

                    // tag value is
                    // using LEGEND;yyyy-mm-dd format
                   openDateSpecific(tag);
                }
            });

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

    private void clearingAllMarkers(){

        int totalBox = 35;
        int currentBox = 1;
        int rowCount = 1;
        int i=0;
        int limitIndex = 6;

        View v = null;
        TextView dummy = null;


        // matching into the calendar (UI)
        while(currentBox<=totalBox) {

            if (rowCount == 1) {
                v = tableRow1.getChildAt(i);
            } else if (rowCount == 2) {
                v = tableRow2.getChildAt(i);
            } else if (rowCount == 3) {
                v = tableRow3.getChildAt(i);
            } else if (rowCount == 4) {
                v = tableRow4.getChildAt(i);
            } else if (rowCount == 5) {
                v = tableRow5.getChildAt(i);
            }
            //do something with your child element
            dummy = (TextView) v;
            dummy.setBackgroundResource(0);
            dummy.setText(null);

            if(i==limitIndex){
                i = 0;
                rowCount++;
            }else{
                i++;
            }

            currentBox++;

        }
    }

    int indexPos = -1;
    int limitNumberDay;

    private void creatingDefaultCalendar(){


        Calendar dateNow = Calendar.getInstance();
        // return back to 1st day
        dateNow.set(Calendar.DAY_OF_MONTH, 1);

        if(currentMonth==0){

            // this is for navigation purposes

            DateFormat sdfTemp = new SimpleDateFormat("yyyy-MM-dd", new Locale("ID"));
            // this one is yyyy-MM-dd format
            String dateTemp = sdfTemp.format(dateNow.getTime());
            // the number used here as a reference for navigation
            currentMonth = Integer.parseInt(dateTemp.split("-")[1]);

        }else {
            // when the currentMonth is not zero
            // thus we set the month accordingly
            // when currentMonth is March = Feb++

            dateNow.set(Calendar.MONTH, currentMonth-1);
            clearingAllMarkers();

        }


        DateFormat sdf = new SimpleDateFormat("EEEE MMMM yyyy", new Locale("ID"));
        DateFormat sdfEnglish = new SimpleDateFormat("MMMM yyyy");
        DateFormat sdfIndo = new SimpleDateFormat("MMMM yyyy", new Locale("ID"));
        DateFormat sdf42Digit = new SimpleDateFormat("yyyy-MM");


        String date = sdf.format(dateNow.getTime());
        String bulanTahunIndo = sdfIndo.format(dateNow.getTime());
        String bulanTahunEnglish = sdfEnglish.format(dateNow.getTime());
        String tahunBulan42Digit = sdf42Digit.format(dateNow.getTime());

        obtainingAllData(bulanTahunIndo, bulanTahunEnglish);

        String namaHariAwalBulan = date.split(" ")[0].toLowerCase();
        int lastDayThisMonth = dateNow.getActualMaximum(Calendar.DAY_OF_MONTH);

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

      //  ShowDialog.message(this, "Membuat "+ namaHariAwalBulan + " " + indexPos + " dan " + lastDayThisMonth);
        setValue(indexPos, lastDayThisMonth, tahunBulan42Digit);

    }

    public void chooseProfile(View v){

        Intent i = new Intent(this, UserProfileActivity.class);
        startActivity(i);

        finish();

    }


    // add the data into the pool
    ScheduleCounter scheduleMachine = new ScheduleCounter();

    private void addingDataRow(Schedule objectIn){

        //ShowDialog.message(this, "Adding into row... " + objectIn.getDate_chosen());

        // adding to the machine counter
        scheduleMachine.addSchedule(objectIn);

        // now applying into the calendar UI
        int totalBox = 35;
        int currentBox = 1;
        int rowCount = 1;
        int i=0;
        int limitIndex = 6;

        View v = null;
        TextView dummy = null;
        String dataTag [] = null;

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

            if(dummy.getTag()!=null) {
                dataTag = dummy.getTag().toString().split(";");
            }

            // value is based upon
            // LEGEND;yyyy-mm-dd
            // important to use dataTag[1];

            String valTag = null;
            // check the text numerical with the date given from schedule
            // we ask machine ScheduleCounter to help us
           if(scheduleMachine.isDateEqual(dummy, objectIn)){
               if(scheduleMachine.getStatus(objectIn).equals(Keys.LEGEND_GREEN)){
                   valTag = Keys.LEGEND_GREEN + ";" + dataTag[1];
                   dummy.setTag(valTag);
                   dummy.setBackgroundResource(R.drawable.circular_green);
               } else if(scheduleMachine.getStatus(objectIn).equals(Keys.LEGEND_ORANGE)){
                   valTag = Keys.LEGEND_ORANGE + ";" + dataTag[1];
                   dummy.setTag(valTag);
                   dummy.setBackgroundResource(R.drawable.circular_orange);
               } else if(scheduleMachine.getStatus(objectIn).equals(Keys.LEGEND_RED)){
                   valTag = Keys.LEGEND_RED + ";" + dataTag[1];
                   dummy.setTag(valTag);
                   dummy.setBackgroundResource(R.drawable.circular_red);
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
        // nothing happened here
    }

    @Override
    public void onSuccess(String urlTarget, String respond) {

        try {
            //ShowDialog.message(this, "Returned  " + respond);

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

                //ShowDialog.message(this, "tidak ada data dari Server!");
                //finish();

            }
        } catch (Exception ex) {
            ShowDialog.message(this, "Error " + ex.getMessage());
            ex.printStackTrace();
        }


    }
}