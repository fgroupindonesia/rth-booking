package booking1.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import helper.Navigator;
import helper.RespondHelper;
import helper.ShowDialog;
import helper.URLReference;
import helper.WebRequest;
import object.Keys;
import object.Ruqyah;
import object.Schedule;
import shared.UserData;

public class DateSpecificActivity extends AppCompatActivity implements Navigator {

    Schedule dataSchedule [];

    Switch elementToggle, switchJam8, switchJam10, switchJam13, switchJam16, switchJam20;
    TextView textViewTitleJam, textViewTanggalBulanTahun,
            textViewJam8, textViewJam10, textViewJam13, textViewJam16, textViewJam20,
    textViewTemp, textViewDescJam8, textViewDescJam10, textViewDescJam13, textViewDescJam16,
            textViewDescJam20;
    Button buttonDetailOK;
    EditText keteranganEditText;

    int gender;

    ImageView imageViewUserProfile, imageViewJam20, imageViewJam16, imageViewJam13,
            imageViewJam10, imageViewJam8;

    String dateComputerFormat, description;
    ProgressBar progressBarLoading;

    LinearLayout linearToggleHours;

    Switch switchRuqyahMode;

    public String getScheduleDescription(String hour){
       String v = null;

        for (Schedule s: dataSchedule){
            if(s.getSpecific_hour().equalsIgnoreCase(hour) && s.getDescription()!=null){
                if(s.getDescription().trim().length()>0) {
                    v = s.getDescription();
                }
            break;
            }
        }

        return v;
    }

    public void hideDescMe(View v){

        v.setVisibility(View.GONE);

        String tag = v.getTag().toString();

        if(tag.equalsIgnoreCase("08:00")){
            textViewJam8.setVisibility(View.VISIBLE);
            switchJam8.setVisibility(View.VISIBLE);
            imageViewJam8.setVisibility(View.VISIBLE);

        }else if(tag.equalsIgnoreCase("10:00")){
            textViewJam10.setVisibility(View.VISIBLE);
            switchJam10.setVisibility(View.VISIBLE);
            imageViewJam10.setVisibility(View.VISIBLE);

        } else if(tag.equalsIgnoreCase("13:00")){
            textViewJam13.setVisibility(View.VISIBLE);
            switchJam13.setVisibility(View.VISIBLE);
            imageViewJam13.setVisibility(View.VISIBLE);

        } else if(tag.equalsIgnoreCase("16:00")){
            textViewJam16.setVisibility(View.VISIBLE);
            switchJam16.setVisibility(View.VISIBLE);
            imageViewJam16.setVisibility(View.VISIBLE);


        } else if(tag.equalsIgnoreCase("20:00")){
            textViewJam20.setVisibility(View.VISIBLE);
            switchJam20.setVisibility(View.VISIBLE);
            imageViewJam20.setVisibility(View.VISIBLE);


        }

    }


    public void showDescJam8(View v){
        showDescription(v, "08:00");
    }

    public void showDescJam10(View v){
        showDescription(v, "10:00");
    }

    public void showDescJam13(View v){
        showDescription(v, "13:00");
    }

    public void showDescJam16(View v){
        showDescription(v, "16:00");
    }

    public void showDescJam20(View v){
        showDescription(v, "20:00");
    }

    public void showDescription(View v, String hour){

            if(hour.equalsIgnoreCase("08:00") && getScheduleDescription("08:00") != null){
                textViewJam8.setVisibility(View.GONE);
                switchJam8.setVisibility(View.GONE);
                imageViewJam8.setVisibility(View.GONE);
                textViewDescJam8.setVisibility(View.VISIBLE);
                textViewDescJam8.setText(getScheduleDescription("08:00"));
            } else  if(hour.equalsIgnoreCase("10:00") && getScheduleDescription("10:00") != null ){
                textViewJam10.setVisibility(View.GONE);
                switchJam10.setVisibility(View.GONE);
                imageViewJam10.setVisibility(View.GONE);
                textViewDescJam10.setVisibility(View.VISIBLE);
                textViewDescJam10.setText(getScheduleDescription("10:00"));
            } else  if(hour.equalsIgnoreCase("13:00") && getScheduleDescription("13:00") != null ){
                textViewJam13.setVisibility(View.GONE);
                switchJam13.setVisibility(View.GONE);
                imageViewJam13.setVisibility(View.GONE);
                textViewDescJam13.setVisibility(View.VISIBLE);
                textViewDescJam13.setText(getScheduleDescription("13:00"));
            } else  if(hour.equalsIgnoreCase("16:00") && getScheduleDescription("16:00") != null ){
                textViewJam16.setVisibility(View.GONE);
                switchJam16.setVisibility(View.GONE);
                imageViewJam16.setVisibility(View.GONE);
                textViewDescJam16.setVisibility(View.VISIBLE);
                textViewDescJam16.setText(getScheduleDescription("16:00"));
            } else  if(hour.equalsIgnoreCase("20:00") && getScheduleDescription("20:00") != null){
                textViewJam20.setVisibility(View.GONE);
                switchJam20.setVisibility(View.GONE);
                imageViewJam20.setVisibility(View.GONE);
                textViewDescJam20.setVisibility(View.VISIBLE);
                textViewDescJam20.setText(getScheduleDescription("20:00"));
            }

    }

