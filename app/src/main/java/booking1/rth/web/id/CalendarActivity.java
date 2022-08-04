package booking1.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import helper.Navigator;
import helper.RespondHelper;
import helper.ScheduleCounter;
import helper.ScheduleOverall;
import helper.ShowDialog;
import helper.URLReference;
import helper.WebRequest;
import object.Hijri;
import object.Keys;
import object.Month;
import object.Ruqyah;
import object.Schedule;
import object.Weekday;
import shared.UserData;

public class CalendarActivity extends AppCompatActivity implements Navigator {

    TextView textViewNamaBulan, textViewTglMasehi,
            textViewOverallData, textViewTglHijriyyah;
    TableRow tableRow1, tableRow2, tableRow3, tableRow4, tableRow5, tableRow6;
    ImageView imageViewUserProfile, imageViewPreviousMonth, imageViewNextMonth;
    int currentMonth = 0, gender;
    int totalBox = 42;

    TextView textViewCalendarTitleCommand;

    EditText editTextOverallData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // stored for future usage
        UserData.setPreference(this);

        currentMonth = UserData.getPreferenceInt(Keys.CALENDAR_OPENED);

        textViewCalendarTitleCommand = (TextView) findViewById(R.id.textViewCalendarTitleCommand);

        textViewTglMasehi = (TextView) findViewById(R.id.textViewTglMasehi);
        textViewTglHijriyyah = (TextView) findViewById(R.id.textViewTglHijriyyah);

        editTextOverallData = (EditText) findViewById(R.id.editTextOverallData);

       // textViewOverallData = (TextView) findViewById(R.id.textViewOverallData);
        changeTextAfter3Seconds();

        tableRow1 = (TableRow) findViewById(R.id.tableRow1);
        tableRow2 = (TableRow) findViewById(R.id.tableRow2);
        tableRow3 = (TableRow) findViewById(R.id.tableRow3);
        tableRow4 = (TableRow) findViewById(R.id.tableRow4);
        tableRow5 = (TableRow) findViewById(R.id.tableRow5);
        tableRow6 = (TableRow) findViewById(R.id.tableRow6);

        imageViewPreviousMonth = (ImageView) findViewById(R.id.imageViewPreviousMonth);
        imageViewNextMonth = (ImageView) findViewById(R.id.imageViewNextMonth);

        // bulan with Tahun
        textViewNamaBulan = (TextView) findViewById(R.id.textViewNamaBulan);

        imageViewUserProfile = (ImageView) findViewById(R.id.imageViewUserProfile);

        // setting the gender from here
        updateUserProfile();

        centerTitleApp();
        creatingDefaultCalendar();

