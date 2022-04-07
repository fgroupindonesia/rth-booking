package helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import object.Keys;
import object.Schedule;

public class ScheduleOverall {

    ArrayList<String> list = new ArrayList<String>();
    ArrayList<Schedule> listMale = new ArrayList<Schedule>();
    ArrayList<Schedule> listFemale = new ArrayList<Schedule>();

    StringBuffer stbMale = new StringBuffer();
    StringBuffer stbFemale = new StringBuffer();

    StringBuffer stb = new StringBuffer();

    // final output should be:
    // -- OVERALL
    // -- Ikhwan :
    // --- Senin 22/02/2022:
    // --- 08:00 desc
    // --- 10:00 desc

    // --- Selasa 22/02/2022:
    // --- 08:00 desc
    // --- 10:00 desc

    // -- Akhwat :
    // --- Senin 22/02/2022:
    // --- 08:00 desc
    // --- 10:00 desc

    // and lastly here
    public String getCompleteText() {

        if (listMale.size() > 0 && listFemale.size() > 0) {

            stb = new StringBuffer("Ikhwan : \n" + stbMale.toString() +
                    "\n\nAkhwat : \n" + stbFemale.toString());

        } else if(listMale.size()>0 && listFemale.size() == 0){
            stb = new StringBuffer("Ikhwan : \n" + stbMale.toString());
        }else if(listMale.size()==0 && listFemale.size() > 0){
            stb = new StringBuffer("Akhwat : \n" + stbFemale.toString());
        }

        return stb.toString();
    }

    // second translating into special pattern

    String previousData = null;
    public String makeSingleDay(Schedule scb) {
        String finalData = null;

        // final output should be
        // Hari dd-MM-2022
        // 08:00 desc
        // 10:00 desc
        // 13:00 desc
        // 16:00 desc

        if (scb.getGender_therapist() == Keys.MODE_IKHWAN) {
            finalData = populateFor(listMale, scb);

            if(previousData==null){
                stbMale.append(finalData);
                previousData = finalData;
            } else if(!previousData.equalsIgnoreCase(finalData)) {
                stbMale.append(finalData);
                previousData = finalData;
            }

        } else {
            finalData = populateFor(listFemale, scb);

            if(previousData==null){
                stbFemale.append(finalData);
                previousData = finalData;
            } else if(!previousData.equalsIgnoreCase(finalData)) {
                stbFemale.append(finalData);
                previousData = finalData;
            }

        }

        return finalData;

    }

    // third collaborating the formatting output
    private String populateFor(ArrayList<Schedule> dataList, Schedule scb) {
        String temp = null;
        int count = 0;
        // final output should be
        // Hari dd-MM-2022
        // 08:00 desc
        // 10:00 desc
        // 13:00 desc
        // 16:00 desc

        StringBuffer stbSingle = new StringBuffer();
        SimpleDateFormat formatterParser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat completeFormatter = new SimpleDateFormat("EEEE dd-MM-yyyy", new Locale("ID"));


        for (Schedule s : dataList) {
            if (s.getDate_chosen().equalsIgnoreCase(scb.getDate_chosen())) {
                if (count == 0) {

                    try {
                        Date date = formatterParser.parse(scb.getDate_chosen());
                        temp = completeFormatter.format(date);
                    } catch (Exception ex) {
                        break;
                    }

                    stbSingle.append(temp + "\n");

                }

                stbSingle.append("- " + s.getSpecific_hour() + " " + s.getDescription().trim() + "\n");
                count++;
            }
        }

        stbSingle.append("\n");
        return stbSingle.toString();

    }

    // first collecting data
    public void populate(Schedule scb) {

        if (scb.getGender_therapist() == Keys.MODE_AKHWAT) {
            listFemale.add(scb);
        } else {
            listMale.add(scb);
        }

    }


}