    private void hideAllDesc(){
        textViewDescJam8.setVisibility(View.GONE);
        textViewDescJam10.setVisibility(View.GONE);
        textViewDescJam13.setVisibility(View.GONE);
        textViewDescJam16.setVisibility(View.GONE);
        textViewDescJam20.setVisibility(View.GONE);

        imageViewJam20.setVisibility(View.INVISIBLE);
        imageViewJam16.setVisibility(View.INVISIBLE);
        imageViewJam13.setVisibility(View.INVISIBLE);
        imageViewJam10.setVisibility(View.INVISIBLE);
        imageViewJam8.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_specific);

        // stored for future usage
        UserData.setPreference(this);

        switchRuqyahMode = (Switch) findViewById(R.id.switchRuqyahMode);

        progressBarLoading = (ProgressBar) findViewById(R.id.progressBarLoading);

        linearToggleHours = (LinearLayout) findViewById(R.id.linearToggleHours);

        switchJam8 = (Switch) findViewById(R.id.switchJam8);
        switchJam10 = (Switch) findViewById(R.id.switchJam10);
        switchJam13 = (Switch) findViewById(R.id.switchJam13);
        switchJam16 = (Switch) findViewById(R.id.switchJam16);
        switchJam20 = (Switch) findViewById(R.id.switchJam20);

        imageViewJam8 = (ImageView) findViewById(R.id.imageViewJam8);
        imageViewJam10 = (ImageView) findViewById(R.id.imageViewJam10);
        imageViewJam13 = (ImageView) findViewById(R.id.imageViewJam13);
        imageViewJam16 = (ImageView) findViewById(R.id.imageViewJam16);
        imageViewJam20 = (ImageView) findViewById(R.id.imageViewJam20);

        textViewDescJam8 = (TextView) findViewById(R.id.textViewDescJam8);
        textViewDescJam10 = (TextView) findViewById(R.id.textViewDescJam10);
        textViewDescJam13 = (TextView) findViewById(R.id.textViewDescJam13);
        textViewDescJam16 = (TextView) findViewById(R.id.textViewDescJam16);
        textViewDescJam20 = (TextView) findViewById(R.id.textViewDescJam20);

        hideAllDesc();

        imageViewUserProfile = (ImageView) findViewById(R.id.imageViewUserProfile);

        dateComputerFormat = getIntent().getStringExtra(Keys.DATE_CHOSEN);

        // get the status apakah this date already defined in database?
        // if so, then turned the toggle on
        String legend = getIntent().getStringExtra(Keys.LEGEND);

        textViewJam8 = (TextView) findViewById(R.id.textViewJam8);
        textViewJam10 = (TextView) findViewById(R.id.textViewJam10);
        textViewJam13 = (TextView) findViewById(R.id.textViewJam13);
        textViewJam16 = (TextView) findViewById(R.id.textViewJam16);
        textViewJam20 = (TextView) findViewById(R.id.textViewJam20);

        textViewTanggalBulanTahun = (TextView) findViewById(R.id.textViewTanggalBulanTahun);

        // set the gender from here
        updateUserProfile();

        // prepare the date on Indonesian format
        prepareIndonesianTitle();

        //ShowDialog.shortMessage(this, "we got " + legend);
        centerTitleApp();

        // calling the WEB API
        // if not White such as RED / GREEN / ORANGE
        // for editing purposes
        // hide first the toggles
        // let the user wait for the moment