        // request to server another API REST CALL
        getIslamicDate();

    }

    private void getIslamicDate() {

        WebRequest httpCall = new WebRequest(CalendarActivity.this, CalendarActivity.this);

        // posting to server with simple date
        DateFormat sdfIndo = new SimpleDateFormat("dd-MM-yyyy");

        String dataIndo = sdfIndo.format(new Date());

        httpCall.addData("date", dataIndo);

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setDownloadState(false);
        httpCall.setMultipartform(false);

        httpCall.setRequestMethod(WebRequest.GET_METHOD);
        httpCall.setTargetURL(URLReference.AdhanWebsite);
        httpCall.execute();

    }

    private void centerTitleApp() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    public void nextMonth(View v) {

        if (currentMonth != 12) {
            currentMonth++;
            creatingDefaultCalendar();
        }

        visibilityOfNavigation();

    }

    public void prevMonth(View v) {
        if (currentMonth != 1) {
            currentMonth--;
            creatingDefaultCalendar();
        }

        visibilityOfNavigation();

    }

    private void visibilityOfNavigation() {
        if (currentMonth == 12) {
            imageViewNextMonth.setVisibility(View.INVISIBLE);
        } else if (currentMonth > 1) {
            imageViewPreviousMonth.setVisibility(View.VISIBLE);
        } else if (currentMonth == 1) {
            imageViewPreviousMonth.setVisibility(View.INVISIBLE);
        }
    }

    private void updateUserProfile() {

        gender = UserData.getPreferenceInt(Keys.USER_GENDER);

        if (gender == Keys.MODE_IKHWAN) {
            imageViewUserProfile.setImageResource(R.drawable.ikhwan_logo);
        } else if (gender == Keys.MODE_AKHWAT) {
            imageViewUserProfile.setImageResource(R.drawable.akhwat_logo);
        }


    }

    // stored for MMMM yyyy format date
    String mYear;
    private void obtainingAllData(String bulanTahunIndo, String bulanTahunEnglish) {
        WebRequest httpCall = new WebRequest(CalendarActivity.this, CalendarActivity.this);

        // posting to server with default english language instead of Indonesian
        // DateFormat sdfIndo = new SimpleDateFormat("MMMM yyyy", new Locale("ID"));
        // DateFormat sdf = new SimpleDateFormat("MMMM yyyy");

        // String mYear = sdf.format(new Date());
        // String mYearIndo = sdfIndo.format(new Date());

        mYear = bulanTahunEnglish;
        String mYearIndo = bulanTahunIndo;

        textViewNamaBulan.setText(mYearIndo);

        // get the schedule for this specific gender on
        // this specific month of the year
        httpCall.addData("month_year", mYear);
        httpCall.addData("gender_therapist", String.valueOf(gender));

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.ScheduleAll);
        httpCall.execute();

        // ShowDialog.message(this, "Anda posting timing " + mYear);



    }

    private void obtainAllDataRuqyah( ){

        // another data call for ruqyah data
        WebRequest httpCallR = new WebRequest(CalendarActivity.this, CalendarActivity.this);

        // get the ruqyah data for this specific gender on
        // this specific month of the year
        httpCallR.addData("month_year", mYear);
        httpCallR.addData("gender_therapist", String.valueOf(gender));

        // we need to wait for the response
        httpCallR.setWaitState(true);
        httpCallR.setRequestMethod(WebRequest.POST_METHOD);
        httpCallR.setTargetURL(URLReference.RuqyahAll);
        httpCallR.execute();

    }

    private void openDateSpecific(Object objMasuk) {
        // format n bulan (MMMM) tahun (yyyy)

        // objMasuk is tagging with several values:
        // LEGEND;Date (yyyy-mm-dd)
        // for example:
        // white;2022-02-01

        // known as new data for 1 february 2022
        String dataTag[] = objMasuk.toString().split(";");
        String tglSplited[] = dataTag[1].split("-");

        Intent i = new Intent(this, DateSpecificActivity.class);

        //  ShowDialog.shortMessage(this, " yg didpat ialah." + objMasuk);
        // we then post the legend status as String
        i.putExtra(Keys.LEGEND, dataTag[0]);
        // along with the date
        i.putExtra(Keys.DATE_CHOSEN, dataTag[1]);

        // we save the temporarily month number
        int monthNum = Integer.parseInt(tglSplited[1]);
        UserData.savePreference(Keys.CALENDAR_OPENED, monthNum);


        startActivity(i);

        // we closed the calender at the moment
        this.finish();

    }

    private String numberToString(int n) {
        String x = null;

        if (n < 10) {
            x = "0" + n;
        } else {
            x = String.valueOf(n);
        }

        return x;
    }

    public String generateTagValue(String legend, String tahunBulan42digit, int valDate) {
        // combining legend with dateIn
        // for example : white;yyyy-mm-dd
        // DateFormat sdf = new SimpleDateFormat("yyyy-MM");

        //String computerFormat = sdf.format(new Date());
        String computerFormat = tahunBulan42digit;

        String dateIn = computerFormat + "-" + numberToString(valDate);

        return legend + ";" + dateIn;
    }

    private void setValue(int startingIndex, int limitNumber, String tahunBulan42Digit) {

        limitNumberDay = limitNumber;

        // used for day numbering
        int number = 1;

        // used for indexing days (0-6)
        int i = startingIndex;
        int rowCount = 1;
        int lastDayIndex = 6;

        View v = null;
        TextView dummy;
        // i will iterate from 0 to 6
        // and number will iterate from 1 to 30 or 31 or even 29 depend upon the limit
        while (number <= limitNumber) {

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
            } else if (rowCount == 6) {
                v = tableRow6.getChildAt(i);
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
                    TextView txt = (TextView) v;
                    Object tag = txt.getTag();

                    // tag value is
                    // using LEGEND;yyyy-mm-dd format
                    openDateSpecific(tag);
                }
            });

            dummy.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                   // opening as client for preview
                    // obtaining the date using
                    // EEEE dd-MMM-yyyy format passed
                    // but split it under the tag
                    // FIRST-POST [0] is coloring scheme
                    // SECOND-POST [1] is the date located,
                    // but it still using yyyy-MM-dd format

                    String dateNa = v.getTag().toString().split(";")[1];

                    String dateWantedFormat = convertDateFormat(dateNa, "EEEE dd-MMM-yyyy", Keys.LANGUAGE_ID);

                    previewDateAsClient(dateWantedFormat);

                    return true;
                }
            });

            // when it reached the last x-order indexes
            if (i < lastDayIndex) {
                i++;
            } else {
                i = 0;
                rowCount++;
            }

            number++;
        }
    }

    private String convertDateFormat(String in, String dateFormatResult, int languageResult) {

        String res = null;

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat targetFormat = null;

        if (languageResult == Keys.LANGUAGE_EN) {
            targetFormat = new SimpleDateFormat(dateFormatResult);
        } else {
            targetFormat = new SimpleDateFormat(dateFormatResult, new Locale("ID"));
        }

        try {
            Date date = originalFormat.parse(in);
            res = targetFormat.format(date);
        } catch (Exception ex) {

        }

        return res;

    }

    private boolean isEqual(String n, String m) {
        return n.toLowerCase().equalsIgnoreCase(m);
    }

    private void clearingAllMarkers() {


        int currentBox = 1;
        int rowCount = 1;
        int i = 0;
        int limitIndex = 6;

        View v = null;
        TextView dummy = null;


        // matching into the calendar (UI)
        while (currentBox <= totalBox) {

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
            } else if (rowCount == 6) {
                v = tableRow6.getChildAt(i);
            }
            //do something with your child element
            dummy = (TextView) v;
            dummy.setBackgroundResource(0);
            dummy.setText(null);

            if (i == limitIndex) {
                i = 0;
                rowCount++;
            } else {
                i++;
            }

            currentBox++;

        }
    }

    int indexPos = -1;
    int limitNumberDay;

    private void creatingDefaultCalendar() {


        Calendar dateNow = Calendar.getInstance();
        // return back to 1st day
        dateNow.set(Calendar.DAY_OF_MONTH, 1);

        if (currentMonth == 0 || currentMonth == -1) {

            // this is for navigation purposes

            DateFormat sdfTemp = new SimpleDateFormat("yyyy-MM-dd", new Locale("ID"));
            // this one is yyyy-MM-dd format
            String dateTemp = sdfTemp.format(dateNow.getTime());
            // the number used here as a reference for navigation
            currentMonth = Integer.parseInt(dateTemp.split("-")[1]);

        } else {
            // when the currentMonth is not zero
            // thus we set the month accordingly
            // when currentMonth is March = Feb++

            dateNow.set(Calendar.MONTH, currentMonth - 1);
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
        if (isEqual(namaHariAwalBulan, "senin")) {
            indexPos = 1;
        } else if (isEqual(namaHariAwalBulan, "selasa")) {
            indexPos = 2;
        } else if (isEqual(namaHariAwalBulan, "rabu")) {
            indexPos = 3;
        } else if (isEqual(namaHariAwalBulan, "kamis")) {
            indexPos = 4;
        } else if (isEqual(namaHariAwalBulan, "jumat")) {
            indexPos = 5;
        } else if (isEqual(namaHariAwalBulan, "sabtu")) {
            indexPos = 6;
        } else if (isEqual(namaHariAwalBulan, "minggu")) {
            indexPos = 0;
        }

        //  ShowDialog.message(this, "Membuat "+ namaHariAwalBulan + " " + indexPos + " dan " + lastDayThisMonth);
        setValue(indexPos, lastDayThisMonth, tahunBulan42Digit);

    }

    public void chooseProfile(View v) {

        Intent i = new Intent(this, UserProfileActivity.class);
        startActivity(i);

        finish();

    }

    @Override
    public void onDestroy(){
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    final int TIME_TEXT_CHANGED = 3000;
    final Handler handler = new Handler(Looper.getMainLooper());

    private void changeTextAfter3Seconds(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if(textViewCalendarTitleCommand.getTag()!=null){
                    textViewCalendarTitleCommand.setText("Long Press di satu tanggal (Preview)");
                    textViewCalendarTitleCommand.setTag(null);
                    blinkingTitle(true);
                }else {
                    textViewCalendarTitleCommand.setTag(Keys.INFO_TEXT_PILIH_TANGGAL);
                    textViewCalendarTitleCommand.setText("Pilih tanggal yg akan di-set");
                    blinkingTitle(false);
                }

                changeTextAfter3Seconds();
            }
        }, TIME_TEXT_CHANGED);
    }

    private void blinkingTitle(boolean b){
        if(b) {
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking);
            textViewCalendarTitleCommand.startAnimation(animation);
        }else{
            textViewCalendarTitleCommand.clearAnimation();
        }
    }

    public void previewDateAsClient(String dateText) {
        // dateText is using special format such as :
        // EEEE dd-MMM-yyyy

        Intent i = new Intent(this, BookingScheduleActivity.class);

        // stored data access type for differentiating one to another access
        UserData.savePreference(Keys.USER_USAGE, Keys.ACCESS_MANAGEMENT);

        // and here we put the date stored for data passing used in the next activity
        // in an Indonesian format
        i.putExtra(Keys.DATE_CHOSEN, dateText);

       // ShowDialog.message(this, "we send " + dateText);

        startActivity(i);

    }

    private void recoloringDataRow(Ruqyah objectIn){
        // now applying into the calendar UI
        int currentBox = 1;
        int rowCount = 1;
        int i = 0;

        View v = null;
        TextView dummy = null;
        String dataTag[] = null;

        boolean found = false;

        // matching into the calendar (UI)
        while (!found) {

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
            } else if (rowCount == 6) {
                v = tableRow6.getChildAt(i);
            }
            //do something with your child element
            dummy = (TextView) v;

            if (dummy.getTag() != null) {
                dataTag = dummy.getTag().toString().split(";");
            }

            // value is based upon
            // LEGEND;yyyy-mm-dd
            // important to use dataTag[1];

            String valTag = null;
            // check the text numerical with the date given from data_chosen of ruqyah
            if (scheduleMachine.isDateEqual(dummy, objectIn)) {
                if (objectIn.getStatus() == Keys.TOGGLE_ON) {
                    valTag = Keys.LEGEND_PINK + ";" + dataTag[1];
                    dummy.setTag(valTag);
                    dummy.setBackgroundResource(R.drawable.circular_pink);
                }
                found = true;
            }

            if (i == 6) {
                i = 0;
                rowCount++;
            } else {
                i++;
            }

            currentBox++;

        }
    }

    // add the data into the pool
    ScheduleCounter scheduleMachine = new ScheduleCounter();
    StringBuffer stb = new StringBuffer();
    ScheduleOverall schedOver = new ScheduleOverall();

    private void addingDataRow(Schedule objectIn) {

        schedOver.populate(objectIn);

        // ShowDialog.message(this, "Adding into row... " + objectIn.getDate_chosen());

        // adding to the machine counter
        scheduleMachine.addSchedule(objectIn);

        // now applying into the calendar UI

        int currentBox = 1;
        int rowCount = 1;
        int i = 0;
        int limitIndex = 6;

        View v = null;
        TextView dummy = null;
        String dataTag[] = null;

        boolean found = false;

        // matching into the calendar (UI)
        while (!found) {

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
            }  else if (rowCount == 6) {
                v = tableRow6.getChildAt(i);
            }
            //do something with your child element
            dummy = (TextView) v;

            if (dummy.getTag() != null) {
                dataTag = dummy.getTag().toString().split(";");
            }

            // value is based upon
            // LEGEND;yyyy-mm-dd
            // important to use dataTag[1];

            String valTag = null;
            // check the text numerical with the date given from schedule
            // we ask machine ScheduleCounter to help us
            if (scheduleMachine.isDateEqual(dummy, objectIn)) {
                if (scheduleMachine.getStatus(objectIn).equals(Keys.LEGEND_GREEN)) {
                    valTag = Keys.LEGEND_GREEN + ";" + dataTag[1];
                    dummy.setTag(valTag);
                    dummy.setBackgroundResource(R.drawable.circular_green);
                } else if (scheduleMachine.getStatus(objectIn).equals(Keys.LEGEND_ORANGE)) {
                    valTag = Keys.LEGEND_ORANGE + ";" + dataTag[1];
                    dummy.setTag(valTag);
                    dummy.setBackgroundResource(R.drawable.circular_orange);
                } else if (scheduleMachine.getStatus(objectIn).equals(Keys.LEGEND_RED)) {
                    valTag = Keys.LEGEND_RED + ";" + dataTag[1];
                    dummy.setTag(valTag);
                    dummy.setBackgroundResource(R.drawable.circular_red);
                }

                found = true;
            }

            if (i == 6) {
                i = 0;
                rowCount++;
            } else {
                i++;
            }

            currentBox++;

        }

    }

    @Override
    public void nextActivity() {
        // nothing happened here
    }

    public void goToLineToday(View v){
        // give a space for several character below
        String tglNowIndo = new SimpleDateFormat("EEEE dd-MM-yyyy", new Locale("ID")).format(new Date());
        int post = schedOver.getCompleteText().indexOf(tglNowIndo);
        editTextOverallData.setSelection(post, post+tglNowIndo.length());
        editTextOverallData.requestFocus(post);
    }

    @Override
    public void onSuccess(String urlTarget, String respond) {

        try {
            //ShowDialog.message(this, "Returned  " + respond);

            Gson gson = new Gson();

            JsonParser parser = new JsonParser();

            if (RespondHelper.isValidRespond(respond)) {

                if (urlTarget.contains(URLReference.ScheduleAll)) {

                    JSONArray jsons = RespondHelper.getArray(respond, "multi_data");

                    JsonElement mJson = parser.parse(jsons.toString());

                    Schedule object[] = gson.fromJson(mJson, Schedule[].class);

                    Arrays.sort(object, new Comparator<Schedule>() {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date d1, d2;

                        public int compare(Schedule o1, Schedule o2) {

                            try {
                                d1 = sdf.parse(o1.getDate_chosen());
                                d2 = sdf.parse(o2.getDate_chosen());
                            } catch (Exception ex) {

                            }

                            return d1.compareTo(d2);
                        }
                    });

                    // this is for specific month only
                    // data returned is restricted by month of current year

                    for (Schedule single : object) {
                        addingDataRow(single);
                    }

                    // do once more for filtering layout
                    for (Schedule single : object) {
                        schedOver.makeSingleDay(single);
                    }

                    // ShowDialog.message(this, "total schedule is" + object.length);

                    editTextOverallData.setText(schedOver.getCompleteText());

                    // call another Web API
                    obtainAllDataRuqyah();

                } else if (urlTarget.contains(URLReference.AdhanWebsite)) {

                    //ShowDialog.message(this, respond);

                    JSONObject jsons = RespondHelper.getObject(respond, "data");

                    Hijri dataHijriyyah = gson.fromJson(jsons.getJSONObject("hijri").toString(), Hijri.class);

                    Weekday dataHijriyyahWeek = gson.fromJson(jsons.getJSONObject("hijri").getJSONObject("weekday").toString(), Weekday.class);
                    Month dataHijriyyahMonth = gson.fromJson(jsons.getJSONObject("hijri").getJSONObject("month").toString(), Month.class);

                    // ShowDialog.message(this, dataHijriyyahWeek.getEn());

                    textViewTglMasehi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.star16, 0);
                    textViewTglMasehi.setText(new SimpleDateFormat("EEEE dd-MMM-yyyy", new Locale("ID")).format(new Date()));
                    textViewTglHijriyyah.setText(dataHijriyyahWeek.getEn() + " "
                            + dataHijriyyah.getDay() + " "
                            + dataHijriyyahMonth.getEn() + " "
                            + dataHijriyyah.getYear());

                }else if(urlTarget.contains(URLReference.RuqyahAll)){
                    JSONArray jsons = RespondHelper.getArray(respond, "multi_data");

                    JsonElement mJson = parser.parse(jsons.toString());

                    Ruqyah object[] = gson.fromJson(mJson, Ruqyah[].class);

                    Arrays.sort(object, new Comparator<Ruqyah>() {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date d1, d2;

                        public int compare(Ruqyah o1, Ruqyah o2) {

                            try {
                                d1 = sdf.parse(o1.getDate_chosen());
                                d2 = sdf.parse(o2.getDate_chosen());
                            } catch (Exception ex) {

                            }

                            return d1.compareTo(d2);
                        }
                    });

                    // this is for specific month only
                    // data returned is restricted by month of current year

                    for (Ruqyah single : object) {
                        recoloringDataRow(single);
                    }

                }

            } else if (!RespondHelper.isValidRespond(respond)) {

                //ShowDialog.message(this, "tidak ada data dari Server!\n"+respond);
                //finish();

            }
        } catch (Exception ex) {
            ShowDialog.message(this, "Error di activity" + ex.getMessage());
            ex.printStackTrace();
        }


    }
}