package booking1.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import helper.TextChangedListener;
import helper.URLReference;
import object.Keys;
import shared.UserData;

public class ClientProfileActivity extends AppCompatActivity {

    LinearLayout linearAkhwat, linearIkhwan, linearLayoutDetectRegister,
            linearLayoutDataForm, linearLayoutProfile, linearLayoutProfesi,
            linearModeSingle, linearModeMulti, linearLayoutBookingMode;

    TextView textviewTitle;
    int gender;

    boolean userRegister = false;

    Button buttonCheck;
    EditText editTextNamaLengkap, editTextNomerWhatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // for storing data
        UserData.setPreference(this);

        gender = UserData.getPreferenceInt(Keys.USER_GENDER);

        textviewTitle = (TextView) findViewById(R.id.textviewTitle);

        linearModeSingle = (LinearLayout) findViewById(R.id.linearModeSingle);
        linearModeMulti = (LinearLayout) findViewById(R.id.linearModeMulti);
        linearLayoutBookingMode = (LinearLayout) findViewById(R.id.linearLayoutBookingMode);

        linearAkhwat = (LinearLayout) findViewById(R.id.linearAkhwat);
        linearIkhwan = (LinearLayout) findViewById(R.id.linearIkhwan);
        linearLayoutDetectRegister = (LinearLayout) findViewById(R.id.linearLayoutDetectRegister);
        linearLayoutProfile = (LinearLayout) findViewById(R.id.linearLayoutProfile);
        linearLayoutDataForm = (LinearLayout) findViewById(R.id.linearLayoutDataForm);
        linearLayoutProfesi = (LinearLayout) findViewById(R.id.linearLayoutProfesi);

        editTextNamaLengkap = (EditText) findViewById(R.id.editTextNamaLengkap);
        editTextNomerWhatsapp = (EditText) findViewById(R.id.editTextNomerWhatsapp);

        buttonCheck = (Button) findViewById(R.id.buttonCheck);

        centerTitleApp();

        // first time is
        // ask are you a new / registered patient? --> save
        // ask again are you boy / girl?  --> save
        // both are required to input username & whatsapp  --> save
        if (!UserData.getPreferenceBoolean(Keys.USER_FORM_STATUS_COMPLETED)) {
            showLayout(Keys.LAYOUT_DETECT_REGISTER);
        } else {
            // we let the user set the profesi first
            // before choosing profile (gender)
            //ShowDialog.message(this, "ada " + UserData.getPreferenceInt(Keys.USER_PROFESSION));
            if (UserData.getPreferenceInt(Keys.USER_PROFESSION) == Keys.PROFESI_EMPTY) {
                showLayout(Keys.LAYOUT_CHOOSE_PROFESSION);
            }else {
                // because the user has filled everything so
                // we proceed to pick either Single or Multi order booking?
                showLayout(Keys.LAYOUT_CHOOSE_BOOKING_MODE);
            }
        }

        // next time this opened again
        // because we already ask & has the registered patient data locally
        // so we just ask are you a boy / girl? ---> save once more
        // but we will not ask username & whatsapp anymore
        // just do booking


