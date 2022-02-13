package helper;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class ErrorLogger {


    public static void write(Exception ex){

        File file = null;
        try{
            File sdCard = Environment.getExternalStorageDirectory();
            file = new File(sdCard.getAbsolutePath() + "/Android/data/booking.rth.web.id/");
            file.mkdirs();
            file = new File(file.getAbsolutePath() + "/error.log");
            file.createNewFile();
            PrintStream ps = new PrintStream(file);

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = df.format(c);

            ps.println(formattedDate);
            ex.printStackTrace(ps);
            //ps.println(ex.initCause(ex).toString());
            ps.close();

            Log.e(TAG,Log.getStackTraceString(ex));

        } catch(Exception e){
            //
        }

    }

}
