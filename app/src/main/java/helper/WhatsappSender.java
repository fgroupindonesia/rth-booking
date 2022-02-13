package helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.net.URLEncoder;

public class WhatsappSender {
    Context context;

    public WhatsappSender(Context x){
        context = x;
    }



    public void sendMessageToWhatsAppContact(String number, String message) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        try {
            String url = "https://wa.me/"+ number + "?text=" + message;
           context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            url
                    )));
        } catch (Exception e) {

            ErrorLogger.write(e);
            e.printStackTrace();
        }
    }

}
