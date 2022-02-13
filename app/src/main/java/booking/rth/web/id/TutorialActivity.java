package booking.rth.web.id;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import helper.UserProfile;
import object.Keys;

public class TutorialActivity extends AppCompatActivity {

    Button buttonOKClient, buttonOKManajemen;
    LinearLayout linearTutorialPasien, linearTutorialManajemen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        buttonOKClient = (Button) findViewById(R.id.buttonOKClient);
        buttonOKManajemen = (Button) findViewById(R.id.buttonOKManajemen);

        linearTutorialPasien = (LinearLayout) findViewById(R.id.linearTutorialPasien);
        linearTutorialManajemen = (LinearLayout) findViewById(R.id.linearTutorialManajemen);

        showLayoutAsUsage();

    }


    private void showLayoutAsUsage(){

        if(UserProfile.USAGE == Keys.MANAGEMENT){
            linearTutorialManajemen.setVisibility(View.VISIBLE);
            linearTutorialPasien.setVisibility(View.GONE);
        }else{
            linearTutorialManajemen.setVisibility(View.GONE);
            linearTutorialPasien.setVisibility(View.VISIBLE);
        }

    }

    public boolean isTextEquals(Button btn, String compared){
        return btn.getText().toString().equalsIgnoreCase(compared);
    }

    public void openNext(View v){

        if(haveNetworkConnection()){
            Intent intent = null;
           if(UserProfile.USAGE == Keys.CLIENT && isTextEquals(buttonOKClient, "ok")){
                 intent = new Intent(this, ClientProfileActivity.class);

           } else  if(UserProfile.USAGE == Keys.MANAGEMENT && isTextEquals(buttonOKManajemen, "ok")){
               intent = new Intent(this, CalendarActivity.class);

           }

           if(intent!=null){
               startActivity(intent);
           }

            finish();
        } else{
            // ubah button jadi Exit
            ((Button) v).setText("Exit");
            // lalu munculkan popup no internet connection
            popup("No Internet Connection!");
        }


    }

    private void popup(String pesan){
        Toast.makeText(this, pesan,
                Toast.LENGTH_LONG).show();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}