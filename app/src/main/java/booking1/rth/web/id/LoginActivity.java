package booking1.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class LoginActivity extends AppCompatActivity implements Navigator {

    EditText editTextUsername, editTextPassword;
    TextView textViewMessage;
    ProgressBar progressBarLoading;
    Button buttonLogin;

    int gender, userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        progressBarLoading = (ProgressBar) findViewById(R.id.progressBarLoading);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        textViewMessage = (TextView) findViewById(R.id.textViewMessage);

        centerTitleApp();
    }

    public String getText(EditText txt) {
        return txt.getText().toString();
    }

    private boolean isThisNoHP(String data) {

        if (data.startsWith("+628") || data.startsWith("08")) {
            return true;
        }

        return false;

    }

    public void verifyUser(View v) {

        progressBarLoading.setVisibility(View.VISIBLE);
        buttonLogin.setVisibility(View.INVISIBLE);

        WebRequest httpCall = new WebRequest(LoginActivity.this, LoginActivity.this);

        if (isThisNoHP(getText(editTextUsername))) {
            httpCall.addData("contact", getText(editTextUsername));
        } else {
            httpCall.addData("username", getText(editTextUsername));
        }

        httpCall.addData("pass", getText(editTextPassword));

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.UserVerify);
        httpCall.execute();
    }

    private void centerTitleApp() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    @Override
    public void nextActivity() {

    }

    private void openTutorialActivity() {
        Intent i = new Intent(this, TutorialActivity.class);
        startActivity(i);

        finish();
    }

    @Override
    public void onSuccess(String urlTarget, String respond) {
        try {
            // ShowDialog.message(this, "dapt " + respond);

            Gson objectG = new Gson();

            if (RespondHelper.isValidRespond(respond)) {

                if (urlTarget.contains(URLReference.UserVerify)) {

                    JSONObject jsons = RespondHelper.getObject(respond, "multi_data");

                    JsonParser parser = new JsonParser();
                    JsonElement mJson = parser.parse(jsons.toString());

                    User objectUser = objectG.fromJson(mJson, User.class);

                    // we set temporarily for the integer (of gender) along with its ID
                    gender = objectUser.getGender();
                    userid = objectUser.getId();

                    UserData.savePreference(Keys.USER_GENDER, gender);
                    UserData.savePreference(Keys.USER_ID, userid);

                    //ShowDialog.message(this, "terverifikasi gender "+ gender + " dengan id "+ userid);

                    progressBarLoading.setVisibility(View.INVISIBLE);
                    buttonLogin.setVisibility(View.INVISIBLE);

                    textViewMessage.setVisibility(View.GONE);
                    openTutorialActivity();
                }

            } else if (!RespondHelper.isValidRespond(respond)) {

                progressBarLoading.setVisibility(View.INVISIBLE);
                buttonLogin.setVisibility(View.VISIBLE);

                textViewMessage.setVisibility(View.VISIBLE);
                //finish();

            }
        } catch (Exception ex) {
            ShowDialog.message(this, "Error " + ex.getMessage());
            ex.printStackTrace();

            progressBarLoading.setVisibility(View.INVISIBLE);
            buttonLogin.setVisibility(View.VISIBLE);

        }

    }
}