        // this is precaution
        // if the user didnt fill any data
        setEditTextListeners();
    }

    public void openNextActivity(View v){

        // this is single order
        Intent i = new Intent(this, BookingScheduleActivity.class);
        startActivity(i);

    }

    public void openFamilyMember(View v){

        // this would be a multi order
        // but let see the name list first
        Intent i = new Intent(this, MemberActivity.class);
        startActivity(i);

    }

    public void openWebWorkshopForm(View v){

            Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse(URLReference.RegistrationWorkshopPage));
            n.setPackage("com.android.chrome");
            startActivity(n);

    }

    private void setEditTextListeners(){
        TextChangedListener textListener = new TextChangedListener(editTextNomerWhatsapp, editTextNamaLengkap);
        textListener.setButtonLock(buttonCheck);

        editTextNamaLengkap.addTextChangedListener(textListener);
        editTextNomerWhatsapp.addTextChangedListener(textListener);
    }

    public String getTextFromElement(EditText edt) {
        return edt.getText().toString();
    }

    public void saveAndCheck(View v) {

        String wa = getTextFromElement(editTextNomerWhatsapp);
        String nama = getTextFromElement(editTextNamaLengkap);

        // store locally for later usage
        UserData.savePreference(Keys.WHATSAPP, wa);
        UserData.savePreference(Keys.USERNAME, nama);
        UserData.savePreference(Keys.USER_FORM_STATUS_COMPLETED, true);

        // continue to another activity
        openNext();
        finish();
    }

    private void showLayout(int jenisLayout) {
        if (jenisLayout == Keys.LAYOUT_CHOOSE_PROFILE) {
            linearLayoutDetectRegister.setVisibility(View.GONE);
            linearLayoutProfile.setVisibility(View.VISIBLE);
            linearLayoutDataForm.setVisibility(View.GONE);
            linearLayoutProfesi.setVisibility(View.GONE);
            linearLayoutBookingMode.setVisibility(View.GONE);

            textviewTitle.setText(R.string.text_booking_untuk);

        } else if (jenisLayout == Keys.LAYOUT_DATA_FORM) {
            linearLayoutDetectRegister.setVisibility(View.GONE);
            linearLayoutProfile.setVisibility(View.GONE);
            linearLayoutDataForm.setVisibility(View.VISIBLE);
            linearLayoutProfesi.setVisibility(View.GONE);
            linearLayoutBookingMode.setVisibility(View.GONE);

            textviewTitle.setText(R.string.text_data_lengkap);

        } else if (jenisLayout == Keys.LAYOUT_DETECT_REGISTER) {
            linearLayoutDetectRegister.setVisibility(View.VISIBLE);
            linearLayoutProfile.setVisibility(View.GONE);
            linearLayoutDataForm.setVisibility(View.GONE);
            linearLayoutProfesi.setVisibility(View.GONE);
            linearLayoutBookingMode.setVisibility(View.GONE);

            textviewTitle.setText(R.string.text_profil_anda);
        } else if (jenisLayout == Keys.LAYOUT_CHOOSE_PROFESSION) {
            linearLayoutDetectRegister.setVisibility(View.GONE);
            linearLayoutProfile.setVisibility(View.GONE);
            linearLayoutDataForm.setVisibility(View.GONE);
            linearLayoutBookingMode.setVisibility(View.GONE);

            linearLayoutProfesi.setVisibility(View.VISIBLE);
            textviewTitle.setText(R.string.text_profil_anda);

        } else if (jenisLayout == Keys.LAYOUT_CHOOSE_BOOKING_MODE) {
            linearLayoutDetectRegister.setVisibility(View.GONE);
            linearLayoutProfile.setVisibility(View.GONE);
            linearLayoutDataForm.setVisibility(View.GONE);
            linearLayoutProfesi.setVisibility(View.GONE);

            linearLayoutBookingMode.setVisibility(View.VISIBLE);
            textviewTitle.setText(R.string.text_label_pilih_booking);

        }
    }

    boolean pelajar = false;

    public void openChooseProfile(View v) {

        String tag = v.getTag().toString();

        if (tag.equalsIgnoreCase("umum")) {
            pelajar = false;
        } else {
            pelajar = true;
        }

        if (pelajar) {
            UserData.savePreference(Keys.USER_PROFESSION, Keys.PROFESI_PELAJAR);
        } else {
            UserData.savePreference(Keys.USER_PROFESSION, Keys.PROFESI_UMUM);
        }

        showLayout(Keys.LAYOUT_CHOOSE_PROFILE);

    }

    public void openChooseProfesi(View v) {

        String tag = v.getTag().toString();

        if (tag.equalsIgnoreCase("lama")) {
            userRegister = true;
        } else {
            userRegister = false;
        }

        UserData.savePreference(Keys.USER_REGISTER_STATUS, userRegister);
        showLayout(Keys.LAYOUT_CHOOSE_PROFESSION);
    }

    private void centerTitleApp() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    public void openDataForm(View v) {

        String tag = v.getTag().toString();

        if (tag.equalsIgnoreCase("akhwat")) {
            gender = Keys.MODE_AKHWAT;
        } else {
            gender = Keys.MODE_IKHWAN;
        }

        UserData.savePreference(Keys.USER_GENDER, gender);
        if (!UserData.getPreferenceBoolean(Keys.USER_FORM_STATUS_COMPLETED)) {
            showLayout(Keys.LAYOUT_DATA_FORM);
        } else {
            openNext();
        }

    }


    public void openNext() {

        // gender is already defined either from button (linearLayout) clicked
        // or from the local data stored

        // WE try book using single mode
        Intent i = new Intent(this, PickDateActivity.class);
        i.putExtra(Keys.BOOKING_MODE, Keys.MODE_BOOKING_SINGLE_ORDER);

        startActivity(i);

    }

}