package booking.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import helper.Navigator;
import helper.RespondHelper;
import helper.ShowDialog;
import helper.URLReference;
import helper.UserProfile;
import helper.WebRequest;
import object.Keys;
import object.Schedule;

public class DateSpecificActivity extends AppCompatActivity implements Navigator {

    Switch elementToggle, switchJam8, switchJam10, switchJam13, switchJam16, switchJam20;
    TextView textViewTitleJam, textViewTanggalBulanTahun;
    Button buttonDetailOK;
    EditText keteranganEditText;

    ImageView imageViewUserProfile;

    String dateComputerFormat, description;
    ProgressBar progressBarLoading;

    LinearLayout linearToggleHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_specific);

        progressBarLoading = (ProgressBar) findViewById(R.id.progressBarLoading);

        linearToggleHours = (LinearLayout) findViewById(R.id.linearToggleHours);

        switchJam8 = (Switch) findViewById(R.id.switchJam8);
        switchJam10 = (Switch) findViewById(R.id.switchJam10);
        switchJam13 = (Switch) findViewById(R.id.switchJam13);
        switchJam16 = (Switch) findViewById(R.id.switchJam16);
        switchJam20 = (Switch) findViewById(R.id.switchJam20);

        imageViewUserProfile = (ImageView) findViewById(R.id.imageViewUserProfile);

        dateComputerFormat = getIntent().getStringExtra(Keys.DATE_CHOSEN);

        // get the status apakah this date already defined in database?
        // if so, then turned the toggle on
        String legend = getIntent().getStringExtra(Keys.LEGEND);

        textViewTanggalBulanTahun = (TextView) findViewById(R.id.textViewTanggalBulanTahun);

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
        }else{
            // when it's coming from white status
            // means everything needs to be ready 0 status(es)
            createNewEntry();

        }

        updateUserProfile();

    }

    private void centerTitleApp(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    private void updateUserProfile(){

        int  modeUser = UserProfile.USER_GENDER;

        if(modeUser==Keys.MODE_IKHWAN){
            imageViewUserProfile.setImageResource(R.drawable.ikhwan_logo);
        }else if(modeUser==Keys.MODE_AKHWAT){
            imageViewUserProfile.setImageResource(R.drawable.akhwat_logo);
        }


    }

    private void createNewEntry(){

       // ShowDialog.shortMessage(this, "Creating new entries for 5 times in a day.");

        // we will create 5 different time
        // with 0 status(es)
        String timeEntries [] = {"08:00", "10:00", "13:00", "16:00", "20:00"};
        description = "";

        for(String jamSelected : timeEntries) {

            WebRequest httpCall = new WebRequest(DateSpecificActivity.this, DateSpecificActivity.this);

            httpCall.addData("specific_hour", jamSelected);
            httpCall.addData("status", String.valueOf(Keys.TOGGLE_OFF));
            httpCall.addData("description", description);
            httpCall.addData("gender_therapist", String.valueOf(UserProfile.USER_GENDER));
            httpCall.addData("date_chosen", dateComputerFormat);

            // we need to wait for the response
            httpCall.setWaitState(true);
            httpCall.setRequestMethod(WebRequest.POST_METHOD);
            httpCall.setTargetURL(URLReference.ScheduleAdd);
            httpCall.execute();

        }

    }

    private String prepareIndonesianTitle(){

        String tglPilihanIndo = null;

        SimpleDateFormat formatterParser = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = formatterParser.parse(dateComputerFormat);
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", new Locale("ID"));
             tglPilihanIndo = formatter.format(date);
        }catch (Exception ex){

        }

        if(tglPilihanIndo!=null)
            textViewTanggalBulanTahun.setText(tglPilihanIndo);

        return tglPilihanIndo;
    }

    public void getDataServer(){
        WebRequest httpCall = new WebRequest(DateSpecificActivity.this,DateSpecificActivity.this);

        httpCall.addData("gender_therapist", String.valueOf(UserProfile.USER_GENDER));
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

    // num is actually index provided from string values (xml)
    public boolean isText(Switch el, int num){
        String comp = getResources().getString(num);
        return isText(el, comp);
    }

    public boolean isText(Switch el, String n){
        return el.getText().toString().equalsIgnoreCase(n);
    }

    public boolean getToggleStatus(Switch el){
        return el.isChecked();
    }

    // num is actually index provided from string values (xml)
    private void showKeterangan(int num){
        // jamSelected is using 00:00 format
        // such as 08:00
        final String jamSelected = getResources().getString(num);
        final int numRef = num;

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_detail_date_specific);
        dialog.setTitle(prepareIndonesianTitle());

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
                dialog.dismiss();
            }
        });


        dialog.show();


    }

    public void toggleJam(View v){

        elementToggle = (Switch) v;

        // these are for the ON toggles

        if(isText(elementToggle, R.string.text_hour_08) && getToggleStatus(elementToggle)){
            showKeterangan(R.string.text_hour_08);
        } else if(isText(elementToggle, R.string.text_hour_10) && getToggleStatus(elementToggle)){
            showKeterangan(R.string.text_hour_10);
        } else if(isText(elementToggle, R.string.text_hour_13) && getToggleStatus(elementToggle)){
            showKeterangan(R.string.text_hour_13);
        } else if(isText(elementToggle, R.string.text_hour_16) && getToggleStatus(elementToggle)){
            showKeterangan(R.string.text_hour_16);
        } else if(isText(elementToggle, R.string.text_hour_20) && getToggleStatus(elementToggle)){
            showKeterangan(R.string.text_hour_20);
        }

        // these are for the OFF toggles

        if(isText(elementToggle, R.string.text_hour_08) && !getToggleStatus(elementToggle)){
            updateDataWebSchedule(R.string.text_hour_08, elementToggle, Keys.TOGGLE_OFF);
        } else if(isText(elementToggle, R.string.text_hour_10) && !getToggleStatus(elementToggle)){
            updateDataWebSchedule(R.string.text_hour_10, elementToggle, Keys.TOGGLE_OFF);
        } else if(isText(elementToggle, R.string.text_hour_13) && !getToggleStatus(elementToggle)){
            updateDataWebSchedule(R.string.text_hour_13, elementToggle, Keys.TOGGLE_OFF);
        } else if(isText(elementToggle, R.string.text_hour_16) && !getToggleStatus(elementToggle)){
            updateDataWebSchedule(R.string.text_hour_16, elementToggle, Keys.TOGGLE_OFF);
        } else if(isText(elementToggle, R.string.text_hour_20) && !getToggleStatus(elementToggle)){
            updateDataWebSchedule(R.string.text_hour_20, elementToggle, Keys.TOGGLE_OFF);
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, CalendarActivity.class);
        startActivity(i);

        this.finish();
    }

    private void updateDataWebSchedule(int num, Switch element, int statusOnOff, String desc){

        if(desc==null){
            description = "";
        }else{
            description = desc;
        }

        // jamSelected is using following format
        // 08:00 for example!
        final String jamSelected = getResources().getString(num);
        String anID = null;
        // for editing purposes
        if(element.getTag()!=null){
            anID = element.getTag().toString();
        }

        WebRequest httpCall = new WebRequest(DateSpecificActivity.this, DateSpecificActivity.this);

        httpCall.addData("specific_hour", jamSelected);
        httpCall.addData("status", String.valueOf(statusOnOff));
        httpCall.addData("description", description);
        httpCall.addData("gender_therapist", String.valueOf(UserProfile.USER_GENDER));
        httpCall.addData("date_chosen", dateComputerFormat);

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);

        // for editing purposes
        if(element.getTag()!=null) {
            httpCall.addData("id", anID);
            httpCall.setTargetURL(URLReference.ScheduleUpdate);
        }else{
            httpCall.setTargetURL(URLReference.ScheduleAdd);
        }

        httpCall.execute();

    }

    private void updateDataWebSchedule(int num, Switch element, int statusOnOff){

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

        } else if(obj.getSpecific_hour().equalsIgnoreCase("10:00") ){

            // we set the id over here for editing purposes
            if(obj.getId()!=0){
                switchJam10.setTag(obj.getId());
            }

            if(obj.getStatus()==1) {
                switchJam10.setChecked(true);
            }

        } else if(obj.getSpecific_hour().equalsIgnoreCase("13:00") ){

            // we set the id over here for editing purposes
            if(obj.getId()!=0){
                switchJam13.setTag(obj.getId());
            }

            if(obj.getStatus()==1) {
                switchJam13.setChecked(true);
            }

        } else if(obj.getSpecific_hour().equalsIgnoreCase("16:00") ){

            // we set the id over here for editing purposes
            if(obj.getId()!=0){
                switchJam16.setTag(obj.getId());
            }

            if(obj.getStatus()==1) {
                switchJam16.setChecked(true);
            }

        } else if(obj.getSpecific_hour().equalsIgnoreCase("20:00") ){

            // we set the id over here for editing purposes
            if(obj.getId()!=0){
                switchJam20.setTag(obj.getId());
            }

            if(obj.getStatus()==1) {
                switchJam20.setChecked(true);
            }

        }

    }



    @Override
    public void nextActivity() {

    }

    @Override
    public void onSuccess(String urlTarget, String respond) {

        progressBarLoading.setVisibility(View.GONE);

        try {
          //  ShowDialog.message(this, "Returned  " + respond);

            Gson objectG = new Gson();
            JsonParser parser = new JsonParser();


            if (RespondHelper.isValidRespond(respond)) {

                if (urlTarget.contains(URLReference.ScheduleDetail)) {

                    JSONArray jsons = RespondHelper.getArray(respond, "multi_data");

                    JsonElement mJson =  parser.parse(jsons.toString());
                    Schedule object [] = objectG.fromJson(mJson, Schedule[].class);

                    // this is for specific date & month only

                    for (Schedule single:object){
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