        linearToggleHours.setVisibility(View.GONE);
        progressBarLoading.setVisibility(View.VISIBLE);

        if(!legend.equalsIgnoreCase("white")){
            getDataServer();
            checkRuqyahMode();
        }else{
            // when it's coming from white status
            // means everything needs to be ready 0 status(es)
            createNewEntry();
        }


    }




    private void centerTitleApp(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    int prof;
    private void updateUserProfile(){

       gender = UserData.getPreferenceInt(Keys.USER_GENDER);

        if(gender==Keys.MODE_IKHWAN){
            imageViewUserProfile.setImageResource(R.drawable.ikhwan_logo);
        }else if(gender==Keys.MODE_AKHWAT){
            imageViewUserProfile.setImageResource(R.drawable.akhwat_logo);
        }


    }

    private void creatingDummySchedules(){

        dataSchedule = new Schedule[5];

        for(int x=0; x<5; x++){
            Schedule sched = new Schedule();

            sched.setDate_chosen(dateComputerFormat);
            sched.setStatus(Keys.TOGGLE_OFF);
            sched.setSpecific_hour(timeEntries[x]);
            sched.setGender_therapist(gender);

            dataSchedule[x] = sched;
        }

    }

    private String convertDateFormat(String in, String dateFormatResult, int languageResult) {

        String res = null;

        SimpleDateFormat originalFormat = new SimpleDateFormat("EEEE dd-MMM-yyyy", new Locale("ID"));
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

    private void checkRuqyahMode() {

        WebRequest httpCall = new WebRequest(DateSpecificActivity.this, DateSpecificActivity.this);

        httpCall.addData("date_chosen", dateComputerFormat);
        httpCall.addData("gender_therapist", String.valueOf(gender));

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setDownloadState(false);
        httpCall.setMultipartform(false);

        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.RuqyahCheck);
        httpCall.execute();

    }

    int toggleStat;
    public void toggleRuqyahMode(View v) {

        if(switchRuqyahMode.isChecked()){
            toggleStat = 1;
        }else{
            toggleStat = 0;
        }

        WebRequest httpCall = new WebRequest(DateSpecificActivity.this, DateSpecificActivity.this);

        httpCall.addData("status", String.valueOf(toggleStat));
        httpCall.addData("date_chosen", dateComputerFormat);
        httpCall.addData("gender_therapist", String.valueOf(gender));

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setDownloadState(false);
        httpCall.setMultipartform(false);

        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.RuqyahAdd);
        httpCall.execute();

    }

    String timeEntries [] = {"08:00", "10:00", "13:00", "16:00", "20:00"};
    private void createNewEntry(){

       // ShowDialog.shortMessage(this, "Creating new entries for 5 times in a day.");

        // we will create 5 different time
        // with 0 status(es)

        description = "";

        // this is dummy one for editing purposes
       creatingDummySchedules();

        for(String jamSelected : timeEntries) {

            WebRequest httpCall = new WebRequest(DateSpecificActivity.this, DateSpecificActivity.this);

            httpCall.addData("specific_hour", jamSelected);
            httpCall.addData("status", String.valueOf(Keys.TOGGLE_OFF));
            httpCall.addData("description", description);
            httpCall.addData("gender_therapist", String.valueOf(gender));
            httpCall.addData("date_chosen", dateComputerFormat);

            // we need to wait for the response
            httpCall.setWaitState(true);
            httpCall.setRequestMethod(WebRequest.POST_METHOD);
            httpCall.setTargetURL(URLReference.ScheduleAdd);
            httpCall.execute();

        }

        // now we are also creating the ruqyah schedules for empty one
        WebRequest httpCall = new WebRequest(DateSpecificActivity.this, DateSpecificActivity.this);

        httpCall.addData("status", String.valueOf(Keys.TOGGLE_OFF));
        httpCall.addData("gender_therapist", String.valueOf(gender));
        httpCall.addData("date_chosen", dateComputerFormat);

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.RuqyahAdd);
        httpCall.execute();

    }

    private String prepareIndonesianTitle(){

        String tglPilihanIndo = null;

        SimpleDateFormat formatterParser = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = formatterParser.parse(dateComputerFormat);
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy", new Locale("ID"));
             tglPilihanIndo = formatter.format(date);
        }catch (Exception ex){

        }

        if(tglPilihanIndo!=null)
            textViewTanggalBulanTahun.setText(tglPilihanIndo);

        return tglPilihanIndo;
    }

    public void getDataServer(){
        WebRequest httpCall = new WebRequest(DateSpecificActivity.this,DateSpecificActivity.this);

        httpCall.addData("gender_therapist", String.valueOf(gender));
        httpCall.addData("date_chosen", dateComputerFormat);

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.ScheduleDetail);
        httpCall.execute();

       // ShowDialog.message(this, "Anda posting " + UserProfile.USER_GENDER + " pada " + dateComputerFormat);

    }


    public void chooseProfile(View v){

        Intent i = new Intent(this, UserProfileActivity.class);
        startActivity(i);

        finish();

    }

    public boolean isText(Switch el, String n){
        return el.getText().toString().equalsIgnoreCase(n);
    }

    public boolean isText(Switch el, int res){
        String r = getResources().getString(res);
        return el.getText().toString().equalsIgnoreCase(r);
    }

    public boolean getToggleStatus(Switch el){
        return el.isChecked();
    }

    private void updateKeterangan(String hour, String info){
        int post = 0;
        Schedule x = null;
        for (Schedule s:dataSchedule){
            if(s.getSpecific_hour().equalsIgnoreCase(hour)){
                s.setDescription(info);
                x = s;
                break;
            }

            post++;
        }

        // update the existing ref back
        dataSchedule[post] = x;

    }

    // num is actually index provided from string values (xml)
    public void showKeterangan(int num){
        // jamSelected is using 00:00 format
        // such as 08:00
        final String jamSelected = getResources().getString(num);
        final int numRef = num;

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_detail_date_specific);
        dialog.setTitle(prepareIndonesianTitle());

        dialog.setOnCancelListener(new Dialog.OnCancelListener(){

            @Override
            public void onCancel(DialogInterface dialog) {
                if(jamSelected.equalsIgnoreCase("08:00")){
                    // if it is cancelled
                    switchJam8.setChecked(false);
                } else if(jamSelected.equalsIgnoreCase("10:00")){
                    // if it is cancelled
                    switchJam10.setChecked(false);
                } else if(jamSelected.equalsIgnoreCase("13:00")){
                    // if it is cancelled
                    switchJam13.setChecked(false);
                } else if(jamSelected.equalsIgnoreCase("16:00")){
                    // if it is cancelled
                    switchJam16.setChecked(false);
                } else if(jamSelected.equalsIgnoreCase("20:00")){
                    // if it is cancelled
                    switchJam20.setChecked(false);
                }
            }


        });

        textViewTitleJam = (TextView) dialog.findViewById(R.id.textViewTitleJam);
        textViewTitleJam.setText("Beri Keterangan pada " + jamSelected);

        buttonDetailOK = (Button) dialog.findViewById(R.id.buttonOK);
        buttonDetailOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save first then dismiss the popup
                keteranganEditText = (EditText) dialog.findViewById(R.id.editTextKeterangan);
                String  keterangan = keteranganEditText.getText().toString();
             //   ShowDialog.shortMessage(DateSpecificActivity.this, "diisinya " + keterangan);

                updateDataWebSchedule(numRef, elementToggle, Keys.TOGGLE_ON, keterangan);


                // giving the iconic for specific hour
                if(jamSelected.equalsIgnoreCase("08:00")){
                    //textViewJam8.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit16_icon, 0, 0, 0);
                    imageViewJam8.setVisibility(View.VISIBLE);
                    // update the array first
                    updateKeterangan(jamSelected, keterangan);
                }else if(jamSelected.equalsIgnoreCase("10:00")){
                    imageViewJam10.setVisibility(View.VISIBLE);
                    updateKeterangan(jamSelected, keterangan);
                    //textViewJam10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit16_icon, 0, 0, 0);
                } else if(jamSelected.equalsIgnoreCase("13:00")){
                    imageViewJam13.setVisibility(View.VISIBLE);
                    updateKeterangan(jamSelected, keterangan);
                    //textViewJam13.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit16_icon, 0, 0, 0);
                } else if(jamSelected.equalsIgnoreCase("16:00")){
                    imageViewJam16.setVisibility(View.VISIBLE);
                    updateKeterangan(jamSelected, keterangan);
                    //textViewJam16.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit16_icon, 0, 0, 0);
                } else if(jamSelected.equalsIgnoreCase("20:00")){
                    imageViewJam20.setVisibility(View.VISIBLE);
                    updateKeterangan(jamSelected, keterangan);
                    //textViewJam20.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit16_icon, 0, 0, 0);
                }

                dialog.dismiss();

            }
        });


        dialog.show();


    }


    public void toggleJam20(View v){
        toggleJam(v, R.string.text_hour_20);
    }


    public void toggleJam16(View v){
        toggleJam(v, R.string.text_hour_16);
    }


    public void toggleJam13(View v){
        toggleJam(v, R.string.text_hour_13);
    }


    public void toggleJam10(View v){
        toggleJam(v, R.string.text_hour_10);
    }

    public void toggleJam8(View v){
        toggleJam(v, R.string.text_hour_08);
        }

    public void toggleJam(View v, int res){

        elementToggle = (Switch) v;

        // these are for the ON toggles

        if(getToggleStatus(elementToggle)){
           // showKeterangan(R.string.text_hour_08);
            showKeterangan(res);
        }

        // these are for the OFF toggles
        String jam = getResources().getString(res);

       // if(isText(elementToggle, R.string.text_hour_08) && !getToggleStatus(elementToggle)){
        if( !getToggleStatus(elementToggle)) {
            // updateDataWebSchedule(R.string.text_hour_08, elementToggle, Keys.TOGGLE_OFF);
            updateDataWebSchedule(res, elementToggle, Keys.TOGGLE_OFF);

            // delete locally
            updateKeterangan(jam, null);

            if(jam.equalsIgnoreCase("08:00")){
                // removal the iconic help image
                //textViewJam8.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                imageViewJam8.setVisibility(View.INVISIBLE);

            } else if(jam.equalsIgnoreCase("10:00")){
                // removal the iconic help image
                // textViewJam10.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                imageViewJam10.setVisibility(View.INVISIBLE);

            } else if(jam.equalsIgnoreCase("13:00")){
                // removal the iconic help image
               // textViewJam13.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                imageViewJam13.setVisibility(View.INVISIBLE);

            } else if(jam.equalsIgnoreCase("16:00")){
                // removal the iconic help image
                //textViewJam16.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                imageViewJam16.setVisibility(View.INVISIBLE);

            } else if(jam.equalsIgnoreCase("20:00")){
                // removal the iconic help image
                // textViewJam20.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                imageViewJam20.setVisibility(View.INVISIBLE);

            }

        }


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, CalendarActivity.class);
        startActivity(i);

        this.finish();
    }

    public void updateDataWebSchedule(int num, Switch element, int statusOnOff, String desc){

        if(desc==null){
            description = "";
        }else{
            description = desc;
        }

        // jamSelected is using following format
        // 08:00 for example!
        final String jamSelected = getResources().getString(num);
        String anID = null;

        WebRequest httpCall = new WebRequest(DateSpecificActivity.this, DateSpecificActivity.this);

        httpCall.addData("specific_hour", jamSelected);
        httpCall.addData("status", String.valueOf(statusOnOff));
        httpCall.addData("description", description);
        httpCall.addData("gender_therapist", String.valueOf(gender));
        httpCall.addData("date_chosen", dateComputerFormat);

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);

        // for editing purposes
        if(element!=null){
        if(element.getTag()!=null) {
            anID = element.getTag().toString();
            httpCall.addData("id", anID);
            httpCall.setTargetURL(URLReference.ScheduleUpdate);
        }else{
            httpCall.setTargetURL(URLReference.ScheduleAdd);
        }
        }

        httpCall.execute();

    }

    public void updateDataWebSchedule(int num, Switch element, int statusOnOff){

        updateDataWebSchedule(num, element, statusOnOff, null);

    }

    private void addingDataIntoToggle(Schedule obj){


        if(obj.getSpecific_hour().equalsIgnoreCase("08:00") ){

            // we set the id over here for editing purposes
            if(obj.getId()!=0){
                switchJam8.setTag(obj.getId());

            }

            if(obj.getStatus()==1) {
                switchJam8.setChecked(true);

            }

            if(obj.getDescription().length()>0) {
                textViewDescJam8.setText(obj.getDescription());
                //textViewJam8.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit16_icon, 0, 0, 0);
                imageViewJam8.setVisibility(View.VISIBLE);

            }

        } else if(obj.getSpecific_hour().equalsIgnoreCase("10:00") ){

            // we set the id over here for editing purposes
            if(obj.getId()!=0){
                switchJam10.setTag(obj.getId());

            }

            if(obj.getStatus()==1) {
                switchJam10.setChecked(true);

            }

            if(obj.getDescription().length()>0) {
                textViewDescJam10.setText(obj.getDescription());
               // textViewJam10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit16_icon, 0, 0, 0);
                imageViewJam10.setVisibility(View.VISIBLE);

            }

        } else if(obj.getSpecific_hour().equalsIgnoreCase("13:00") ){

            // we set the id over here for editing purposes
            if(obj.getId()!=0){
                switchJam13.setTag(obj.getId());

            }

            if(obj.getStatus()==1) {
                switchJam13.setChecked(true);

            }

            if(obj.getDescription().length()>0) {
                textViewDescJam13.setText(obj.getDescription());
                //textViewJam13.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit16_icon, 0, 0, 0);
                imageViewJam13.setVisibility(View.VISIBLE);

            }
        } else if(obj.getSpecific_hour().equalsIgnoreCase("16:00") ){

            // we set the id over here for editing purposes
            if(obj.getId()!=0){
                switchJam16.setTag(obj.getId());

            }

            if(obj.getStatus()==1) {
                switchJam16.setChecked(true);

            }

            if(obj.getDescription().length()>0) {
                textViewDescJam16.setText(obj.getDescription());
                imageViewJam16.setVisibility(View.VISIBLE);

//                textViewJam16.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit16_icon, 0, 0, 0);
            }

        } else if(obj.getSpecific_hour().equalsIgnoreCase("20:00") ){

            // we set the id over here for editing purposes
            if(obj.getId()!=0){
                switchJam20.setTag(obj.getId());
            }

            if(obj.getStatus()==1) {
                switchJam20.setChecked(true);

            }

            if(obj.getDescription().length()>0) {
                textViewDescJam20.setText(obj.getDescription());
                imageViewJam20.setVisibility(View.VISIBLE);
                //textViewJam20.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit16_icon, 0, 0, 0);
            }
        }

    }

    @Override
    public void nextActivity() {

    }

    Ruqyah dataRuqyah;

    @Override
    public void onSuccess(String urlTarget, String respond) {

        progressBarLoading.setVisibility(View.GONE);

        try {
          //  ShowDialog.message(this, "Returned  " + respond);

            Gson objectG = new Gson();
            JsonParser parser = new JsonParser();


            if (RespondHelper.isValidRespond(respond)) {

                if (urlTarget.contains(URLReference.RuqyahCheck)) {

                    JSONObject jsons = RespondHelper.getObject(respond, "multi_data");

                    JsonElement mJson =  parser.parse(jsons.toString());
                    dataRuqyah = objectG.fromJson(mJson, Ruqyah.class);

                    // this is for specific date with status 1
                    if(dataRuqyah.getStatus() == 1 && dataRuqyah.getGender_therapist() == gender && dataRuqyah.getDate_chosen().equalsIgnoreCase(dateComputerFormat)) {
                        switchRuqyahMode.setChecked(true);
                    }

                }else if (urlTarget.contains(URLReference.ScheduleDetail)) {

                    JSONArray jsons = RespondHelper.getArray(respond, "multi_data");

                    JsonElement mJson =  parser.parse(jsons.toString());
                    dataSchedule = objectG.fromJson(mJson, Schedule[].class);

                    // this is for specific date & month only

                    for (Schedule single:dataSchedule){
                        addingDataIntoToggle(single);
                    }

                    // then we show the layout because the entries obtained are ready!
                    linearToggleHours.setVisibility(View.VISIBLE);
                    progressBarLoading.setVisibility(View.GONE);

                }else if (urlTarget.contains(URLReference.ScheduleUpdate))  {
                 //   ShowDialog.message(this, "update berhasil!");

                }else if(urlTarget.contains(URLReference.ScheduleAdd)){
                    // for adding new schedule is success value here
                    // we need to receive the returned ID over here
                    // and then assigned it to the toggle on UI
                    JSONObject jsons = RespondHelper.getObject(respond, "multi_data");

                    JsonElement mJson =  parser.parse(jsons.toString());
                    Schedule object  = objectG.fromJson(mJson, Schedule.class);
                    addingDataIntoToggle(object);

                    // then we show the layout because the adding entries are successfully done!
                    linearToggleHours.setVisibility(View.VISIBLE);
                    progressBarLoading.setVisibility(View.GONE);

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