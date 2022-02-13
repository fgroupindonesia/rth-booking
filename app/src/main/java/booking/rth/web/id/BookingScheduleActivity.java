package booking.rth.web.id;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import helper.Navigator;
import helper.RespondHelper;
import helper.ShowDialog;
import helper.URLReference;
import helper.UserProfile;
import helper.WebRequest;
import helper.WhatsappSender;
import object.Keys;
import object.Schedule;

public class BookingScheduleActivity extends AppCompatActivity implements Navigator {

    TextView textViewDatePicked, textViewKet08, textViewKet10,
            textViewKet13, textViewKet16, textViewKet20,
            textViewHour08, textViewHour10,
            textViewHour13, textViewHour16, textViewHour20;

    String dateChosen, NO_RTH = "+6285871341474";
    int gender_therapist;
    // 1 : male
    // 2 : female


    ImageView imageViewUserProfile;
    WhatsappSender waSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_schedule);

        waSender = new WhatsappSender(this);

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

        imageViewUserProfile = (ImageView) findViewById(R.id.imageViewUserProfile);

        updateText();
        updateUserProfile();
        callWeb();

    }

    private void updateText(){

        Bundle extras = getIntent().getExtras();
        String dateText;

        if (extras != null) {
            dateText = extras.getString(Keys.DATE_CHOSEN);
            dateChosen = dateText;

            textViewDatePicked.setText(dateChosen);
        }

    }

    private String convertDateFormat(String in)  {

        String res = null;

        SimpleDateFormat originalFormat =  new SimpleDateFormat("EEEE dd-MMM-yyyy", new Locale("ID"));
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = originalFormat.parse(in);
            res = targetFormat.format(date);
        } catch(Exception ex){

        }

        return res;

    }

    private void callWeb(){

        String dateServerFormat = convertDateFormat(dateChosen);

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

    public void updateDatePicked(){

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

    private void setNormal(TextView txt){
        txt.setTypeface(null, Typeface.NORMAL);
    }

    private void addingDataRow(Schedule obj){

        if(obj.getSpecific_hour().equalsIgnoreCase("08:00")){
            if(obj.getStatus()==1) {
                setAvailable(textViewKet08);
                setBold(textViewHour08);
            }

        } else if(obj.getSpecific_hour().equalsIgnoreCase("10:00")){
            if(obj.getStatus()==1) {
                setAvailable(textViewKet10);
                setBold(textViewHour10);
            }
        } else if(obj.getSpecific_hour().equalsIgnoreCase("13:00")){
            if(obj.getStatus()==1) {
                setAvailable(textViewKet13);
                setBold(textViewHour13);
            }
        } else if(obj.getSpecific_hour().equalsIgnoreCase("16:00")){
            if(obj.getStatus()==1) {
                setAvailable(textViewKet16);
                setBold(textViewHour16);
            }
        } else if(obj.getSpecific_hour().equalsIgnoreCase("20:00")){
            if(obj.getStatus()==1) {
                setAvailable(textViewKet20);
                setBold(textViewHour20);
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

            Gson objectG = new Gson();

            if (RespondHelper.isValidRespond(respond)) {

                if (urlTarget.contains(URLReference.ScheduleDetail)) {

                    JSONArray jsons = RespondHelper.getArray(respond, "multi_data");

                    JsonParser parser = new JsonParser();
                    JsonElement mJson =  parser.parse(jsons.toString());
                    Gson gson = new Gson();
                    Schedule object [] = gson.fromJson(mJson, Schedule[].class);

                    for (Schedule single:object){
                        addingDataRow(single);
                    }

                }

            } else if (!RespondHelper.isValidRespond(respond)) {

                ShowDialog.message(this, "tidak ada jadwal yg kosong!");
                //finish();

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
    String res = "Bismillah,\nSaya ingin *Booking Jadwal*\n\n*"+
            dateChosen + "* Pada *Jam " + hour + "*\n\n"+
            getBookingCode() +
            lowerMessage +   " apakah bisa?";

      res =  res.replaceAll("\n", "%0a");

    return res;
    }

    private String lowerMessage =  "\nuntuk Therapy: ...\n"+
            "Nama Lengkap Saya: ...\n"+
            "Kota: ...\n\n";

    private boolean isAvailable(TextView v){

        // if it has a word of 'tidak'
        if(v.getText().toString().toLowerCase().contains("tidak")){
            return false;
        }

        return true;
    }

    public void openChatJam08(View v){
        if(isAvailable((TextView) v) ){
            waSender.sendMessageToWhatsAppContact(NO_RTH, createText("08:00"));
        }else{
            ShowDialog.message(this, "Maaf jam 08:00 tidak tersedia!");
        }

        //ShowDialog.message(this, "Error saat jam 08!");
    }


    public void openChatJam10(View v){
        if(isAvailable((TextView) v) ){
            waSender.sendMessageToWhatsAppContact(NO_RTH, createText("10:00"));
        }else{
            ShowDialog.message(this, "Maaf jam 10:00 tidak tersedia!");
        }
    }

    public void openChatJam13(View v){
        if(isAvailable((TextView) v) ){
            waSender.sendMessageToWhatsAppContact(NO_RTH, createText("13:00"));
        }else{
            ShowDialog.message(this, "Maaf jam 13:00 tidak tersedia!");
        }
    }

    public void openChatJam16(View v){
        if(isAvailable((TextView) v) ){
            waSender.sendMessageToWhatsAppContact(NO_RTH, createText("16:00"));
        }else{
            ShowDialog.message(this, "Maaf jam 16:00 tidak tersedia!");
        }
    }

    public void openChatJam20(View v){
        if(isAvailable((TextView) v) ){
            waSender.sendMessageToWhatsAppContact(NO_RTH, createText("20:00"));
        }else{
            ShowDialog.message(this, "Maaf jam 20:00 tidak tersedia!");
        }
    }

    public void chooseProfile(View v){

        Intent i = new Intent(this, ClientProfileActivity.class);
        startActivity(i);

        finish();

    }

}