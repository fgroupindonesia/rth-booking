package helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import object.Keys;

public class OrderHelper {

    int totalPrice = 0;
    int myPrice = 0;
    String message = "";

    public OrderHelper() {
        // default is general for 30.000
        myPrice = 30000;
    }

    public OrderHelper(int workMode) {
        if (workMode == Keys.PROFESI_UMUM) {
            myPrice = 30000;
            message = "[tarif umum]";
        } else if (workMode == Keys.PROFESI_PELAJAR) {
            myPrice = 15000;
            message = "[tarif pelajar]";
        }
    }

    ArrayList<String> orderList = new ArrayList<String>();

    public void resetItem(){
        orderList.clear();
    }

    public void addItem(String name) {

        name = name.replace("+", "plus");

        if (!orderList.contains(name)) {
            orderList.add(name);
        }

    }

    public void removeItem(String name) {

        name = name.replace("+", "plus");

        orderList.remove(name);

    }

    public int count(){
        return orderList.size();
    }

    public ArrayList <String> getItems(){
        return orderList;
    }

    private boolean containRuqyah(){
        for(String n: orderList){
            if(n.toLowerCase().contains("ruqyah")){
                return true;

            }
        }

        return false;
    }

    public String getTotalPrice() {
        String akhir = null;

        if(!containRuqyah()){
            akhir = message + " x " + orderList.size();
            totalPrice = orderList.size() * myPrice;
        }else{
            // we deduct the ruqyah because it is from InfaQ
            // not calculated from here
            if(orderList.size()>1){
                akhir = message + " x " + (orderList.size()-1) + " + infaQ";
            }else{
                // if only ruqyah
                akhir = " + infaQ";
            }
             totalPrice = (orderList.size()-1) * myPrice;
        }

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        String textna = kursIndonesia.format(totalPrice);

        // String akhir = textna.substring(0, textna.length() - 3);
        // we hide the total price at the moment

        return akhir;
    }

}
