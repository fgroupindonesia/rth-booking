package booking1.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
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
import object.Ruqyah;
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

    LinearLayout linearTreatmentPick, linearBooking, linearWarningRuqyah;

    int gender;
    // 1 : male
    // 2 : female

    int profesi;
    // 1 : pelajar
    // 2 : umum
    // -1 : none

    ProgressBar progressBarLoading;

    ImageView imageViewUserProfile, imageViewProfesi, imageViewRuqyah;
    WhatsappSender waSender;

    View bottomView;

    // limit booking is before this hour
    int hourBeforeBooked = 1;
    Animation animBounce;

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
        linearWarningRuqyah = (LinearLayout) findViewById(R.id.linearWarningRuqyah);

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
        imageViewRuqyah = (ImageView) findViewById(R.id.imageViewRuqyah);

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
        if (!UserData.getPreferenceBoolean(Keys.USER_REGISTER_STATUS)) {
            // directly show the timetable (jadwal)
            // and choose the treatment on website form
            showBookingTable(true);
            showTreatmentOptions(false);

        }

        // for animated ruqyah purposes only
        animBounce = AnimationUtils.loadAnimation(this, R.anim.blinking);


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

    private void checkRuqyahMode() {

        WebRequest httpCall = new WebRequest(BookingScheduleActivity.this, BookingScheduleActivity.this);

        String dateAmrik = convertDateFormat(dateChosen, "yyyy-MM-dd", Keys.LANGUAGE_EN);

        httpCall.addData("date_chosen", dateAmrik);
        httpCall.addData("gender_therapist", String.valueOf(gender));

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setDownloadState(false);
        httpCall.setMultipartform(false);

        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.RuqyahCheck);
        httpCall.execute();

   //     ShowDialog.message(this, "Ruqyah is checked! " + dateAmrik);
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
        txt.setTextColor(getResources().getColor(R.color.green));
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.note16, 0);
    }

    private void setBold(TextView txt) {
        txt.setTypeface(null, Typeface.BOLD);
    }

    private void addingDataRow(Schedule obj) {

        if (obj.getSpecific_hour().equalsIgnoreCase("08:00")) {

            // we need to ensure this is not today and within 1 hour booked
            // as the time limit already reached

            if (obj.getStatus() != 1 && canBookingHour(8)) {
                setAvailable(textViewKet08);
                setBold(textViewHour08);
                buttonBookingJam08.setVisibility(View.VISIBLE);
            }

        } else if (obj.getSpecific_hour().equalsIgnoreCase("10:00")) {

            if (obj.getStatus() != 1 && canBookingHour(10)) {
                setAvailable(textViewKet10);
                setBold(textViewHour10);
                buttonBookingJam10.setVisibility(View.VISIBLE);
            }
        } else if (obj.getSpecific_hour().equalsIgnoreCase("13:00")) {

            if (obj.getStatus() != 1 && canBookingHour(13)) {
                setAvailable(textViewKet13);
                setBold(textViewHour13);
                buttonBookingJam13.setVisibility(View.VISIBLE);
            }
        } else if (obj.getSpecific_hour().equalsIgnoreCase("16:00")) {


            if (obj.getStatus() != 1 && canBookingHour(16)) {
                setAvailable(textViewKet16);
                setBold(textViewHour16);
                buttonBookingJam16.setVisibility(View.VISIBLE);
            }
        } else if (obj.getSpecific_hour().equalsIgnoreCase("20:00")) {


            if (obj.getStatus() != 1 && canBookingHour(20)) {
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

    private void clearAllCheckboxes(boolean b) {

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

    public void resetTreatment(View v) {
        help.resetItem();
        if (((CheckBox) v).isChecked()) {
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

    private void renderBiaya() {

        bottomView.requestFocus();
        scrollViewBooking.post(new Runnable() {
            @Override
            public void run() {
                scrollViewBooking.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        if (help.count() > 0) {
            buttonTreatmentOK.setVisibility(View.VISIBLE);
            textViewEstimasiBiaya.setVisibility(View.VISIBLE);
            String totalText = help.getTotalPrice();


            textViewEstimasiBiaya.setText("Estimasi Biaya : " + totalText);
        } else {
            buttonTreatmentOK.setVisibility(View.GONE);
            textViewEstimasiBiaya.setVisibility(View.GONE);
        }

    }

    public void tampilkanJadwal(View v) {
        showLoading(false);
        showTreatmentOptions(false);
        showBookingTable(true);

        // showWarningRuqyah(false);
        // once schedule are not available now we check the ruqyah mode
        checkRuqyahMode();

    }

    private void showTreatmentOptions(boolean b) {
        if (b) {
            linearTreatmentPick.setVisibility(View.VISIBLE);
        } else {
            linearTreatmentPick.setVisibility(View.GONE);
        }
    }

    private void showBookingTable(boolean b) {
        if (b) {
            linearBooking.setVisibility(View.VISIBLE);
            checkRateApp();
        } else {
            linearBooking.setVisibility(View.GONE);
        }
    }

    public void kalkulasiBiaya(View v) {

        CheckBox chb = (CheckBox) v;

        String nama = chb.getText().toString();

        if (chb.isChecked()) {

            help.addItem(nama);

        } else {
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

        if (prof == Keys.PROFESI_UMUM) {
            imageViewProfesi.setImageResource(R.drawable.umum_icon);
        } else if (prof == Keys.PROFESI_PELAJAR) {
            imageViewProfesi.setImageResource(R.drawable.pelajar_icon);
        }
    }

    private boolean canBookingHour(int hourUsed) {

        // this function only used for the selected day only
        // if another date chosen we will not use this function
        // because it is based on this limit Hour only
        // thus the buttons are all restricted

        SimpleDateFormat originalFormat = new SimpleDateFormat("EEEE dd-MMM-yyyy", new Locale("ID"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat sdfJam = new SimpleDateFormat("HH:mm");

        boolean isItToday, disableHour = false;

        try {

            Date tglTerpilih = originalFormat.parse(dateChosen);
            Date tglHariIni = new Date();

            String tglTerpilihText, tglHariIniText;

            tglHariIniText = sdf.format(tglHariIni);
            tglTerpilihText = sdf.format(tglTerpilih);

            isItToday = tglHariIniText.equalsIgnoreCase(tglTerpilihText);

            if (isItToday) {

                // lets do the checking
                String dataJam[] = sdfJam.format(tglHariIni).split(":");
                // ShowDialog.message(this, "Didapat " + sdfJam.format(tglHariIni));
                int h = Integer.parseInt(dataJam[0]);
                int m = Integer.parseInt(dataJam[1]);

                // limit allowed for 8 morning
                // is before 8-1 : 07:00 and before
                // etc ...
                disableHour = canBook(hourUsed, h, m);
                // ShowDialog.message(this, "for " + hourUsed + " you " + disableHour);
            }else{
                // because it is not today so
                // let it be
                disableHour = true;
            }

        } catch (Exception ex) {
            ShowDialog.message(this, "Error in logic of disableHour..." + ex.getMessage());
        }

        return disableHour;

    }

    private boolean canBook(int hourIn, int h, int m) {
        boolean b = true;

        if (h == hourIn - 1 && m == 0) {
            b = false;
        } else if (h < hourIn - 1 && m > 0) {
            b = true;
        }

        return b;
    }

    @Override
    public void nextActivity() {

    }

    private void showWarningRuqyah(boolean b) {

        if (b) {
            blinking();
            linearWarningRuqyah.setVisibility(View.VISIBLE);
        } else {
            linearWarningRuqyah.setVisibility(View.GONE);
        }

    }

    @Override
    public void onSuccess(String urlTarget, String respond) {

        try {
            //ShowDialog.message(this, "dapt " + respond);

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();

            if (RespondHelper.isValidRespond(respond)) {

                if (urlTarget.contains(URLReference.RuqyahCheck)) {

                    JSONObject jsons = RespondHelper.getObject(respond, "multi_data");

                    JsonElement mJson = parser.parse(jsons.toString());

                    Ruqyah object = gson.fromJson(mJson, Ruqyah.class);
                    String dateServerFormat = convertDateFormat(dateChosen, "yyyy-MM-dd", Keys.LANGUAGE_EN);

                    if (object.getDate_chosen().equalsIgnoreCase(dateServerFormat)) {
                        if (object.getGender_therapist() == gender && object.getStatus() == 1) {
                            showLoading(false);
                            showBookingTable(false);
                            showTreatmentOptions(false);
                            showWarningRuqyah(true);
                        }
                    }

                  //  ShowDialog.message(this, "ruqyah ada tapi... " + object.getStatus());

                } else if (urlTarget.contains(URLReference.ScheduleDetail)) {

                    JSONArray jsons = RespondHelper.getArray(respond, "multi_data");

                    JsonElement mJson = parser.parse(jsons.toString());

                    Schedule object[] = gson.fromJson(mJson, Schedule[].class);

                    for (Schedule single : object) {
                        addingDataRow(single);
                    }

                    showLoading(false);
                    showBookingTable(false);
                    showTreatmentOptions(true);
                    showWarningRuqyah(false);



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

                if (urlTarget.contains(URLReference.ScheduleDetail)) {
                    String pesan = "Maaf sekali, tidak ada jadwal yg kosong!";
                    ShowDialog.message(this, pesan);

                    showLoading(false);
                    showBookingTable(true);
                    showTreatmentOptions(false);
                    showWarningRuqyah(false);

                    textViewPetunjuk.setText(pesan);



                }


            }
        } catch (Exception ex) {
            ShowDialog.message(this, "Error " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public String getAlbumStorageDir() {

        // creating file folder first
        File file = getFilesDir();
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    private File exportFile(File src, File dst) throws Exception {

        //if folder does not exist
        if (!dst.exists()) {
            if (!dst.mkdirs()) {
                return null;
            }
        }

        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File expFile = new File(dst.getPath() + File.separator + src.getName());
        FileChannel inChannel = null;
        FileChannel outChannel = null;


        inChannel = new FileInputStream(src).getChannel();
        outChannel = new FileOutputStream(expFile).getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        if (inChannel != null)
            inChannel.close();
        if (outChannel != null)
            outChannel.close();

        return expFile;
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

            // we make a copy in the SD Card
            String temp = Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName();
            File fileTemp = new File(temp);

            if (!fileTemp.exists()) {
                fileTemp.mkdirs();
            }

            // we create a new one

            fileTemp = new File(temp + File.separator + file.getName());
            File fileFinal = exportFile(file, fileTemp);

            Uri uri = Uri.fromFile(fileFinal);

            // share to another app
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share Booking Jadwal Therapy");

            // check jika sedang ruqyah kalimatnya bedain
            if (linearWarningRuqyah.getVisibility() == View.VISIBLE) {
                intent.putExtra(Intent.EXTRA_TEXT, "Saat ini *booking jadwal therapy* di RTH sedang Ruqyah...");
            } else {
                intent.putExtra(Intent.EXTRA_TEXT, "Ayo kita *booking jadwal therapy* di RTH...");
            }

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

    public String getTreatment() {
        StringBuffer mes = new StringBuffer();

        int i = 1;
        mes.append("Saya perlu *treatment :* ");

        if (help.count() > 0) {
            for (String item : help.getItems()) {
                mes.append("\n" + i + "." + item);
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

    private void checkRateApp() {

        boolean pernahRate = UserData.getPreferenceBoolean(Keys.RATE_APP_PREVIOUS);

        if (!pernahRate) {
            showDialogRate();
        }

    }

    Dialog dialog;

    private void showDialogRate() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_rate);

        Button btnCancel = dialog.findViewById(R.id.dialog_cancel);
        Button btnOk = dialog.findViewById(R.id.dialog_ok);

        btnOk.setText("Save");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save dulu status nya TRUE
                UserData.savePreference(Keys.RATE_APP_PREVIOUS, true);

                rateApp();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setTitle("Rate App ini");
        dialog.show();
    }


    private void blinking() {

        imageViewRuqyah.startAnimation(animBounce);


    }

    // this is for rate app

    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (Exception e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }


    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
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

    public void openChatJam08(View v) {

        visitLinkOrWhatsapp(buttonBookingJam08, textViewKet08, "08:00");

        //ShowDialog.message(this, "Error saat jam 08!");
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