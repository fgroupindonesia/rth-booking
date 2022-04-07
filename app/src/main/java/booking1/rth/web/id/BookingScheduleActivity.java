package booking1.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import helper.Navigator;
import helper.OrderHelper;
import helper.RespondHelper;
import helper.ShowDialog;
import helper.URLMaker;
import helper.URLReference;
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
            textViewDateIslamic, textViewPetunjuk,
            textViewEstimasiBiaya;

    Button buttonBookingJam08, buttonBookingJam10, buttonBookingJam13,
            buttonBookingJam16, buttonBookingJam20;

    String dateChosen, NO_RTH = "+6285871341474",
            usName, hourSelected, wa;

    LinearLayout linearTreatmentPick, linearBooking;

    int gender;
    // 1 : male
    // 2 : female

    int profesi;
    // 1 : pelajar
    // 2 : umum
    // -1 : none

    ProgressBar progressBarLoading;

    ImageView imageViewUserProfile, imageViewProfesi;
    WhatsappSender waSender;

    View bottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_schedule);

        // for storing later usage
        UserData.setPreference(this);

        usName = UserData.getPreferenceString(Keys.USERNAME);
        wa = UserData.getPreferenceString(Keys.WHATSAPP);
        profesi = UserData.getPreferenceInt(Keys.USER_PROFESSION);

        // to know the payment details later
        help = new OrderHelper(profesi);
        //ShowDialog.message(this, "profesinya " + profesi);

        waSender = new WhatsappSender(this);

        // this part is only helper for scrolling down
        scrollViewBooking = (ScrollView) findViewById(R.id.scrollViewBooking);
        bottomView = (View) findViewById(R.id.bottomView);

        buttonTreatmentOK = (Button) findViewById(R.id.buttonTreatmentOK);
        linearTreatmentPick = (LinearLayout) findViewById(R.id.linearTreatmentPick);
        linearBooking = (LinearLayout) findViewById(R.id.linearBookingTable);

        textViewEstimasiBiaya = (TextView) findViewById(R.id.textViewEstimasiBiaya);
        textViewDateIslamic = (TextView) findViewById(R.id.textViewDateIslamic);
        textViewPetunjuk = (TextView) findViewById(R.id.textViewPetunjuk);
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

        checkboxFashdu = (CheckBox) findViewById(R.id.checkBoxFashdu);
        checkboxHijamah = (CheckBox) findViewById(R.id.checkBoxHijamah);
        checkboxPijatSyaraf = (CheckBox) findViewById(R.id.checkBoxPijat);
        checkboxGurah = (CheckBox) findViewById(R.id.checkBoxGurah);
        checkboxNebu = (CheckBox) findViewById(R.id.checkBoxNebu);
        checkboxElektrik = (CheckBox) findViewById(R.id.checkBoxElektrik);
        checkboxMoxa = (CheckBox) findViewById(R.id.checkBoxMoxa);
        checkboxLintah = (CheckBox) findViewById(R.id.checkBoxLintah);
        checkboxRuqyah = (CheckBox) findViewById(R.id.checkBoxRuqyah);
        checkboxUmum = (CheckBox) findViewById(R.id.checkBoxUmum);

        imageViewUserProfile = (ImageView) findViewById(R.id.imageViewUserProfile);
        imageViewProfesi = (ImageView) findViewById(R.id.imageViewProfesi);

        // updating gender here
        updateUserProfile();

        centerTitleApp();

        // getting dateChosen here
        updateText();

        callWeb();

        // request to server another API REST CALL
        // using dateChosen as well
        getIslamicDate();

        if (UserData.getPreferenceBoolean(Keys.USER_REGISTER_STATUS)) {
            // if true then this is registered user

            setButtonToWhatsapp(buttonBookingJam08);
            setButtonToWhatsapp(buttonBookingJam10);
            setButtonToWhatsapp(buttonBookingJam13);
            setButtonToWhatsapp(buttonBookingJam16);
            setButtonToWhatsapp(buttonBookingJam20);
        } else {
            // if not true then this is a new user
            setButtonToLink(buttonBookingJam08);
            setButtonToLink(buttonBookingJam10);
            setButtonToLink(buttonBookingJam13);
            setButtonToLink(buttonBookingJam16);
            setButtonToLink(buttonBookingJam20);

        }

        showLoading(true);
        // if this person is a new member
        if(!UserData.getPreferenceBoolean(Keys.USER_REGISTER_STATUS)){
            // directly show the timetable (jadwal)
            // and choose the treatment on website form
            showBookingTable(true);
        }

    }

    private void showLoading(boolean b) {
        if (b) {
            progressBarLoading.setVisibility(View.VISIBLE);
            textViewPetunjuk.setVisibility(View.GONE);
        } else {
            progressBarLoading.setVisibility(View.GONE);
            textViewPetunjuk.setVisibility(View.VISIBLE);
        }

    }

    private void setButtonToLink(Button btn) {
        btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.file_info, 0, 0, 0);
        //btn.setBackgroundResource(R.drawable.file_info);
        btn.setTag("link");
    }

    private void setButtonToWhatsapp(Button btn) {
        btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.whatsapp24, 0, 0, 0);
        //btn.setBackgroundResource(R.drawable.file_info);
        btn.setTag("whatsapp");
    }

    private void getIslamicDate() {

        WebRequest httpCall = new WebRequest(BookingScheduleActivity.this, BookingScheduleActivity.this);

        String dataIndo = convertDateFormat(dateChosen, "dd-MM-yyyy", Keys.LANGUAGE_EN);

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

    // getting dateChosen here
    private void updateText() {

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

    private String convertDateFormat(String in, String dateFormat, int language) {

        String res = null;

        SimpleDateFormat originalFormat = new SimpleDateFormat("EEEE dd-MMM-yyyy", new Locale("ID"));
        DateFormat targetFormat = null;

        if (language == Keys.LANGUAGE_EN) {
            targetFormat = new SimpleDateFormat(dateFormat);
        } else {
            targetFormat = new SimpleDateFormat(dateFormat, new Locale("ID"));
        }

        try {
            Date date = originalFormat.parse(in);
            res = targetFormat.format(date);
        } catch (Exception ex) {

        }

        return res;

    }

    private void callWeb() {

        String dateServerFormat = convertDateFormat(dateChosen, "yyyy-MM-dd", Keys.LANGUAGE_EN);

        //ShowDialog.message(this, "sending " + dateServerFormat);
        //ShowDialog.message(this, "sending " + String.valueOf(UserProfile.USER_GENDER));

        WebRequest httpCall = new WebRequest(BookingScheduleActivity.this, BookingScheduleActivity.this);
        httpCall.addData("date_chosen", dateServerFormat);
        httpCall.addData("gender_therapist", String.valueOf(gender));

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.ScheduleDetail);
        httpCall.execute();

        // call web API based on selected date only
        // and then after returned result go updating every responds

    }

    private void setAvailable(TextView txt) {
        // setting the drawableRight image
        txt.setText(R.string.text_tersedia_small);
        txt.setTextColor(getResources().getColor(R.color.colorGreen));
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.note16, 0);
    }

    private void setBold(TextView txt) {
        txt.setTypeface(null, Typeface.BOLD);
    }

    private void addingDataRow(Schedule obj) {

        if (obj.getSpecific_hour().equalsIgnoreCase("08:00")) {
            if (obj.getStatus() != 1) {
                setAvailable(textViewKet08);
                setBold(textViewHour08);
                buttonBookingJam08.setVisibility(View.VISIBLE);
            }

        } else if (obj.getSpecific_hour().equalsIgnoreCase("10:00")) {
            if (obj.getStatus() != 1) {
                setAvailable(textViewKet10);
                setBold(textViewHour10);
                buttonBookingJam10.setVisibility(View.VISIBLE);
            }
        } else if (obj.getSpecific_hour().equalsIgnoreCase("13:00")) {
            if (obj.getStatus() != 1) {
                setAvailable(textViewKet13);
                setBold(textViewHour13);
                buttonBookingJam13.setVisibility(View.VISIBLE);
            }
        } else if (obj.getSpecific_hour().equalsIgnoreCase("16:00")) {
            if (obj.getStatus() != 1) {
                setAvailable(textViewKet16);
                setBold(textViewHour16);
                buttonBookingJam16.setVisibility(View.VISIBLE);
            }
        } else if (obj.getSpecific_hour().equalsIgnoreCase("20:00")) {
            if (obj.getStatus() != 1) {
                setAvailable(textViewKet20);
                setBold(textViewHour20);
                buttonBookingJam20.setVisibility(View.VISIBLE);
            }
        }

    }

    CheckBox checkboxHijamah, checkboxGurah, checkboxMoxa,
            checkboxNebu, checkboxRuqyah, checkboxUmum,
            checkboxElektrik, checkboxPijatSyaraf,
            checkboxFashdu, checkboxLintah;

    private void clearAllCheckboxes(boolean b){

        checkboxHijamah.setEnabled(!b);
        checkboxGurah.setEnabled(!b);
        checkboxNebu.setEnabled(!b);
        checkboxRuqyah.setEnabled(!b);
        checkboxElektrik.setEnabled(!b);
        checkboxPijatSyaraf.setEnabled(!b);
        checkboxLintah.setEnabled(!b);
        checkboxMoxa.setEnabled(!b);
        checkboxFashdu.setEnabled(!b);

        checkboxHijamah.setChecked(false);
        checkboxGurah.setChecked(false);
        checkboxNebu.setChecked(false);
        checkboxRuqyah.setChecked(false);
        checkboxElektrik.setChecked(false);
        checkboxPijatSyaraf.setChecked(false);
        checkboxLintah.setChecked(false);
        checkboxMoxa.setChecked(false);
        checkboxFashdu.setChecked(false);

    }

    OrderHelper help;

    public void resetTreatment(View v){
        help.resetItem();
        if(((CheckBox) v).isChecked()) {
            help.addItem("umum");

            clearAllCheckboxes(true);
        } else {
            help.resetItem();

            clearAllCheckboxes(false);
        }


        renderBiaya();
    }

    Button buttonTreatmentOK;
    ScrollView scrollViewBooking;

    private void renderBiaya(){

        bottomView.requestFocus();
        scrollViewBooking.post(new Runnable() {
            @Override
            public void run() {
                scrollViewBooking.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        if(help.count()>0){
            buttonTreatmentOK.setVisibility(View.VISIBLE);
            textViewEstimasiBiaya.setVisibility(View.VISIBLE);
            String totalText = help.getTotalPrice();


            textViewEstimasiBiaya.setText("Estimasi Biaya : " + totalText);
        }else{
            buttonTreatmentOK.setVisibility(View.GONE);
            textViewEstimasiBiaya.setVisibility(View.GONE);
        }

    }

    public void tampilkanJadwal(View v){
      showBookingTable(true);
    }

    private void showBookingTable(boolean b){
        if(b) {
            linearTreatmentPick.setVisibility(View.GONE);
            linearBooking.setVisibility(View.VISIBLE);
        } else {
            linearTreatmentPick.setVisibility(View.VISIBLE);
            linearBooking.setVisibility(View.GONE);
        }
    }

    public void kalkulasiBiaya(View v) {

        CheckBox chb = (CheckBox) v;

        String nama = chb.getText().toString();

        if(chb.isChecked()){

              help.addItem(nama);

        }else{
                help.removeItem(nama);
        }

        //ShowDialog.message(this, "terpilih " + chb.getText());

        renderBiaya();
    }

    int prof;
    private void updateUserProfile() {

        gender = UserData.getPreferenceInt(Keys.USER_GENDER);

        if (gender == Keys.MODE_IKHWAN) {
            imageViewUserProfile.setImageResource(R.drawable.ikhwan_logo);
        } else if (gender == Keys.MODE_AKHWAT) {
            imageViewUserProfile.setImageResource(R.drawable.akhwat_logo);
        }

        prof = UserData.getPreferenceInt(Keys.USER_PROFESSION);

        if(prof == Keys.PROFESI_UMUM){
            imageViewProfesi.setImageResource(R.drawable.umum_icon);
        }else if(prof == Keys.PROFESI_PELAJAR){
            imageViewProfesi.setImageResource(R.drawable.pelajar_icon);
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
                    JsonElement mJson = parser.parse(jsons.toString());

                    Schedule object[] = gson.fromJson(mJson, Schedule[].class);

                    for (Schedule single : object) {
                        addingDataRow(single);
                    }

                    showLoading(false);

                } else if (urlTarget.contains(URLReference.AdhanWebsite)) {

                    //ShowDialog.message(this, respond);

                    JSONObject jsons = RespondHelper.getObject(respond, "data");

                    Hijri dataHijriyyah = gson.fromJson(jsons.getJSONObject("hijri").toString(), Hijri.class);

                    Weekday dataHijriyyahWeek = gson.fromJson(jsons.getJSONObject("hijri").getJSONObject("weekday").toString(), Weekday.class);
                    Month dataHijriyyahMonth = gson.fromJson(jsons.getJSONObject("hijri").getJSONObject("month").toString(), Month.class);

                    // ShowDialog.message(this, dataHijriyyahWeek.getEn());

                    textViewDateIslamic.setText(dataHijriyyahWeek.getEn() + " "
                            + dataHijriyyah.getDay() + " "
                            + dataHijriyyahMonth.getEn() + " "
                            + dataHijriyyah.getYear());

                }

            } else if (!RespondHelper.isValidRespond(respond)) {

                String pesan = "Maaf sekali, tidak ada jadwal yg kosong!";
                ShowDialog.message(this, pesan);

                showLoading(false);
                textViewPetunjuk.setText(pesan);

                showBookingTable(true);

            }
        } catch (Exception ex) {
            ShowDialog.message(this, "Error " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public String getAlbumStorageDir() {
        String path = Environment.getExternalStorageDirectory()
                + "/Android/data/" + getApplicationContext().getPackageName();

        // creating file folder first
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }

        return path;
    }

    public void shareTo(View view) {
        // screenshot
        View v = getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        File file = null;

        // save locally
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            file = new File(getAlbumStorageDir(), "/_jadwal_therapy_"
                    + System.currentTimeMillis() + ".png");

            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            Uri uri = Uri.fromFile(file);


            // share to another app
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share Booking Jadwal Therapy");
            intent.putExtra(Intent.EXTRA_TEXT, "Ayo kita *booking jadwal therapy* di RTH...");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/png");
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            ShowDialog.message(this, "error at 411 " + e.getMessage());
        }

    }

    public String getBookingCode() {

        String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // 36 letter.
        String randomStr = generateRandom(aToZ);
        String s = "Kode Booking: *" + randomStr + "*\n";
        return s;

    }

    public String getTreatment(){
        StringBuffer mes  = new StringBuffer();

        int i = 1;
        mes.append("Saya perlu *treatment :* ");

        if(help.count()>0) {
            for (String item : help.getItems()) {
                mes.append("\n" + i + "." + item );
                i++;
            }

            mes.append("\n");
        } else {
            mes.append("umum\n");
        }

        return mes.toString();
    }

    private static String generateRandom(String aToZ) {
        Random rand = new Random();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randIndex = rand.nextInt(aToZ.length());
            if (i == 4) {
                res.append("-");
            }
            res.append(aToZ.charAt(randIndex));
        }
        return res.toString();
    }

    public String createText(String hour) {
        String kelamin;

        if (gender == Keys.MODE_IKHWAN) {
            kelamin = "pria";
        } else {
            kelamin = "wanita";
        }

        String lowerMessage = "\nMohon agar dijadwalkan *Therapy*\n" +
                "apakah bisa?" +
                "\n\n";

        String res = "Bismillah,\nSaya *" + usName + "* ingin *Booking Jadwal*\n\n*" +
                dateChosen + "* Pada *Jam " + hour + "*\n\n" +
                getBookingCode() +
                getTreatment() +
                lowerMessage;

        res = res.replaceAll("\n", "%0a");

        return res;
    }


    private boolean isAvailable(TextView v) {

        // if it has a word of 'tidak'
        if (v.getText().toString().toLowerCase().contains("tidak")) {
            return false;
        }

        return true;
    }

    public void openLink(String hourSelected) {

        // we assume this person has filled in the form
        UserData.savePreference(Keys.USER_REGISTER_STATUS, true);

        URLMaker umaker = new URLMaker();
        umaker.setMainURL(URLReference.RegistrationPage);
        umaker.addParameterValue(Keys.HOUR_SELECTED, hourSelected);
        // date is using dd-MM-yyyy format
        String dateForCognito = convertDateFormat(dateChosen, "EEEE dd-MM-yyyy", Keys.LANGUAGE_ID);
        umaker.addParameterValue(Keys.DATE_CHOSEN, dateForCognito);

        // name is using html parameter encoder
        umaker.addParameterValue(Keys.USERNAME, usName);
        umaker.addParameterValue(Keys.WHATSAPP, wa);

        Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse(umaker.getCompleteURL()));
        n.setPackage("com.android.chrome");
        startActivity(n);

    }

    public void openChatJam08(View v) {

        visitLinkOrWhatsapp(buttonBookingJam08, textViewKet08, "08:00");

        //ShowDialog.message(this, "Error saat jam 08!");
    }

    public void visitLinkOrWhatsapp(Button btn, TextView txt, String hourSelected) {
        if (btn.getTag().toString().equalsIgnoreCase("link")) {
            // buka ke pendaftaran
            openLink(hourSelected);
            ShowDialog.message(this, "Silahkan isi form dengan lengkap!");
        } else {

            // buka whatsapp
            if (isAvailable(txt)) {
                waSender.sendMessageToWhatsAppContact(NO_RTH, createText(hourSelected));
            } else {
                ShowDialog.message(this, "Maaf jam " + hourSelected + " tidak tersedia!");
            }


        }

        finish();
    }

    public void openLinkMaps(View v) {

        Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse(URLReference.Gmaps));
        n.setPackage("com.android.chrome");
        startActivity(n);

    }

    public void openLinkWeb(View v) {

        Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse(URLReference.MainWebsite));
        n.setPackage("com.android.chrome");
        startActivity(n);

    }

    public void openChatJam10(View v) {

        visitLinkOrWhatsapp(buttonBookingJam10, textViewKet10, "10:00");
    }

    public void openChatJam13(View v) {
        visitLinkOrWhatsapp(buttonBookingJam13, textViewKet13, "13:00");
    }

    public void openChatJam16(View v) {

        visitLinkOrWhatsapp(buttonBookingJam16, textViewKet16, "16:00");

    }

    public void openChatJam20(View v) {

        visitLinkOrWhatsapp(buttonBookingJam20, textViewKet20, "20:00");

    }

    public void chooseProfile(View v) {

        Intent i = new Intent(this, ClientProfileActivity.class);
        startActivity(i);

        finish();

    }

}