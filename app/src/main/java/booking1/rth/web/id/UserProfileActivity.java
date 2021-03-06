package booking1.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import helper.Navigator;
import helper.RespondHelper;
import helper.ShowDialog;
import helper.URLReference;
import helper.WebRequest;
import object.Keys;
import object.User;
import shared.UserData;

public class UserProfileActivity extends AppCompatActivity implements Navigator {

    ImageView imageViewUserProfile;
    EditText editTextFullname, editTextPass, editTextEmail, editTextAlamat,
            editTextUsername, editTextNoKontak;
    Spinner spinnerKelamin, spinnerMembership;

    int userid, gender;

    ProgressBar progressBar2;
    Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // for future usage
        UserData.setPreference(this);

        buttonOK = (Button) findViewById(R.id.buttonOkProfile);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        spinnerKelamin = (Spinner) findViewById(R.id.spinnerKelamin);

        editTextFullname = (EditText) findViewById(R.id.editTextFullname);
        editTextAlamat = (EditText) findViewById(R.id.editTextAlamat);
        editTextNoKontak = (EditText) findViewById(R.id.editTextNoKontak);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPass = (EditText) findViewById(R.id.editTextPass);

        imageViewUserProfile = (ImageView) findViewById(R.id.imageViewUserProfile);

        centerTitleApp();

        userid = UserData.getPreferenceInt(Keys.USER_ID);

        updateUserProfile();

        if(userid!=0){
            // grab the data from server
            getDetails();
        }


    }



    private void centerTitleApp(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    private void updateUserProfile(){

       gender = UserData.getPreferenceInt(Keys.USER_GENDER);

        if(gender==Keys.MODE_IKHWAN){
            imageViewUserProfile.setImageResource(R.drawable.ikhwan_logo);
        }else if(gender==Keys.MODE_AKHWAT){
            imageViewUserProfile.setImageResource(R.drawable.akhwat_logo);
        }

    }

    private void getDetails(){

        WebRequest httpCall = new WebRequest(UserProfileActivity.this,UserProfileActivity.this);
        httpCall.addData("id", String.valueOf(userid));

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.UserProfile);
        httpCall.execute();

    }

    public String getText(EditText txt){
        return txt.getText().toString();
    }


    public void saveUserProfile(View v){

        // gender 0 is female
        // gender 1 is male
      gender = Keys.MODE_AKHWAT;
        if(spinnerKelamin.getSelectedItem().toString().equalsIgnoreCase("pria")){
            gender = Keys.MODE_IKHWAN;
        }

        WebRequest httpCall = new WebRequest(UserProfileActivity.this,UserProfileActivity.this);
        httpCall.addData("username", getText(editTextUsername));
        httpCall.addData("pass", getText(editTextPass));
        httpCall.addData("email", getText(editTextEmail));
        httpCall.addData("home_address", getText(editTextAlamat));
        httpCall.addData("contact", getText(editTextNoKontak));
        httpCall.addData("full_name", getText(editTextFullname));
        httpCall.addData("gender", String.valueOf(gender));
        httpCall.addData("id", String.valueOf(userid));

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.UserUpdate);
        httpCall.execute();

        progressBar2.setVisibility(View.VISIBLE);
        buttonOK.setVisibility(View.GONE);

    }

    private void renderData(User dataIn){

        editTextEmail.setText(dataIn.getEmail());
        editTextNoKontak.setText(dataIn.getContact());
        editTextAlamat.setText(dataIn.getHome_address());
        editTextFullname.setText(dataIn.getFull_name());
        editTextPass.setText(dataIn.getPass());
        editTextUsername.setText(dataIn.getUsername());

        spinnerKelamin.setSelection(dataIn.getGender());
        //spinnerMembership.setSelection(dataIn.getMembership());

        progressBar2.setVisibility(View.GONE);
        buttonOK.setVisibility(View.VISIBLE);

    }

    @Override
    public void nextActivity() {

    }

    @Override
    public void onSuccess(String urlTarget, String respond) {

        try {
            //ShowDialog.message(this, "Returned  " + respond);

            Gson objectG = new Gson();

            if (RespondHelper.isValidRespond(respond)) {

                if (urlTarget.contains(URLReference.UserProfile)) {

                    JSONObject jsons = RespondHelper.getObject(respond, "multi_data");

                    JsonParser parser = new JsonParser();
                    JsonElement mJson =  parser.parse(jsons.toString());
                     User object  = objectG.fromJson(mJson, User.class);

                    // data returned is for current user profile only

                    renderData(object);


                } else   if (urlTarget.contains(URLReference.UserUpdate)) {

                    // stored locally if success
                    UserData.savePreference(Keys.USER_GENDER, gender);

                    Intent i = new Intent(this, CalendarActivity.class);
                    startActivity(i);

                   // ShowDialog.message(this, "user updated!" + gender);

                    finish();


                }

            } else if (!RespondHelper.isValidRespond(respond)) {

                ShowDialog.message(this, "Ada kesalahan dari Server!");
                ShowDialog.message(this, respond);
                //finish();

                progressBar2.setVisibility(View.GONE);
                buttonOK.setVisibility(View.VISIBLE);



            }
        } catch (Exception ex) {
            ShowDialog.message(this, "Error " + ex.getMessage());
            ex.printStackTrace();
        }


    }
}