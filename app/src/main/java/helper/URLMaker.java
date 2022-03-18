package helper;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class URLMaker {

    String mainURL;

    ArrayList <String> keys  = new ArrayList<>();
    ArrayList <String> values = new ArrayList<>();

    public void setMainURL(String n){
        mainURL = n;
    }

    public void addParameterValue(String d, String v){
            keys.add(d);
            try {
                values.add(URLEncoder.encode(v, "UTF-8"));
            } catch (Exception ex){
                values.add(v);
            }
    }

    private String getAllParameterValues(){

        StringBuffer stb = new StringBuffer();

        for(int x=0; x<keys.size(); x++){
            stb.append(keys.get(x) + "=" + values.get(x));
            if(x+1!=keys.size()){
                stb.append("&");
            }
        }

        return stb.toString();

    }

    public String getCompleteURL(){

        String url = null;

        if(!mainURL.contains("?")){
            url = mainURL + "?";
        }else {
            url = mainURL;
        }

        url += getAllParameterValues();

        return url;

    }

}
