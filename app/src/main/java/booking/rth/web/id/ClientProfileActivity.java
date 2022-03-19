package booking.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import object.Keys;
import shared.UserData;

public class ClientProfileActivity extends AppCompatActivity {

    LinearLayout linearAkhwat, linearIkhwan, linearLayoutDetectRegister,
    linearLayoutDataForm, linearLayoutProfile;

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

        linearAkhwat = (LinearLayout) findViewById(R.id.linearAkhwat);
        linearIkhwan = (LinearLayout) findViewById(R.id.linearIkhwan);
        linearLayoutDetectRegister = (LinearLayout) findViewById(R.id.linearLayoutDetectRegister);
        linearLayoutProfile = (LinearLayout) findViewById(R.id.linearLayoutProfile);
        linearLayoutDataForm = (LinearLayout) findViewById(R.id.linearLayoutDataForm);

        editTextNamaLengkap = (EditText) findViewById(R.id.editTextNamaLengkap);
        editTextNomerWhatsapp = (EditText) findViewById(R.id.editTextNomerWhatsapp);

        buttonCheck = (Button) findViewById(R.id.buttonCheck);

        centerTitleApp();

        // first time is
        // ask are you a new / registered patient? --> save
        // ask again are you boy / girl?  --> save
        // both are required to input username & whatsapp  --> save
        if(!UserData.getPreferenceBoolean(Keys.USER_FORM_STATUS_COMPLETED)){
            showLayout(Keys.LAYOUT_DETECT_REGISTER);
        }else{
            showLayout(Keys.LAYOUT_CHOOSE_PROFILE);
        }

        // next time this opened again
        // because we already ask & has the registered patient data locally
        // so we just ask are you a boy / girl? ---> save once more
        // but we will not ask username & whatsapp anymore
        // just do booking

    }

    public String getTextFromElement(EditText edt){
        return edt.getText().toString();
    }

    public void saveAndCheck(View v){

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
            textviewTitle.setText(R.string.text_booking_untuk);

        } else if (jenisLayout == Keys.LAYOUT_DATA_FORM) {
            linearLayoutDetectRegister.setVisibility(View.GONE);
            linearLayoutProfile.setVisibility(View.GONE);
            linearLayoutDataForm.setVisibility(View.VISIBLE);
            textviewTitle.setText(R.string.text_data_lengkap);

        }else if(jenisLayout == Keys.LAYOUT_DETECT_REGISTER){
            linearLayoutDetectRegister.setVisibility(View.VISIBLE);
            linearLayoutProfile.setVisibility(View.GONE);
            linearLayoutDataForm.setVisibility(View.GONE);
            textviewTitle.setText(R.string.text_profil_anda);
        }
    }


    public void openChooseProfile(View v) {

        String tag = v.getTag().toString();

        if(tag.equalsIgnoreCase("lama")){
            userRegister = true;
        }else{
            userRegister = false;
        }

        UserData.savePreference(Keys.USER_REGISTER_STATUS, userRegister);
        showLayout(Keys.LAYOUT_CHOOSE_PROFILE);
    }

    private void centerTitleApp() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    public void openDataForm(View v) {

        String tag  = v.getTag().toString();

        if(tag.equalsIgnoreCase("akhwat")){
            gender = Keys.MODE_AKHWAT;
        }else{
            gender = Keys.MODE_IKHWAN;
        }

        UserData.savePreference(Keys.USER_GENDER, gender);
        if(!UserData.getPreferenceBoolean(Keys.USER_FORM_STATUS_COMPLETED)){
            showLayout(Keys.LAYOUT_DATA_FORM);
        }else{
            openNext();
        }

    }


    public void openNext() {

        // gender is already defined either from button (linearLayout) clicked
        // or from the local data stored

        Intent i = new Intent(this, PickDateActivity.class);
        startActivity(i);



    }

}