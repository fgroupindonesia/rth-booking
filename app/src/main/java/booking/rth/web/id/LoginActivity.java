package booking.rth.web.id;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import helper.Navigator;
import helper.RespondHelper;
import helper.ShowDialog;
import helper.URLReference;
import helper.WebRequest;

public class LoginActivity extends AppCompatActivity implements Navigator {

    EditText editTextUsername, editTextPassword;
    TextView textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        textViewMessage = (TextView) findViewById(R.id.textViewMessage);
    }

    public String getText(EditText txt){
        return txt.getText().toString();
    }

    private boolean isThisNoHP(String data){

        if(data.startsWith("+628") || data.startsWith("08")){
            return true;
        }

        return false;

    }

    public void verifyUser(View v){

        WebRequest httpCall = new WebRequest(LoginActivity.this,LoginActivity.this);

        if(isThisNoHP(getText(editTextUsername))){
            httpCall.addData("contact", getText(editTextUsername));
        }else{
            httpCall.addData("username", getText(editTextUsername));
        }

         httpCall.addData("pass", getText(editTextPassword));

        // we need to wait for the response
        httpCall.setWaitState(true);
        httpCall.setRequestMethod(WebRequest.POST_METHOD);
        httpCall.setTargetURL(URLReference.UserVerify);
        httpCall.execute();
    }

    @Override
    public void nextActivity() {

    }

    private void openTutorialActivity(){
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

                    textViewMessage.setVisibility(View.GONE);
                    openTutorialActivity();
                }

            } else if (!RespondHelper.isValidRespond(respond)) {


                textViewMessage.setVisibility(View.VISIBLE);
                //finish();

            }
        } catch (Exception ex) {
            ShowDialog.message(this, "Error " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}