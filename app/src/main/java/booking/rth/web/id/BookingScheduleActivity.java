package booking.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import helper.Navigator;
import helper.RespondHelper;
import helper.ShowDialog;
import helper.URLMaker;
import helper.URLReference;
import helper.UserProfile;
import helper.WebRequest;
import helper.WhatsappSender;
import object.Hijri;
import object.Keys;
import object.Month;
import object.Schedule;
import object.Weekday;
import shared.UserData;

public class BookingScheduleActivity extends AppCompatActivity implements Navigator {

    TextView textViewDatePicked, textViewKet08, textViewKet10,
            textViewKet13, textViewKet16, textViewKet20,
            textViewHour08, textViewHour10,
            textViewHour13, textViewHour16, textViewHour20,
            textViewDateIslamic;

    Button buttonBookingJam08, buttonBookingJam10, buttonBookingJam13,
            buttonBookingJam16, buttonBookingJam20;

    String dateChosen, NO_RTH = "+6285871341474",
            usName, hourSelected, wa;

    int gender_therapist;
    // 1 : male
    // 2 : female

    ProgressBar progressBarLoading;

    ImageView imageViewUserProfile;
    WhatsappSender waSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_schedule);

        // for storing later usage
        UserData.setPreference(this);

        usName = UserData.getPreferenceString(Keys.USERNAME);
        wa = UserData.getPreferenceString(Keys.WHATSAPP);

        waSender = new WhatsappSender(this);

        textViewDateIslamic = (TextView) findViewById(R.id.textViewDateIslamic);

        progressBarLoading = (ProgressBar) findViewById(R.id.progressBarLoading);

        textViewDatePicked = (TextView) findViewById(R.id.textViewDatePicked);
        textViewKet08 = (TextView) findViewById(R.id.textViewKet08);
        textViewKet10 = (TextView) findViewById(R.id.textViewKet10);
        textViewKet13 = (TextView) findViewById(R.id.textViewKet13);
        textViewKet16 = (TextView) findViewById(R.id.textViewKet16);
        textViewKet20 = (TextView) findViewById(R.id.textViewKet20);

        textViewHour08 = (TextView) findViewById(R.id.textViewHour08);
        textViewHour10 = (TextView) findViewById(R.id.textViewHour10);
        textViewHour13 = (TextView) findViewById(R.id.textViewHour13);
        textViewHour16 = (TextView) findViewById(R.id.textViewHour16);
        textViewHour20 = (TextView) findViewById(R.id.textViewHour20);

        buttonBookingJam08 = (Button) findViewById(R.id.buttonBookingJam08);
        buttonBookingJam10 = (Button) findViewById(R.id.buttonBookingJam10);
        buttonBookingJam13 = (Button) findViewById(R.id.buttonBookingJam13);
        buttonBookingJam16 = (Button) findViewById(R.id.buttonBookingJam16);
        buttonBookingJam20 = (Button) findViewById(R.id.buttonBookingJam20);

        imageViewUserProfile = (ImageView) findViewById(R.id.imageViewUserProfile);

        centerTitleApp();
        updateText();

        // updating dateChosen here
        updateUserProfile();
        callWeb();

        // request to server another API REST CALL
        // using dateChosen as well
        getIslamicDate();

        if(UserData.getPreferenceBoolean(Keys.USER_REGISTER_STATUS)){
            // if true then this is registered user

            setButtonToWhatsapp(buttonBookingJam08);
            setButtonToWhatsapp(buttonBookingJam10);
            setButtonToWhatsapp(buttonBookingJam13);
            setButtonToWhatsapp(buttonBookingJam16);
            setButtonToWhatsapp(buttonBookingJam20);
        }else {
            // if not true then this is a new user
            setButtonToLink(buttonBookingJam08);
            setButtonToLink(buttonBookingJam10);
            setButtonToLink(buttonBookingJam13);
            setButtonToLink(buttonBookingJam16);
            setButtonToLink(buttonBookingJam20);

        }

    }

    private void setButtonToLink(Button btn){
        btn.setBackgroundResource(R.drawable.file_info);
        btn.setTag("link");
    }

    private void setButtonToWhatsapp(Button btn){
        btn.setBackgroundResource(R.drawable.whatsapp24);
        btn.setTag("whatsapp");
    }

    private void getIslamicDate(){

        WebRequest httpCall = new WebRequest(BookingScheduleActivity.this,BookingScheduleActivity.this);

        String dataIndo =   convertDateFormat(dateChosen, "dd-MM-yyyy");

        httpCall.addData("date", dataIndo);
        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setDownloadState(false);
        httpCall.setMultipartform(false);

        httpCall.setRequestMethod(WebRequest.GET_METHOD);
        httpCall.setTargetURL(URLReference.AdhanWebsite);
        httpCall.execute();

    }

    private void centerTitleApp(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    // updating dateChosen here
    private void updateText(){

        Bundle extras = getIntent().getExtras();
        String dateText;

        if (extras != null) {
            dateText = extras.getString(Keys.DATE_CHOSEN);
            dateChosen = dateText;

            // the format used is
            // EEEE dd-MMM-yyyy

            textViewDatePicked.setText(dateChosen);
        }

    }

    private String convertDateFormat(String in, String dateFormat)  {

        String res = null;

        SimpleDateFormat originalFormat =  new SimpleDateFormat("EEEE dd-MMM-yyyy", new Locale("ID"));
        DateFormat targetFormat = new SimpleDateFormat(dateFormat);
        try {
            Date date = originalFormat.parse(in);
            res = targetFormat.format(date);
        } catch(Exception ex){

        }

        return res;

    }

    private void callWeb(){

        String dateServerFormat = convertDateFormat(dateChosen, "yyyy-MM-dd");

        //ShowDialog.message(this, "sending " + dateServerFormat);
        //ShowDialog.message(this, "sending " + String.valueOf(UserProfile.USER_GENDER));

        WebRequest httpCall = new WebRequest(BookingScheduleActivity.this,BookingScheduleActivity.this);
        httpCall.addData("date_chosen", dateServerFormat);
        httpCall.addData("gender_therapist", String.valueOf(UserProfile.USER_GENDER));

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.ScheduleDetail);
        httpCall.execute();

        // call web API based on selected date only
        // and then after returned result go updating every responds

    }

    private void setAvailable(TextView txt){
        // setting the drawableRight image
        txt.setText(R.string.text_tersedia_small);
        txt.setTextColor(getResources().getColor(R.color.colorGreen));
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.note16, 0);
    }

    private void setBold(TextView txt){
        txt.setTypeface(null, Typeface.BOLD);
    }

    private void addingDataRow(Schedule obj){

        if(obj.getSpecific_hour().equalsIgnoreCase("08:00")){
            if(obj.getStatus()!=1) {
                setAvailable(textViewKet08);
                setBold(textViewHour08);
                buttonBookingJam08.setVisibility(View.VISIBLE);
            }

        } else if(obj.getSpecific_hour().equalsIgnoreCase("10:00")){
            if(obj.getStatus()!=1) {
                setAvailable(textViewKet10);
                setBold(textViewHour10);
                buttonBookingJam10.setVisibility(View.VISIBLE);
            }
        } else if(obj.getSpecific_hour().equalsIgnoreCase("13:00")){
            if(obj.getStatus()!=1) {
                setAvailable(textViewKet13);
                setBold(textViewHour13);
                buttonBookingJam13.setVisibility(View.VISIBLE);
            }
        } else if(obj.getSpecific_hour().equalsIgnoreCase("16:00")){
            if(obj.getStatus()!=1) {
                setAvailable(textViewKet16);
                setBold(textViewHour16);
                buttonBookingJam16.setVisibility(View.VISIBLE);
            }
        } else if(obj.getSpecific_hour().equalsIgnoreCase("20:00")){
            if(obj.getStatus()!=1) {
                setAvailable(textViewKet20);
                setBold(textViewHour20);
                buttonBookingJam20.setVisibility(View.VISIBLE);
            }
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

    @Override
    public void nextActivity() {

    }

    @Override
    public void onSuccess(String urlTarget, String respond) {

        try {
            //ShowDialog.message(this, "dapt " + respond);

            Gson gson = new Gson();

            if (RespondHelper.isValidRespond(respond)) {

                if (urlTarget.contains(URLReference.ScheduleDetail)) {

                    JSONArray jsons = RespondHelper.getArray(respond, "multi_data");

                    JsonParser parser = new JsonParser();
                    JsonElement mJson =  parser.parse(jsons.toString());

                    Schedule object [] = gson.fromJson(mJson, Schedule[].class);

                    for (Schedule single:object){
                        addingDataRow(single);
                    }

                    progressBarLoading.setVisibility(View.INVISIBLE);

                } else if(urlTarget.contains(URLReference.AdhanWebsite)){

                    //ShowDialog.message(this, respond);

                    JSONObject jsons = RespondHelper.getObject(respond, "data");

                    Hijri dataHijriyyah  = gson.fromJson(jsons.getJSONObject("hijri").toString(), Hijri.class);

                    Weekday dataHijriyyahWeek = gson.fromJson(jsons.getJSONObject("hijri").getJSONObject("weekday").toString(), Weekday.class);
                    Month dataHijriyyahMonth = gson.fromJson(jsons.getJSONObject("hijri").getJSONObject("month").toString(), Month.class);

                    // ShowDialog.message(this, dataHijriyyahWeek.getEn());

                    textViewDateIslamic.setText(dataHijriyyahWeek.getEn() + " "
                            + dataHijriyyah.getDay() + " "
                            + dataHijriyyahMonth.getEn() + " "
                            + dataHijriyyah.getYear());

                }

            } else if (!RespondHelper.isValidRespond(respond)) {

                ShowDialog.message(this, "tidak ada jadwal yg kosong!");
                progressBarLoading.setVisibility(View.INVISIBLE);

            }
        } catch (Exception ex) {
            ShowDialog.message(this, "Error " + ex.getMessage());
            ex.printStackTrace();
        }


    }

    public String getBookingCode(){

        String aToZ="ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // 36 letter.
        String randomStr=generateRandom(aToZ);
        String s = "Kode Booking: *"+randomStr + "*\n";
        return s;

    }

    private static String generateRandom(String aToZ) {
        Random rand=new Random();
        StringBuilder res=new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randIndex=rand.nextInt(aToZ.length());
            if(i==4){
                res.append("-");
            }
            res.append(aToZ.charAt(randIndex));
        }
        return res.toString();
    }

    public String createText(String hour){
        String kelamin;

        if(UserProfile.USER_GENDER==Keys.MODE_IKHWAN){
            kelamin = "pria";
        }else {
            kelamin = "wanita";
        }

    String res = "Bismillah,\nSaya *"+kelamin+ "* ingin *Booking Jadwal*\n\n*"+
            dateChosen + "* Pada *Jam " + hour + "*\n\n"+
            getBookingCode() +
            lowerMessage + " apakah bisa?";

      res =  res.replaceAll("\n", "%0a");

    return res;
    }

    private String lowerMessage =  "\nuntuk *Therapy*\n"+
            "Nama Lengkap Saya: *" + usName +"*\n"+
            "\n\n";

    private boolean isAvailable(TextView v){

        // if it has a word of 'tidak'
        if(v.getText().toString().toLowerCase().contains("tidak")){
            return false;
        }

        return true;
    }

    public void openLink(String hourSelected){

        URLMaker umaker = new URLMaker();
        umaker.setMainURL(URLReference.RegistrationPage);
        umaker.addParameterValue(Keys.HOUR_SELECTED, hourSelected);
        // date is using dd-MM-yyyy format
        umaker.addParameterValue(Keys.DATE_CHOSEN, dateChosen);
        // name is using html parameter encoder
        umaker.addParameterValue(Keys.USERNAME, usName);
        umaker.addParameterValue(Keys.WHATSAPP, wa);

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(umaker.getCompleteURL())));

    }

    public void openChatJam08(View v){

        visitLinkOrWhatsapp(buttonBookingJam08, textViewKet08, "08:00");

        //ShowDialog.message(this, "Error saat jam 08!");
    }

    public void visitLinkOrWhatsapp(Button btn, TextView txt, String hourSelected){
        if(btn.getTag().toString().equalsIgnoreCase("link")){
            // buka ke pendaftaran
            openLink(hourSelected);
        } else {

            // buka whatsapp
            if(isAvailable(txt) ){
                waSender.sendMessageToWhatsAppContact(NO_RTH, createText(hourSelected));
            }else{
                ShowDialog.message(this, "Maaf jam "+ hourSelected +" tidak tersedia!");
            }


        }
    }

    public void openChatJam10(View v){

        visitLinkOrWhatsapp(buttonBookingJam10, textViewKet10, "10:00");
    }

    public void openChatJam13(View v){
        visitLinkOrWhatsapp(buttonBookingJam13, textViewKet13, "13:00");
    }

    public void openChatJam16(View v){

        visitLinkOrWhatsapp(buttonBookingJam16, textViewKet16, "16:00");

    }

    public void openChatJam20(View v){

        visitLinkOrWhatsapp(buttonBookingJam20, textViewKet20, "20:00");

    }

    public void chooseProfile(View v){

        Intent i = new Intent(this, ClientProfileActivity.class);
        startActivity(i);

        finish();

    }

}